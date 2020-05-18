package com.team10.ojbattle.service;

import com.team10.ojbattle.entity.Submission;

import java.util.Map;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/5/14 22:26
 * @version: 1.0
 */
public interface BattleService {

    /**
     * 对局匹配
     *
     * @return
     */
    String battleMatch();

    /**
     * 匹配轮询
     *
     * @return 可能抛出2种异常，1.还在匹配中。2.自身已经被匹配。顺利执行返回表示该用户成功匹配对手，返回map。 batlleId和battleName
     */
    Map<String, String> firstShakeHand();

    /**
     * 心跳保持
     * @param gameId 游戏id
     * @param opponentId 对局id
     * @return
     */
    String heartBeat(String gameId, String opponentId);

    /**
     * 提交
     *
     * @param submission 提交记录
     */
    void submit(Submission submission);

    /**
     * 退出游戏
     * @param gameId 游戏id
     */
    void quit(String gameId);

    /**
     * 开启对局
     *
     * @return 返回
     */
    Long secondShakeHand();


    /**
     * 确认对方开启对局
     * @param opponentId 对手id
     * @param opponentName 对手用户名
     * @return 对局id
     */
    String thirdShakeHand(String opponentId, String opponentName);
}
