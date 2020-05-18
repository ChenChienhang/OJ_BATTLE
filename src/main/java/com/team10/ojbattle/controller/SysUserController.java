package com.team10.ojbattle.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.team10.ojbattle.entity.SysUser;
import com.team10.ojbattle.entity.auth.AuthUser;
import com.team10.ojbattle.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (SysUser)控制层，建议不要修改，如果有新增的方法，在子类中写
 *
 * @author 陈健航
 * @since 2020-04-17 10:38:16
 */
@CrossOrigin
public class SysUserController {
    
    @Autowired
    protected SysUserService sysUserService; 
    
    /**
     * 查询所有数据
     * 
     * @return 所有数据
     */
    @GetMapping("/all")
    public R<List<SysUser>> selectAll() {
        return R.ok(this.sysUserService.list());
    }

    /**
     * 分页查询所有数据
     *
     * @param current 查询的页数
     * @param size    页面大小
     * @return 分页数据
     */
    @GetMapping("/page")
    public R<IPage<SysUser>> selectPage(@RequestParam(defaultValue = "1", value = "pageNum") Integer current, @RequestParam(defaultValue = "10", value = "pageSize") Integer size) {
        return R.ok(this.sysUserService.page(new Page<>(current, size)));
    }
    
    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    public R<SysUser> selectOne(@PathVariable Serializable id) {
        return R.ok(this.sysUserService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysUser 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R<Long> insert(@RequestBody SysUser sysUser) {
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        sysUser.setId(authUser.getUserId());
        boolean rs = this.sysUserService.save(sysUser);
        return R.ok(rs ? sysUser.getId() : 0);
    }

    /**
     * 修改数据
     *
     * @param sysUser 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R<Long> update(@RequestBody SysUser sysUser) {
        boolean rs = this.sysUserService.updateById(sysUser);
        return R.ok(rs ? sysUser.getId() : 0);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R<Boolean> delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.sysUserService.removeByIds(idList));
    }
}