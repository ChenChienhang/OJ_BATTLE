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
import com.team10.ojbattle.common.enums.TypeEnum;
import com.team10.ojbattle.common.enums.converter.ProblemDifficultyConverter;
import com.team10.ojbattle.common.enums.converter.TypeEnumConverter;

import java.time.LocalDateTime;

/**
 * (Game)表实体类
 *
 * @author 陈健航
 * @since 2020-05-15 22:08:38
 */
public class Game extends Model<Game> {

    /**
     * 对局id
     */
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 用户1
     */
    private Long player1Id;


    /**
     * 用户1昵称
     */
    private String player1Username;


    /**
     * 用户2
     */
    private Long player2Id;


    /**
     * 用户2昵称
     */
    private String player2Username;


    /**
     * 题目id
     */
    private Long problemId;


    /**
     * 题目标题
     */
    private String problemTitle;


    /**
     * 题目难度
     */
    @JsonDeserialize(converter = ProblemDifficultyConverter.class)
    private ProblemDifficultyEnum problemDifficulty;


    /**
     * 胜利用户id
     */
    private Long winnerId;


    /**
     * 胜利用户昵称
     */
    private String winnerUsername;


    /**
     * 1:对局模式，2：练习模式（方便扩展）
     */
    @JsonDeserialize(converter = TypeEnumConverter.class)
    @ExcelIgnore
    private TypeEnum type;


    /**
     * 用户1结束时间（第一次正确提交）
     */
    @ExcelIgnore
    private LocalDateTime player1EndTime;


    /**
     * 用户2结束时间（第一次正确提交）
     */
    @ExcelIgnore
    private LocalDateTime player2EndTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @ExcelIgnore
    private LocalDateTime createTime;


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

    public Long getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(Long player1Id) {
        this.player1Id = player1Id;
    }

    public String getPlayer1Username() {
        return player1Username;
    }

    public void setPlayer1Username(String player1Username) {
        this.player1Username = player1Username;
    }

    public Long getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(Long player2Id) {
        this.player2Id = player2Id;
    }

    public String getPlayer2Username() {
        return player2Username;
    }

    public void setPlayer2Username(String player2Username) {
        this.player2Username = player2Username;
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

    public ProblemDifficultyEnum getProblemDifficulty() {
        return problemDifficulty;
    }

    public void setProblemDifficulty(ProblemDifficultyEnum problemDifficulty) {
        this.problemDifficulty = problemDifficulty;
    }

    public Long getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Long winnerId) {
        this.winnerId = winnerId;
    }

    public String getWinnerUsername() {
        return winnerUsername;
    }

    public void setWinnerUsername(String winnerUsername) {
        this.winnerUsername = winnerUsername;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public LocalDateTime getPlayer1EndTime() {
        return player1EndTime;
    }

    public void setPlayer1EndTime(LocalDateTime player1EndTime) {
        this.player1EndTime = player1EndTime;
    }

    public LocalDateTime getPlayer2EndTime() {
        return player2EndTime;
    }

    public void setPlayer2EndTime(LocalDateTime player2EndTime) {
        this.player2EndTime = player2EndTime;
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