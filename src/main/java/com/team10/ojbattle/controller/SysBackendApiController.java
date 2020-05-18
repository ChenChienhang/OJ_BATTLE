package com.team10.ojbattle.controller;

import com.team10.ojbattle.entity.SysBackendApi;
import com.team10.ojbattle.service.SysBackendApiService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.api.R;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (SysBackendApi)控制层，建议不要修改，如果有新增的方法，在子类中写
 *
 * @author 陈健航
 * @since 2020-05-18 17:40:33
 */
@CrossOrigin
public class SysBackendApiController {
    
    @Autowired
    protected SysBackendApiService sysBackendApiService; 
    
    /**
     * 查询所有数据
     * 
     * @return 所有数据
     */
    @GetMapping("/all")
    public R<List<SysBackendApi>> selectAll() {
        return R.ok(this.sysBackendApiService.list());
    }
    
    /**
     * 分页查询所有数据
     * 
     * @param current 查询的页数
     * @param size 页面大小
     * @return 分页数据
     */
    @GetMapping
    public R<IPage<SysBackendApi>> selectPage(@RequestParam(defaultValue = "1", value = "pageNum") Integer current, @RequestParam(defaultValue = "10", value = "pageSize") Integer size) {
        return R.ok(this.sysBackendApiService.page(new Page<>(current, size)));
    }
    
    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    public R<SysBackendApi> selectOne(@PathVariable Serializable id) {
        return R.ok(this.sysBackendApiService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysBackendApi 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R<Long> insert(@RequestBody SysBackendApi sysBackendApi) {
        boolean rs = this.sysBackendApiService.save(sysBackendApi);
        return R.ok(rs ? sysBackendApi.getId() : 0);
    }

    /**
     * 修改数据
     *
     * @param sysBackendApi 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R<Long> update(@RequestBody SysBackendApi sysBackendApi) {
        boolean rs = this.sysBackendApiService.updateById(sysBackendApi);
        return R.ok(rs ? sysBackendApi.getId() : 0);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R<Boolean> delete(@RequestParam("idList") List<Long> idList) {
        return R.ok(this.sysBackendApiService.removeByIds(idList));
    }
}