package com.team10.ojbattle.entity;

import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.team10.ojbattle.common.enums.ProblemDifficultyEnum;
import com.team10.ojbattle.common.enums.converter.ProblemDifficultyConverter;
import com.team10.ojbattle.common.enums.converter.TypeEnumConverter;
import com.team10.ojbattle.common.enums.TypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Game)表实体类
 *
 * @author 陈健航
 * @since 2020-05-15 22:08:38
 */
@Data
@NoArgsConstructor
public class Game extends Model<Game> {

    /**
     * 对局id
     */
    @TableId(type = IdType.AUTO)
    @ExcelIgnore
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
}