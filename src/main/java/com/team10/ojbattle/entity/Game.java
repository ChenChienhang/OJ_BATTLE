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
 * @since 2020-04-17 10:32:25
 */
@Data
@NoArgsConstructor
@SuppressWarnings("serial")
public class Game extends Model<Game> {

    @TableId
    /**
     * 对局id
     */
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
     private Long questionId;

    
    /**
     * 题目标题
     */
     private String questionTitle;

    
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
     private Integer type;

    
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

    
    /**
     * 创建时间
     */
     private Date createTime;

    
    /**
     * 更新时间
     */
     private Date updateTime;
}