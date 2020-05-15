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
    void battleMatch();

    /**
     * 匹配轮询
     *
     * @return 可能抛出2种异常，1.还在匹配中。2.自身已经被匹配。顺利执行返回表示该用户成功匹配对手，返回map。 batlleId和battleName
     */
    Map<String, String> firstShakeHand();

    /**
     * 心跳保持
     * @param gameId
     * @param opponentId
     * @return
     */
    String heartBeat(String gameId, String opponentId);

    /**
     * 提交
     *
     * @param submission
     */
    void submit(Submission submission);

    /**
     * 退出游戏
     */
    void quit(String gameId);

    /**
     * 开启对局
     *
     * @return
     */
    Long secondShakeHand();

    /**
     * 确认对方开启对局
     *
     * @param map
     * @return
     */
    String thirdShakeHand(Map<String, String> map);
}
