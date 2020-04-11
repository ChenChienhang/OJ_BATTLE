package com.team10.ojbattle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.team10.ojbattle.entity.Question;
import org.apache.ibatis.annotations.Select;

/**
 * (Question)表数据库访问层
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:21
 */
public interface QuestionDao extends BaseMapper<Question> {

    /**
     * 随机选择一条题目
     * @return
     */
    @Select("SELECT id, title, summary, content, answer, difficulty, create_time " +
            "   FROM question " +
            "   ORDER BY RAND() " +
            "   LIMIT 1")
    Question selectByRandom();

}