package com.team10.ojbattle;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.team10.ojbattle.common.LanguageConfig;
import com.team10.ojbattle.component.JudgeServerClient;
import com.team10.ojbattle.component.JwtTokenUtil;
import com.team10.ojbattle.websocket.MatchingServer;
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

    @Autowired
    JudgeServerClient judgeServerClient;

    @Test
    public void test() {
        String src = "    #include <stdio.h>\n" +
                "    int main(){\n" +
                "        int a, b;\n" +
                "        scanf(\"%d%d\", &a, &b);\n" +
                "        printf(\"%d\\n\", a+b);\n" +
                "        return 0;\n" +
                "    }";


        JSONObject judge = judgeServerClient.judge(
                src,
                LanguageConfig.C_LANG_CONFIG,
                1000,
                1024 * 1024 * 128,
                "normal",
                true,
                null,
                null,
                null,
                null,
                null
        );

        System.out.println(judge);


        //返回给前端的信息
        JSONObject ret = new JSONObject();
        ret.put("code", 2);
        //没有异常

        if (judge.get("err") == null) {
            //正常执行，更新submission表
            JSONArray jsonArray = judge.getJSONArray("data");
            System.out.println(jsonArray);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            Object memory = jsonObject.get("memory");
            System.out.println(memory);
            System.out.println((Integer) memory);
            //运行内存

            int a;
        } else {
            switch (judge.getString("err")) {
                case "CompileError":
                    JSONObject error = new JSONObject();
                    error.put("error", "编译错误");
                    error.put("msg", judge.getString("error"));
                    ret.put("flag", -1);
                    ret.put("data", error);
                    break;
                default:
            }
        }
    }


    @Test
    public void excelToQuestion() {
        JSONObject n = new JSONObject();
        n.put("data", 1);
        JSONObject o = new JSONObject();
        o.put("erroe", n);
        System.out.println(o.containsKey("data"));
        System.out.println(o.containsKey("erroe"));
        //根据本机xlsx的绝对路径修改
//        String fileName = "C:\\Users\\CJH\\IdeaProjects\\oj-battle\\src\\main\\resources\\demo.xlsx";
//        System.out.println(fileName);
//        EasyExcel.read(fileName, Problem.class, new QuestionDataListener(problemService)).sheet(0).doRead();
    }


}