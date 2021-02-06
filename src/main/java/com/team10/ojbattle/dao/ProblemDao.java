package com.team10.ojbattle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.team10.ojbattle.entity.Problem;
import org.apache.ibatis.annotations.Select;

/**
 * (Question)表数据库访问层
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:21
 */
public interface ProblemDao extends BaseMapper<Problem> {

    /**
     * 随机选择一条题目
     * @return
     */
    @Select("SELECT id, title, difficulty\n" +
            "FROM problem\n" +
            "where difficulty = 1\n" +
            "ORDER BY RAND()")
    Problem selectByRandom();

}