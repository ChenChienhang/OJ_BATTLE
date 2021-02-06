package com.team10.ojbattle;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.team10.ojbattle.common.LanguageConfig;
import com.team10.ojbattle.component.JudgeServerClient;
import com.team10.ojbattle.component.JwtTokenUtil;
import com.team10.ojbattle.entity.Game;
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

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        String src = "#include <iostream>\n" +
                "using namespace std;\n" +
                "/* 函数声明 */\n" +
                "int mypow(int x, int n);\n" +
                "int is_daffodils(int val);\n" +
                " \n" +
                "/* 主函数 */\n" +
                "int main()\n" +
                "{\n" +
                "    int n;\n" +
                "    cin >> n;\n" +
                "    if (is_daffodils(n)) {\n" +
                "        cout << \"true\" << endl;\n" +
                "    } else {\n" +
                "        cout << \"false\" << endl;\n" +
                "    }\n" +
                "    return 0;\n" +
                "}\n" +
                " \n" +
                "/* 求x的n次方*/\n" +
                "int mypow(int x, int n)\n" +
                "{\n" +
                "    int i, ret;\n" +
                "    for (i = 0, ret = 1; i < n; i++) {\n" +
                "        ret *= x;\n" +
                "    }\n" +
                "    return ret;\n" +
                "}\n" +
                " \n" +
                "/* 判断是否水仙花数*/\n" +
                "int is_daffodils(int val)\n" +
                "{\n" +
                "    int n, sum, i;\n" +
                "    if (val < 0) {\n" +
                "        return 0;\n" +
                "    }\n" +
                "    /* 计算val的位数n */\n" +
                "    if (val == 0) {\n" +
                "        n = 1;\n" +
                "    } else {\n" +
                "        for (n = 0, i = val; i > 0; n++) {\n" +
                "            i /= 10;\n" +
                "        }\n" +
                "    }\n" +
                "    /* 判断是否水仙花数*/\n" +
                "    for (i = val, sum = 0; i > 0; i /= 10) {\n" +
                "        sum += mypow(i % 10, n);\n" +
                "    }\n" +
                "    if (sum == val) {\n" +
                "        return 1;\n" +
                "    } else {\n" +
                "        return 0;\n" +
                "    }\n" +
                "}\n";



        JSONObject judge = judgeServerClient.judge(
                src,
                LanguageConfig.CPP_LANG_CONFIG,
                1000,
                1024 * 1024 * 128,
                "1003",
                null,
                null,
                null,
                null,
                null,
                null
        );

        System.out.println(judge);
        List<JSONObject> data = judge.getJSONArray("data").toJavaList(JSONObject.class);
        boolean res = data.stream().noneMatch(e -> e.getInteger("result").equals(-1));
        System.out.println(res);
        //返回给前端的信息
        JSONObject ret = new JSONObject();
        ret.put("code", 2);
        //没有异常

        if (judge.get("err") == null) {
            //正常执行，更新submission表
            JSONArray jsonArray = judge.getJSONArray("data");
            //运行内存

            int a;
        } else {
            switch (judge.getString("err")) {
                case "CompileError":
                    JSONObject error = new JSONObject();
                    error.put("error", "编译错误");
                    error.put("msg", judge.getString("data"));
                    ret.put("flag", -1);
                    ret.put("data", error);
                    break;
                default:
            }
        }
    }


    @Test
    public void excelToQuestion() {
        List<Game> list = gameService.list();
        list.forEach(System.out::println);
    }

}