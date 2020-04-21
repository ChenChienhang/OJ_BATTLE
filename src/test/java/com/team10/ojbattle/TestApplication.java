package com.team10.ojbattle;


import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.team10.ojbattle.component.JwtTokenUtil;
import com.team10.ojbattle.entity.SysUser;
import com.team10.ojbattle.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.UnknownServiceException;
import java.util.Collection;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/4/8 14:48
 * @version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestApplication {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    SysUserService sysUserService;


    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void getBC(){
        System.out.println(bCryptPasswordEncoder.encode("123456"));
        System.out.println(jwtTokenUtil.generateToken(new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public String getUsername() {
                return "123456";
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return false;
            }
        }));
        return;
    }

    @Test
    public void rabbitTest() throws IOException {
//        String to = "20172333112@m.scnu.edu.cn";
//        String verifyCode = "verifyCode";
//        String content = "123";
//        String title = "Oj Battle 验证码";
//        System.out.println("读取" );
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setFrom("853804445@qq.com");
//        message.setSubject(title);
//        message.setSentDate(new Date());
//        message.setText(content);
//        mailSender.send(message);
//        String s = stringRedisTemplate.opsForValue().get("verification_code_20172333112@m.scnu.edu.cn");
//        System.out.println(s);
        SysUser user = new SysUser();
        sysUserService.save(user);

    }



}