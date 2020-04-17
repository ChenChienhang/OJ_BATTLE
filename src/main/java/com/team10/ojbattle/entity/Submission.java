package com.team10.ojbattle.entity;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Submission)表实体类
 *
 * @author 陈健航
 * @since 2020-04-17 12:07:27
 */
@Data
@NoArgsConstructor
@SuppressWarnings("serial")
public class Submission extends Model<Submission> {

    @TableId
   /**
    * id
    */
    private Long id;

    
   /**
    * 对局id
    */
    private Long gameId;

    
   /**
    * 题目id
    */
    private Long questionId;

    
   /**
    * 题目标题
    */
    private Long queetionTitle;

    
   /**
    * 用户id
    */
    private Long playerId;

    
   /**
    * 提交代码
    */
    private String code;

    
   /**
    * 0：未通过，1：通过
    */
    private Integer isCorrect;

    
   /**
    * 未通过的原因
    */
    private String result;

    @TableField(fill = FieldFill.INSERT)
   /**
    * 创建时间（提交时间）
    */
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
   /**
    * 修改时间
    */
    private Date updateTime;
}