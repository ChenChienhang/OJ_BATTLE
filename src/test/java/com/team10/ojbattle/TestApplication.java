package com.team10.ojbattle;


import com.alibaba.excel.EasyExcel;
import com.team10.ojbattle.common.utils.QuestionDataListener;
import com.team10.ojbattle.common.utils.SysUserDataListener;
import com.team10.ojbattle.component.JwtTokenUtil;
import com.team10.ojbattle.dao.SysUserDao;
import com.team10.ojbattle.entity.Problem;
import com.team10.ojbattle.entity.SysUser;
import com.team10.ojbattle.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

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
    SysUserService sysUserService;

    @Autowired
    ProblemService problemService;

    @Autowired
    GameService gameService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    SysBackendApiService sysBackendApiService;

    @Autowired
    SysRoleBackendApiService sysRoleBackendApiService;

    @Autowired
    SysUserDao sysUserDao;

    @Test
    public void getBC() {
        System.out.println(bCryptPasswordEncoder.encode("123456"));
    }

    @Test
    public void excelToQuestion() {
        //根据本机xlsx的绝对路径修改
        String fileName = "C:\\Users\\CJH\\IdeaProjects\\oj-battle\\src\\main\\resources\\demo.xlsx";
        System.out.println(fileName);
        EasyExcel.read(fileName, Problem.class, new QuestionDataListener(problemService)).sheet(0).doRead();
    }

    @Test
    public void excelToUser() {
        //根据本机xlsx的绝对路径修改
        String fileName = "C:\\Users\\CJH\\IdeaProjects\\oj-battle\\src\\main\\resources\\demo.xlsx";
        System.out.println(fileName);
        EasyExcel.read(fileName, SysUser.class, new SysUserDataListener(sysUserService)).sheet(2).doRead();
    }

    /**
     * 这个有问题
     */
    @Test
    public void excelToGame() {
//        System.out.println(sysUserDao.getByEmail("888888@qq.com"));
//        String fileName = "C:\\Users\\CJH\\IdeaProjects\\oj-battle\\src\\main\\resources\\demo.xlsx";
//        System.out.println(fileName);
//        EasyExcel.read(fileName, SysRoleBackendApi.class, new SysRoleBackendApiDataListener(sysRoleBackendApiService)).sheet(3).doRead();
//        EasyExcel.read(fileName, SysBackendApi.class, new SysBackendApiDataListener(sysBackendApiService)).sheet(2).doRead();
        //根据本机xlsx的绝对路径修改
//        String fileName = "C:\\Users\\CJH\\IdeaProjects\\oj-battle\\src\\main\\resources\\demo.xlsx";
//        EasyExcel.read(fileName, Game.class, new GameDataListener(gameService)).sheet(3).doRead();
//        AntPathMatcher matcher = new AntPathMatcher();"^[0-9]*$"
//        Pattern pattern = Pattern.compile("/game\\?");
//        System.out.println(pattern.matcher("/game?pageSize=2&userId=2").find());
//        System.out.println(pattern.matcher("game/find").find());
        System.out.println(bCryptPasswordEncoder.matches("88888888", "$2a$10$e1EG850KcZUc2iaXYfvRYOjHdM3vBWZpa4sxd6rDVJ3lIvZPFAS8."));
    }


}