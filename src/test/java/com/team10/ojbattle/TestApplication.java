package com.team10.ojbattle;


import com.alibaba.excel.EasyExcel;
import com.team10.ojbattle.common.utils.excel.QuestionDataListener;
import com.team10.ojbattle.component.JwtTokenUtil;
import com.team10.ojbattle.component.MatchingServer;
import com.team10.ojbattle.entity.Problem;
import com.team10.ojbattle.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
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
@SpringBootTest(classes = OjBattleApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestApplication {

    @Autowired
    ApplicationContext applicationContext;

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
    SubmissionService submissionService;

    @Autowired
    MatchingServer matchingServer;

    @Test
    public void getBC() {
//        Long aLong = stringRedisTemplate.opsForZSet().removeRange("MATCH_POOL", 0, stringRedisTemplate.opsForZSet().size("MATCH_POOL") - 1);
//        System.out.println(aLong);
//        stringRedisTemplate.opsForSet().add("1", "1");
//        System.out.println(stringRedisTemplate.opsForSet().size("1"));
//        stringRedisTemplate.opsForSet().remove("1", "1");
        System.out.println(stringRedisTemplate.opsForSet().size("2"));

    }

    @Test
    public void excelToQuestion() {
        //根据本机xlsx的绝对路径修改
        String fileName = "C:\\Users\\CJH\\IdeaProjects\\oj-battle\\src\\main\\resources\\demo.xlsx";
        System.out.println(fileName);
        EasyExcel.read(fileName, Problem.class, new QuestionDataListener(problemService)).sheet(0).doRead();
    }


}