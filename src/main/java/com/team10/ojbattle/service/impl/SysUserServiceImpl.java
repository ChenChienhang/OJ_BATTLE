package com.team10.ojbattle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.team10.ojbattle.dao.SysUserDao;
import com.team10.ojbattle.entity.SysRole;
import com.team10.ojbattle.entity.SysUser;
import com.team10.ojbattle.entity.auth.AuthUser;
import com.team10.ojbattle.exception.MyErrorCodeEnum;
import com.team10.ojbattle.exception.MyException;
import com.team10.ojbattle.service.SysRoleService;
import com.team10.ojbattle.service.SysUserService;

import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * (User)表服务实现类
 *
 * @author 陈健航
 * @since 2020-04-04 23:43:19
 */
@Service("sysUserService")
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService, UserDetailsService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    private JavaMailSender mailSender;

    private static final String EMAIL_QUEUE = "oj_battle_email";

    @Value("${spring.mail.username}")
    private String SENDER;

    private static final String VERIFY_CODE_KEY = "verification_code_";

    /**
     * 登录检查用
     *
     * @param username
     * @param rawPassword
     * @return
     */
    @Override
    public boolean checkLogin(String username, String rawPassword) {
        //已经做了空异常处理，一定不为空
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUserName, username);
        SysUser sysUser = baseMapper.selectOne(queryWrapper);
        if (sysUser == null) {
            throw new MyException(MyErrorCodeEnum.EMAIL_EXIST_ERROR);
        }
        System.out.println("authUser:'" + sysUser);
        //从数据库查出的用户对象
        String encodedPassword = sysUser.getPassword();
        //和加密后的密码进行比配
        if (!bCryptPasswordEncoder.matches(rawPassword, encodedPassword)) {
            //抛出密码错误异常
            System.out.println("checkLogin--------->密码不正确！");
            throw new MyException(MyErrorCodeEnum.LOGIN_ERROR);
        } else {
            return true;
        }
    }

    @Override
    public boolean register(Map<String, String> user) {
        //检查邮箱是否已经被注册过,虽然之前已经检查过，避免并发出现
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(SysUser::getEmail, user.get("email"));
        SysUser sysUser2 = this.getOne(lambdaQueryWrapper);
        if (sysUser2 != null) {
            throw new MyException(MyErrorCodeEnum.EMAIL_REG_ERROR);
        }

        //检查用户名是否已经被注册过
        lambdaQueryWrapper.clear();
        lambdaQueryWrapper.eq(SysUser::getUserName, user.get("username"));
        SysUser sysUser1 = this.getOne(lambdaQueryWrapper);
        if (sysUser1 != null) {
            throw new MyException(MyErrorCodeEnum.USERNAME_ERROR);
        }
        //检验验证码
        String key = VERIFY_CODE_KEY + user.get("email");
        System.out.println(("verification_code_" + "20172333112@m.scnu.edu.cn").equals(key));
        System.out.println(key);
        String verificationCode = stringRedisTemplate.opsForValue().get(key);
        String s = stringRedisTemplate.opsForValue().get("verification_code_20172333112@m.scnu.edu.cn");

        System.out.println(verificationCode);

        if (StringUtil.isNullOrEmpty(verificationCode)
                || !verificationCode.equals(user.get("verifyCode"))) {
            //抛出验证码异常
            throw new MyException(MyErrorCodeEnum.VERIFICATION_ERROR);
        }

        //存入数据库
        SysUser sysUser3 = new SysUser();
        sysUser3.setEmail(user.get("email"));
        sysUser3.setPassword(bCryptPasswordEncoder.encode(user.get("password")));
        sysUser3.setRegDate(new Date());
        sysUser3.setRoleId("1");
        sysUser3.setState("1");
        this.save(sysUser3);
        return true;
    }


    /**
     * 用户token校验用
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户
        LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(SysUser::getUserName, username);
        SysUser sysUser = this.getOne(userWrapper);
        if (sysUser == null) {
            System.out.println("checkLogin--------->账号不存在，请重新尝试！");
            throw new MyException(MyErrorCodeEnum.EMAIL_EXIST_ERROR);
        }
        //赋予角色
        LambdaQueryWrapper<SysRole> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.eq(SysRole::getRoleId, sysUser.getUserId());
        List<SysRole> roles = roleService.list(roleWrapper);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (SysRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        System.out.println("loadUserByUsername......user ===> " + sysUser);
        return new AuthUser(sysUser.getUserId(), sysUser.getUserName(), sysUser.getPassword(), sysUser.getState(), sysUser.getRanking(), authorities);
    }

    @Override
    public void sendRegEmailProcedure(String email) {
        //查询邮箱有没有被注册过
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getEmail, email);
        SysUser user = getOne(wrapper);
        //邮箱已经被注册过，抛异常
        if (user != null) {
            throw new MyException(MyErrorCodeEnum.EMAIL_REG_ERROR);
        }

        //生成随机六位验证码
        String verifyCode = RandomStringUtils.randomNumeric(6);
        System.out.println("verifyCode: " + verifyCode);

        //存入redis,有效期15min
        String key = VERIFY_CODE_KEY + email;
        stringRedisTemplate.opsForValue().set(key, verifyCode, 15, TimeUnit.MINUTES);

        //发送到消息队列
        Map<String, String> map = new HashMap<>(2);
        map.put("email", email);
        map.put("verifyCode", verifyCode);
        System.out.println("123");
        rabbitTemplate.convertAndSend(EMAIL_QUEUE, map);
    }

    @RabbitListener(queues = EMAIL_QUEUE)
    @Override
    public void sendRegEmailConsumer(Map<String, String> map) {
        //读取短信模板
        System.out.println(SENDER);
        InputStream is = this.getClass().getResourceAsStream("/email_content.txt");
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(is, StandardCharsets.UTF_8);
        for (; ; ) {
            int rsz = 0;
            try {
                rsz = in.read(buffer, 0, buffer.length);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("读取错误");
            }
            if (rsz < 0) {
                break;
            }
            out.append(buffer, 0, rsz);
        }
        String to = map.get("email");
        String verifyCode = map.get("verifyCode");
        String content = out.toString()
                .replace("DATE", new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                .replace("VERIFYCODE", verifyCode);
        String title = "Oj Battle 验证码";
        System.out.println("读取");
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setFrom(SENDER);
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
    }

}