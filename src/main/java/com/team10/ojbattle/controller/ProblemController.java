package com.team10.ojbattle.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.team10.ojbattle.entity.Problem;
import com.team10.ojbattle.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (Question)控制层，建议不要修改，如果有新增的方法，在子类中写
 *
 * @author 陈健航
 * @since 2020-04-17 10:33:45
 */
@CrossOrigin
public class ProblemController {

    @Autowired
    protected ProblemService problemService;

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping("/all")
    public R<List<Problem>> selectAll() {
        return R.ok(this.problemService.list());
    }

    /**
     * 分页查询所有数据
     *
     * @param current 查询的页数
     * @param size    页面大小
     * @return 分页数据
     */
    @GetMapping("/page")
    public R<IPage<Problem>> selectPage(@RequestParam(defaultValue = "1", value = "pageNum") Integer current, @RequestParam(defaultValue = "10", value = "pageSize") Integer size) {
        return R.ok(this.problemService.page(new Page<>(current, size)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    public R<Problem> selectOne(@PathVariable Serializable id) {
        return R.ok(this.problemService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param problem 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R<Long> insert(@RequestBody Problem problem) {
        boolean rs = this.problemService.save(problem);
        return R.ok(rs ? problem.getId() : 0);
    }

    /**
     * 修改数据
     *
     * @param problem 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R<Long> update(@RequestBody Problem problem) {
        boolean rs = this.problemService.updateById(problem);
        return R.ok(rs ? problem.getId() : 0);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R<Boolean> delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.problemService.removeByIds(idList));
    }
}