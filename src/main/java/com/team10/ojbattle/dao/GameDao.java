package com.team10.ojbattle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.team10.ojbattle.entity.Game;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.List;

/**
 * (Game)表数据库访问层
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:13
 */
public interface GameDao extends BaseMapper<Game> {

    /**
     * 查询全部
     */
    @Select("SELECT game.id,player1_id,s1.name,\n" +
            "       player2_id,s2.name,problem_id,problem.title,\n" +
            "       problem_difficulty,winner_id,winner_username,type,\n" +
            "       player1_end_time,player2_end_time,game.create_time,game.update_time\n" +
            "FROM game, sys_user s1 , sys_user s2, problem\n" +
            "where game.player1_id = s1.id && game.player2_id = s2.id && game.problem_id = problem.id"
    )
    @Results({
            @Result(column = "s1.name", property = "player1Username"),
            @Result(column = "s2.name", property = "player2Username"),
            @Result(column = "title", property = "problemTitle")
    })
    List<Game> list();

    /**
     * 根据用户id查询对战数据
     * @param page
     * @param id
     * @return
     */
    @Select("SELECT game.id,player1_id,s1.name,\n" +
            "       player2_id,s2.name,problem_id,problem.title,\n" +
            "       problem_difficulty,winner_id,winner_username,type,\n" +
            "       player1_end_time,player2_end_time,game.create_time,game.update_time\n" +
            "FROM game, sys_user s1 , sys_user s2, problem\n" +
            "where game.player1_id = s1.id && game.player2_id = s2.id && game.problem_id = problem.id " +
            "&& (player1_id = #{userId} or player2_id = #{userId})"
    )
    @Results({
            @Result(column = "s1.name", property = "player1Username"),
            @Result(column = "s2.name", property = "player2Username"),
            @Result(column = "title", property = "problemTitle")
    })
    IPage<Game> listPageByUserId(IPage<Game> page, @Param("userId") Integer id);

    /**
     * 查询分页
     */
    @Select("SELECT game.id,player1_id,s1.name,\n" +
            "       player2_id,s2.name,problem_id,problem.title,\n" +
            "       problem_difficulty,winner_id,winner_username,type,\n" +
            "       player1_end_time,player2_end_time,game.create_time,game.update_time\n" +
            "FROM game, sys_user s1 , sys_user s2, problem\n" +
            "where game.player1_id = s1.id && game.player2_id = s2.id && game.problem_id = problem.id"
    )
    @Results({
            @Result(column = "s1.name", property = "player1Username"),
            @Result(column = "s2.name", property = "player2Username"),
            @Result(column = "title", property = "problemTitle")
    })
    <E extends IPage<Game>> E page(E page);

    /**
     * 查询全部
     */
    @Select("SELECT game.id,player1_id,s1.name,\n" +
            "       player2_id,s2.name,problem_id,problem.title,\n" +
            "       problem_difficulty,winner_id,winner_username,type,\n" +
            "       player1_end_time,player2_end_time,game.create_time,game.update_time\n" +
            "FROM game, sys_user s1 , sys_user s2, problem\n" +
            "where game.player1_id = s1.id && game.player2_id = s2.id && game.problem_id = problem.id && game.id = #{id}"
    )
    @Results({
            @Result(column = "s1.name", property = "player1Username"),
            @Result(column = "s2.name", property = "player2Username"),
            @Result(column = "title", property = "problemTitle")
    })
    Game getById(@Param("id") Serializable id);

}