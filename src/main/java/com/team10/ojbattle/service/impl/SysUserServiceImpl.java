package com.team10.ojbattle.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.team10.ojbattle.common.exception.MyErrorCodeEnum;
import com.team10.ojbattle.common.exception.MyException;
import com.team10.ojbattle.common.utils.FastDFSUtil;
import com.team10.ojbattle.component.EmailUtils;
import com.team10.ojbattle.dao.SysUserDao;
import com.team10.ojbattle.entity.FastDFSFile;
import com.team10.ojbattle.entity.Game;
import com.team10.ojbattle.entity.SysRoleUser;
import com.team10.ojbattle.entity.SysUser;
import com.team10.ojbattle.entity.auth.AuthUser;
import com.team10.ojbattle.entity.vo.RegisterVO;
import com.team10.ojbattle.entity.vo.ResetUsrVO;
import com.team10.ojbattle.service.GameService;
import com.team10.ojbattle.service.SysRoleService;
import com.team10.ojbattle.service.SysRoleUserService;
import com.team10.ojbattle.service.SysUserService;
import io.netty.util.internal.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * (User)表服务实现类
 *
 * @author 陈健航
 * @since 2020-04-04 23:43:19
 */
@Slf4j
@Service("sysUserService")
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser>
        implements SysUserService, UserDetailsService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    SysRoleService roleService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    GameService gameService;

    @Autowired
    SysRoleUserService sysRoleUserService;

    @Autowired
    EmailUtils emailUtils;

    private static final String EMAIL_QUEUE = "oj_battle_email";

    private static final String VERIFY_CODE_KEY = "verification_code_";

    @Override
    public boolean checkLogin(String username, String rawPassword) throws Exception {
        // 已经做了空异常处理，一定不为空
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        //这里不是用户，应该是邮箱！！！！
        queryWrapper.eq(SysUser::getEmail, username);
        SysUser sysUser = baseMapper.selectOne(queryWrapper);
        if (sysUser == null) {
            throw new Exception(String.valueOf(MyErrorCodeEnum.EMAIL_EXIST_ERROR.getCode()));
        }
        // 从数据库查出的用户对象
        String encodedPassword = sysUser.getPassword();
        // 和加密后的密码进行比配
        if (!bCryptPasswordEncoder.matches(rawPassword, encodedPassword)) {
            // 抛出密码错误异常
            throw new Exception(String.valueOf(MyErrorCodeEnum.PASSWORD_ERROR.getCode()));
        } else {
            return true;
        }
    }

    @Override
    public boolean register(RegisterVO registerVO) {
        // 检查邮箱是否已经被注册过,虽然之前已经检查过，避免并发出现
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getEmail, registerVO.getEmail());
        SysUser sysUser2 = this.getOne(lambdaQueryWrapper);
        if (sysUser2 != null) {
            throw new MyException(MyErrorCodeEnum.EMAIL_REG_ERROR);
        }

        // 检查用户名是否已经被注册过
        lambdaQueryWrapper.clear();
        lambdaQueryWrapper.eq(SysUser::getName, registerVO.getUsername());
        SysUser sysUser1 = this.getOne(lambdaQueryWrapper);
        if (sysUser1 != null) {
            throw new MyException(MyErrorCodeEnum.USERNAME_ERROR);
        }
        // 检验验证码
        String key = VERIFY_CODE_KEY + registerVO.getEmail();
        String verificationCode = stringRedisTemplate.opsForValue().get(key);
        if (StringUtil.isNullOrEmpty(verificationCode)
                || !verificationCode.equals(registerVO.getVerificationCode())) {
            // 抛出验证码异常
            throw new MyException(MyErrorCodeEnum.VERIFICATION_ERROR);
        }

        // 存入数据库
        SysUser sysUser3 = new SysUser();
        sysUser3.setName(registerVO.getUsername());
        sysUser3.setEmail(registerVO.getEmail());
        sysUser3.setPassword(registerVO.getPassword());
        this.save(sysUser3);

        //普通用户
        SysRoleUser sysRoleUser = new SysRoleUser();
        sysRoleUser.setRoleId((long) 1);
        sysRoleUser.setUserId(sysUser3.getId());
        sysRoleUserService.save(sysRoleUser);

        return true;
    }

    /**
     * 用户token校验用
     *
     * @param username 这里用email查，不是用username
     * @return
     */
    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) {
        // 这里用email查，不是用username
        SysUser sysUser = this.baseMapper.getByEmail(username);
        if (sysUser == null) {
            throw new Exception(String.valueOf(MyErrorCodeEnum.EMAIL_EXIST_ERROR.getCode()));
        }
        sysUser.setEmail(username);
        return new AuthUser(
                sysUser.getId(),
                sysUser.getName(),
                sysUser.getPassword(),
                sysUser.getRating(),
                sysUser.getAvatar(),
                sysUser.getEmail(),
                sysUser.getSysBackendApiList(),
                sysUser.getSysRoleList());
    }

    @Override
    public void sendRegEmailProcedure(String email) {
        // 查询邮箱有没有被注册过
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getEmail, email);
        SysUser user = getOne(wrapper);
        // 邮箱已经被注册过，抛异常
        if (user != null) {
            throw new MyException(MyErrorCodeEnum.EMAIL_REG_ERROR);
        }

        // 生成随机六位验证码
        String verifyCode = RandomStringUtils.randomNumeric(6);
        log.info("verifyCode: " + verifyCode);

        // 存入redis,有效期15min
        String key = VERIFY_CODE_KEY + email;
        stringRedisTemplate.opsForValue().set(key, verifyCode, 15, TimeUnit.MINUTES);

        // 发送到消息队列
        Map<String, String> map = new HashMap<>(2);
        map.put("email", email);
        map.put("verifyCode", verifyCode);
        rabbitTemplate.convertAndSend(EMAIL_QUEUE, map);
    }

    @RabbitListener(queues = EMAIL_QUEUE)
    @Override
    public void sendRegEmailConsumer(Map<String, String> map) {
        try {
            String to = map.get("email");
            String verifyCode = map.get("verifyCode");
            String content =
                    emailUtils.readContent("email_content.txt")
                            .replace("DATE", new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                            .replace("VERIFYCODE", verifyCode);
            String title = "Oj Battle 验证码";
            emailUtils.sendEmail(to, title, content);
            log.info("发送验证码" + verifyCode + "到" + to);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String uploadAvatar(MultipartFile file) throws Exception {
        // 调用工具类上传
        FastDFSFile fastDFSFile =
                new FastDFSFile(
                        file.getOriginalFilename(),
                        file.getBytes(),
                        // 获取文件扩展名
                        StringUtils.getFilenameExtension(file.getOriginalFilename()),
                        null,
                        null);
        String[] upload = FastDFSUtil.upload(fastDFSFile);
        String url = FastDFSUtil.getTrackerInfo() + "/" + upload[0] + "/" + upload[1];
        // 取出userid
        AuthUser authUser =
                (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = authUser.getUserId();
        // 拼接访问地址 url = http://192.168.0.1:8080/group1/M00/00/00/wewaefawefawf.jpeg,
        // ngnix和FastDFS端口一致的情况下可以直接拼接访问？？？为什么
        baseMapper.updateAvatarById(userId, url);
        return url;
    }

    @Override
    public IPage<SysUser> listTopList(Integer current, Integer size) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SysUser::getRating);
        return this.page(new Page<>(current, size), wrapper);
    }

    @Override
    public void sendFindEmailProcedure(String email) {
        // 查询邮箱有没有被注册过
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getEmail, email);
        SysUser user = getOne(wrapper);
        // 邮箱不存在，无法找回密码
        if (user == null) {
            throw new MyException(MyErrorCodeEnum.EMAIL_EXIST_ERROR);
        }

        // 生成随机六位验证码
        String verifyCode = RandomStringUtils.randomNumeric(6);
        System.out.println("verifyCode: " + verifyCode);

        // 存入redis,有效期15min
        String key = VERIFY_CODE_KEY + email;
        stringRedisTemplate.opsForValue().set(key, verifyCode, 15, TimeUnit.MINUTES);

        // 发送到消息队列
        Map<String, String> map = new HashMap<>(2);
        map.put("email", email);
        map.put("verifyCode", verifyCode);
        rabbitTemplate.convertAndSend(EMAIL_QUEUE, map);
    }

    @Override
    public void reset(ResetUsrVO resetUsrVO) {
        // 检验验证码
        String key = VERIFY_CODE_KEY + resetUsrVO.getEmail();
        String verificationCode = stringRedisTemplate.opsForValue().get(key);
        if (StringUtil.isNullOrEmpty(verificationCode)
                || !verificationCode.equals(resetUsrVO.getVerificationCode())) {
            // 抛出验证码异常
            throw new MyException(MyErrorCodeEnum.VERIFICATION_ERROR);
        }

        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysUser::getEmail, resetUsrVO.getEmail()).set(SysUser::getPassword, bCryptPasswordEncoder.encode(resetUsrVO.getPassword()));
        boolean update = update(wrapper);
        //找不到该邮箱
        if (!update) {
            throw new MyException(MyErrorCodeEnum.EMAIL_EXIST_ERROR);
        }
    }


    //=================下面方法均为了加密密码重写（aop切起来麻烦就算了）========================

    /**
     * 重写，防止查出密码等
     *
     * @param id
     * @return
     */
    @Override
    public SysUser getById(Serializable id) {
        SysUser user = super.getById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    /**
     * 重写，防止查出密码等
     *
     * @return
     */
    @Override
    public List<SysUser> list() {
        List<SysUser> list = super.list();
        if (list != null) {
            list.forEach(e -> e.setPassword(null));
        }
        return list;
    }

    /**
     * 重写，防止查出密码等
     *
     * @param page
     * @return
     */
    @Override
    public <E extends IPage<SysUser>> E page(E page) {
        E res = super.page(page);
        if (res.getRecords() != null) {
            res.getRecords().forEach(e -> e.setPassword(null));
        }
        return res;
    }

    @Override
    public <E extends IPage<SysUser>> E page(E page, Wrapper<SysUser> queryWrapper) {
        E res = super.page(page, queryWrapper);
        if (res.getRecords() != null) {
            res.getRecords().forEach(e -> e.setPassword(null));
        }
        return res;
    }

    /**
     * 重写，密码加密
     *
     * @param entity
     * @return
     */
    @Override
    public boolean save(SysUser entity) {
        entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
        return super.save(entity);
    }

    /**
     * 重写，密码加密
     *
     * @param entityList
     * @return
     */
    @Override
    public boolean saveBatch(Collection<SysUser> entityList) {
        entityList.forEach(e -> e.setPassword(bCryptPasswordEncoder.encode(e.getPassword())));
        return super.saveBatch(entityList);
    }


    @Override
    public boolean updateById(SysUser entity) {
        //密码加密
        if (!StringUtil.isNullOrEmpty(entity.getPassword())) {
            entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
        }
        //改了名称
        if (!StringUtil.isNullOrEmpty(entity.getName())) {
            LambdaUpdateWrapper<Game> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(Game::getPlayer1Username, entity.getName()).eq(Game::getPlayer1Id, entity.getId());
            gameService.update(wrapper);
            wrapper.clear();
            wrapper.set(Game::getPlayer2Username, entity.getName()).eq(Game::getPlayer2Id, entity.getId());
            gameService.update(wrapper);
            wrapper.clear();
            wrapper.set(Game::getWinnerUsername, entity.getName()).eq(Game::getWinnerId, entity.getId());
            gameService.update(wrapper);
        }
        return super.updateById(entity);
    }
}
