package com.team10.ojbattle.controller.extend;

import com.baomidou.mybatisplus.extension.api.R;
import com.team10.ojbattle.component.FastDFSUtil;
import com.team10.ojbattle.controller.SysUserController;

import com.team10.ojbattle.entity.FastDFSFile;
import com.team10.ojbattle.entity.SysUser;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
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

    /**
     * 上传头像
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadAvatar")
    public R<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) throws Exception {
        String avatar = sysUserService.uploadAvatar(file);
        Map<String, String> res = new HashMap<>(1);
        res.put("avatar", avatar);
        return R.ok(res);
    }

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

    /**
     * 查询ranking前十的用户，返回对象仅携带id,name,ranking
     * @return
     */
    @GetMapping("/toplist")
    public R<List<SysUser>> selectRankList() {
        return R.ok(sysUserService.listTopList());
    }


}