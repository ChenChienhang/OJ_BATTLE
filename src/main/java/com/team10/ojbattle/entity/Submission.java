package com.team10.ojbattle.entity;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.team10.ojbattle.common.converter.LanguageEnumConverter;
import com.team10.ojbattle.common.converter.StatusEnumConverter;
import com.team10.ojbattle.common.converter.TypeEnumConverter;
import com.team10.ojbattle.common.enums.LanguageEnum;
import com.team10.ojbattle.common.enums.StatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Submission)表实体类
 *
 * @author 陈健航
 * @since 2020-05-15 22:12:01
 */
@Data
@NoArgsConstructor
public class Submission extends Model<Submission> {


   /**
    * id
    */
    @TableId(type = IdType.AUTO)
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
    private String questionTitle;

    
   /**
    * 用户id
    */
    private Long playerId;

    
   /**
    * 提交代码
    */
    private String code;

    
   /**
    * 编写语言
    */
    @JsonDeserialize(converter = LanguageEnumConverter.class)
    private LanguageEnum language;

    
   /**
    * 运行内存
    */
    private String memory;

    
   /**
    * 运行时间
    */
    private String time;

    
   /**
    * 提交结果，例如成功，编译错误等
    */
    @JsonDeserialize(converter = StatusEnumConverter.class)
    private StatusEnum status;


   /**
    * 创建时间（提交时间）
    */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


   /**
    * 修改时间
    */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}