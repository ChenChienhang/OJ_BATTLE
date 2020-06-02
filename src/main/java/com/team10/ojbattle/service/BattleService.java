package com.team10.ojbattle.service;

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
}
