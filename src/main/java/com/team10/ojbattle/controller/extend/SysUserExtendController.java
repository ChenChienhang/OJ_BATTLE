package com.team10.ojbattle.controller.extend;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.team10.ojbattle.controller.SysUserController;
import com.team10.ojbattle.entity.SysUser;
import com.team10.ojbattle.entity.vo.RegisterVO;
import com.team10.ojbattle.entity.vo.ResetUsrVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/upload_avatar")
    public R<String> uploadAvatar(@RequestParam("file") MultipartFile file) throws Exception {
        String avatar = sysUserService.uploadAvatar(file);
        return R.ok(avatar);
    }

    /**
     * 发送注册邮件
     *
     * @param email
     * @return
     */
    @GetMapping("/reg_email")
    public R<String> sendRegEmail(@RequestParam String email) {
        sysUserService.sendRegEmailProcedure(email);
        return R.ok(null);
    }

    /**
     * 找回密码邮件
     *
     * @param email
     * @return
     */
    @GetMapping("/find_email")
    public R<String> sendFindEmail(@RequestParam String email) {
        sysUserService.sendFindEmailProcedure(email);
        return R.ok(null);
    }

    /**
     * 注册
     *
     * @param registerVO
     * @return
     */
    @PostMapping("/register")
    public R<String> register(@RequestBody RegisterVO registerVO) {
        sysUserService.register(registerVO);
        return R.ok(null);
    }

    /**
     * 查询ranking前十的用户，返回对象仅携带id,name,ranking
     *
     * @return
     */
    @GetMapping("/top_list")
    public R<IPage<SysUser>> selectRankList(@RequestParam(defaultValue = "1", value = "pageNum") Integer current, @RequestParam(defaultValue = "10", value = "pageSize") Integer size) {
        return R.ok(sysUserService.listTopList(current, size));
    }

    @PutMapping("/reset")
    public R<String> reset(@RequestBody ResetUsrVO resetUsrVO) {
        sysUserService.reset(resetUsrVO);
        return R.ok(null);
    }


}