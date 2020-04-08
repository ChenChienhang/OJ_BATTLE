package com.team10.ojbattle.controller;

import com.team10.ojbattle.entity.SysUser;
import com.team10.ojbattle.service.SysUserService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.api.R;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (User)控制层，建议不要修改，如果有新增的方法，在子类中写
 *
 * @author 陈健航
 * @since 2020-04-04 23:43:20
 */
@CrossOrigin
public class SysUserController {
    
    @Autowired
    protected SysUserService sysUserService;
    
    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param sysUser 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R<IPage<SysUser>>  selectAll(Page<SysUser> page, SysUser sysUser) {
        return R.ok (this.sysUserService.page(page, new QueryWrapper<>(sysUser)));
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
    public R<Integer> insert(@RequestBody SysUser sysUser) {
        boolean rs = this.sysUserService.save(sysUser);
        
        return R.ok(null);
    }

    /**
     * 修改数据
     *
     * @param sysUser 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R<Integer>  update(@RequestBody SysUser sysUser) {
        boolean rs = this.sysUserService.updateById(sysUser);
        return R.ok(null);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R<Boolean> delete(@RequestParam("idList") List<Long> idList) {
        return R.ok(this.sysUserService.removeByIds(idList));
    }
}