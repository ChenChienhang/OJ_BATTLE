package com.team10.ojbattle.service;

import com.team10.ojbattle.entity.Game;
import com.team10.ojbattle.entity.Submission;

import java.util.Map;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/4/7 21:58
 * @version: 1.0
 */
public interface BattleService {

    /**
     * 对局匹配
     * @return
     */
    Game battleMatch();

    /**
     * 匹配轮询
     * @return
     */
    Game waitForMatching();


    /**
     * 心跳保持
     * @param battleId
     */
    void heartBeat(String battleId);

    void submit(Submission submission);

    void quit();


}
