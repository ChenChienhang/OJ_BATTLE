package com.team10.ojbattle.service.impl;

import com.team10.ojbattle.component.HeartBeatPool;
import com.team10.ojbattle.component.JwtTokenUtil;
import com.team10.ojbattle.component.MatchingPool;
import com.team10.ojbattle.entity.Submission;
import com.team10.ojbattle.entity.auth.AuthUser;
import com.team10.ojbattle.service.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/5/14 22:26
 * @version: 1.0
 */
@Service("battleService")
public class BattleServiceImpl implements BattleService {

    @Autowired
    HeartBeatPool heartBeatPool;

    @Autowired
    MatchingPool matchingPool;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Override
    public String battleMatch() {
        // 加入匹配池
        matchingPool.add();
        return jwtTokenUtil.generateToken((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    /**
     * 返回结果：1.自己已经被别人匹配，2.匹配池数量不足，3.自己匹配了别人
     *
     * @return
     */
    @Override
    public Map<String, String> firstShakeHand() {
        return matchingPool.firstShakeHand();
    }

    /**
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long secondShakeHand() {
        return matchingPool.secondShakeHand();
    }

    /**
     * 等待确认，可能等待确认异常
     * @param opponentId 对手id
     * @param opponentName 对手用户名
     * @return gameId对局id/RET_KEEP_CONFIRM轮询确认/RET_MATCH_ERROR匹配错误
     */
    @Override
    public String thirdShakeHand(String opponentId, String opponentName) {
        return matchingPool.thirdShakeHand(opponentId, opponentName);
    }

    /**
     * 心跳保持
     *
     * @param opponentId 对手id
     * @return state
     */
    @Override
    public String heartBeat(String gameId, String opponentId) {
        return heartBeatPool.keepHeartBeat(gameId, opponentId);
    }

    @Override
    public void submit(Submission submission) {
    }

    /**
     * 退出对局，删除对局
     */
    @Override
    public void quit(String gameId) {
        heartBeatPool.giveUp(gameId);
    }
}
