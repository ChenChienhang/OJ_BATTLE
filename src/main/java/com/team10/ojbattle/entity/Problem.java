package com.team10.ojbattle.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.team10.ojbattle.common.enums.ProblemDifficultyEnum;
import com.team10.ojbattle.common.enums.converter.ProblemDifficultyConverter;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * (Question)表实体类
 *
 * @author 陈健航
 * @since 2020-05-15 22:10:05
 */
@Data
public class Problem extends Model<Problem> {


    /**
     * 题目id
     */
    @TableId(type = IdType.AUTO)
    @ExcelIgnore
    private Long id;

    /**
     * 标题
     */
    private String title;


    /**
     * 题目内容
     */
    private String content;


    /**
     * 示例输入
     */
    private String input;


    /**
     * 示例输出
     */
    private String output;


    /**
     * 提交的总次数
     */
    private Integer submissionCount;


    /**
     * 通过的次数
     */
    private Integer acCount;

    /**
     * 最大时间
     */
    private Integer maxCpuTime;


    /**
     * 最大内存
     */
    private Integer maxMemory;


    /**
     * 是否spj
     */
    private Integer isSpj;


    /**
     * 难度
     */
    @JsonDeserialize(converter = ProblemDifficultyConverter.class)
    private ProblemDifficultyEnum difficulty;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @ExcelIgnore
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ExcelIgnore
    private LocalDateTime updateTime;

}