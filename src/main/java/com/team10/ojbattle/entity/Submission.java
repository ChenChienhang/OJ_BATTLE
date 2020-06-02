package com.team10.ojbattle.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.team10.ojbattle.common.enums.LanguageEnum;
import com.team10.ojbattle.common.enums.StatusEnum;
import com.team10.ojbattle.common.enums.converter.LanguageEnumConverter;
import com.team10.ojbattle.common.enums.converter.StatusEnumConverter;

import java.time.LocalDateTime;

/**
 * (Submission)表实体类
 *
 * @author 陈健航
 * @since 2020-05-15 22:12:01
 */
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
    private Long problemId;

    
   /**
    * 题目标题
    */
    private String problemTitle;

    
   /**
    * 用户id
    */
    private Long playerId;

    
   /**
    * 提交代码
    */
    private String src;

    
   /**
    * 编写语言
    */
    @JsonDeserialize(converter = LanguageEnumConverter.class)
    private LanguageEnum language;

    
   /**
    * 运行内存
    */
    private Long memory;

    
   /**
    * 运行时间
    */
    private Long time;

    /**
     * 执行结果布尔变量
     */
    private Integer result;
    
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public String getProblemTitle() {
        return problemTitle;
    }

    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public LanguageEnum getLanguage() {
        return language;
    }

    public void setLanguage(LanguageEnum language) {
        this.language = language;
    }

    public Long getMemory() {
        return memory;
    }

    public void setMemory(Long memory) {
        this.memory = memory;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
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