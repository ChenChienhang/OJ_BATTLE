package com.team10.ojbattle.service;

import com.alibaba.fastjson.JSONObject;
import com.team10.ojbattle.entity.auth.AuthUser;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/5/14 22:26
 * @version: 1.0
 */
public interface BattleService {


    /**
     * 存储对局信息
     *
     * @param player1 玩家一
     * @param player2 玩家二
     * @return
     */
    Long openGame(AuthUser player1, AuthUser player2);


    /**
     * 填充对局信息
     *
     * @param player      用户id
     * @param gameId      题目id
     * @param isJudgePass 是否因为提交题目更新的
     */
    void cLoseGame(AuthUser player, Long gameId, boolean isJudgePass);

    /**
     * 更新题目提交次数
     *
     * @param problemId
     * @param isPass    是否通过
     */
    void refreshQuestion(Long problemId, boolean isPass);


    void refreshUser(Long problemId, boolean isPass, int difficulty);

    /**
     * 判题
     *
     * @param playerId
     * @param src
     * @param language
     * @param gameId
     * @return
     */
    JSONObject judge(AuthUser playerId, String src, Integer language, Long gameId);

    /**
     * 对局是否已经完成
     *
     * @param gameId
     * @return
     */
    boolean checkGameIsOver(String gameId);

}
