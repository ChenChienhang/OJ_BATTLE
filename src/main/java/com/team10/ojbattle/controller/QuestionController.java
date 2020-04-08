package com.team10.ojbattle.controller;

import com.team10.ojbattle.entity.Question;
import com.team10.ojbattle.service.QuestionService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.api.R;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (Question)控制层，建议不要修改，如果有新增的方法，在子类中写
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:21
 */
@CrossOrigin
public class QuestionController {
    
    @Autowired
    protected QuestionService questionService; 
    
    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param question 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R<IPage<Question>>  selectAll(Page<Question> page, Question question) {
        return R.ok (this.questionService.page(page, new QueryWrapper<>(question)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    public R<Question> selectOne(@PathVariable Serializable id) {
        return R.ok(this.questionService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param question 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R<Integer> insert(@RequestBody Question question) {
        boolean rs = this.questionService.save(question);
        
        return R.ok(null);
    }

    /**
     * 修改数据
     *
     * @param question 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R<Integer>  update(@RequestBody Question question) {
        boolean rs = this.questionService.updateById(question);
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
        return R.ok(this.questionService.removeByIds(idList));
    }
}