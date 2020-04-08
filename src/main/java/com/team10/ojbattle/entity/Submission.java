package com.team10.ojbattle.entity;
import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Submission)表实体类
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:00
 */
@Data
@NoArgsConstructor
@SuppressWarnings("serial")
public class Submission extends Model<Submission> {

    @TableId
    /**
     * id
     */
     private String id;

    
    /**
     * 对局id
     */
     private String gameId;

    
    /**
     * 题目id
     */
     private String questionId;

    
    /**
     * 题目标题
     */
     private String questionTitle;

    
    /**
     * 用户id
     */
     private String playerId;

    
    /**
     * 代码
     */
     private String code;

    
    /**
     * 0:未通过，1：通过
     */
     private String isCorrect;

    
    /**
     * 如果不通过的原因，例如编译错误，结果错误，超时
     */
     private String result;

    
    /**
     * 提交时间
     */
     private Date submitTime;
}