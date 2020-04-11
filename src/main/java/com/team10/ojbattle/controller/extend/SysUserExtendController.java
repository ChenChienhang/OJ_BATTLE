package com.team10.ojbattle.controller.extend;

import com.baomidou.mybatisplus.extension.api.R;
import com.team10.ojbattle.controller.SysUserController;

import org.springframework.web.bind.annotation.*;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.Map;


/**
 * (User)控制层扩展类，一般初次生成，后续不再覆盖。这个类提供编写自己定义的方法。
 *
 * @author 陈健航
 * @since 2020-04-04 23:43:20
 */
@RestController
@RequestMapping("/user")
public class SysUserExtendController extends SysUserController {

    @GetMapping("/verifycode/{email}")
    public R<String> sendEmail(@PathVariable String email) {
        sysUserService.sendRegEmailProcedure(email);
        return R.ok(null);
    }

    @PostMapping("/register")
    public R<String> register(@RequestBody Map<String, String> map) {
        sysUserService.register(map);
        return R.ok(null);
    }


}