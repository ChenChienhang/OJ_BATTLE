package com.team10.ojbattle.entity;
import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Game)表实体类
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:13
 */
@Data
@NoArgsConstructor
@SuppressWarnings("serial")
public class Game extends Model<Game> {

    @TableId
    /**
     * 对局id
     */
     private String id;

    
    /**
     * 用户1
     */
     private String player1Id;

    
    /**
     * 用户1昵称
     */
     private String player1Username;

    
    /**
     * 用户2
     */
     private String player2Id;

    
    /**
     * 用户2昵称
     */
     private String player2Username;

    
    /**
     * 题目id
     */
     private String questionId;

    
    /**
     * 题目标题
     */
     private String questionTitle;

    
    /**
     * 胜利用户id
     */
     private String winnerId;

    
    /**
     * 胜利用户昵称
     */
     private String winnerUsername;

    
    /**
     * 1:对局模式，2：练习模式（方便扩展）
     */
     private String type;

    
    /**
     * 开始时间
     */
     private Date startTime;

    
    /**
     * 用户1结束时间（第一次正确提交）
     */
     private Date player1EndTime;

    
    /**
     * 用户2结束时间（第一次正确提交）
     */
     private Date player2EndTime;
}