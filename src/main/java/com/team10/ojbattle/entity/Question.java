package com.team10.ojbattle.entity;
import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Question)表实体类
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:21
 */
@Data
@NoArgsConstructor
@SuppressWarnings("serial")
public class Question extends Model<Question> {

    @TableId
    /**
     * 题目id
     */
     private String id;

    
    /**
     * 标题
     */
     private String title;

    
    /**
     * 题目描述
     */
     private String summary;

    
    /**
     * 题目内容
     */
     private String content;

    
    /**
     * 题目的正确输出
     */
     private String answer;

    
    /**
     * 难度
     */
     private Object difficulty;

    
    /**
     * 创建时间
     */
     private Date createTime;
}