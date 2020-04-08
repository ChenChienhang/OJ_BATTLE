package com.team10.ojbattle.controller;

import com.team10.ojbattle.entity.Game;
import com.team10.ojbattle.service.GameService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.api.R;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (Game)控制层，建议不要修改，如果有新增的方法，在子类中写
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:13
 */
@CrossOrigin
public class GameController {
    
    @Autowired
    protected GameService gameService; 
    
    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param game 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R<IPage<Game>>  selectAll(Page<Game> page, Game game) {
        return R.ok (this.gameService.page(page, new QueryWrapper<>(game)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    public R<Game> selectOne(@PathVariable Serializable id) {
        return R.ok(this.gameService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param game 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R<Integer> insert(@RequestBody Game game) {
        boolean rs = this.gameService.save(game);
        return R.ok(null);
    }

    /**
     * 修改数据
     *
     * @param game 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R<Long>  update(@RequestBody Game game) {
        boolean rs = this.gameService.updateById(game);
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
        return R.ok(this.gameService.removeByIds(idList));
    }
}