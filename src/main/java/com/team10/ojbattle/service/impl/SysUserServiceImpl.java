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
import com.team10.ojbattle.utils.BCryptPasswordEncoder;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * (User)表服务实现类
 *
 * @author 陈健航
 * @since 2020-04-04 23:43:19
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService, UserDetailsService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    private SysRoleService roleService;

    /**
     * 登录检查用
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
        //检查邮箱是否已经被注册过
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
        String key = "verification_code_" + user.getOrDefault("email", "");
        String verificationCode = redisTemplate.opsForValue().get(key);
        if (StringUtil.isNullOrEmpty(verificationCode)
                || !verificationCode.equals(user.getOrDefault("verificationCode", ""))) {
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
        return new AuthUser(sysUser.getUserId(), sysUser.getUserName(), sysUser.getPassword(), sysUser.getState(), authorities);
    }

}