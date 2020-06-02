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

import java.time.LocalDateTime;

/**
 * (Question)表实体类
 *
 * @author 陈健航
 * @since 2020-05-15 22:10:05
 */
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Integer getSubmissionCount() {
        return submissionCount;
    }

    public void setSubmissionCount(Integer submissionCount) {
        this.submissionCount = submissionCount;
    }

    public Integer getAcCount() {
        return acCount;
    }

    public void setAcCount(Integer acCount) {
        this.acCount = acCount;
    }

    public ProblemDifficultyEnum getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(ProblemDifficultyEnum difficulty) {
        this.difficulty = difficulty;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}