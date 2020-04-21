package com.team10.ojbattle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.team10.ojbattle.component.HeartBeatPool;
import com.team10.ojbattle.component.MatchingPool;
import com.team10.ojbattle.dao.GameDao;
import com.team10.ojbattle.entity.Game;
import com.team10.ojbattle.entity.Submission;
import com.team10.ojbattle.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * (Game)表服务实现类
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:13
 */
@Service("gameService")
public class GameServiceImpl extends ServiceImpl<GameDao, Game> implements GameService {


    @Autowired
    HeartBeatPool heartBeatPool;

    @Autowired
    MatchingPool matchingPool;

    @Override
    public void battleMatch() {
        //加入匹配池
        matchingPool.match();
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
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long secondShakeHand() {
        return matchingPool.secondShakeHand();
    }

    /**
     * 等待确认，可能等待确认异常
     *
     * @param map 对手id，对手name
     * @return gameId对局id/RET_KEEP_CONFIRM轮询确认/RET_MATCH_ERROR匹配错误
     */
    @Override
    public String thirdShakeHand(Map<String, String> map) {
        return matchingPool.thirdShakeHand(map);
    }


    /**
     * 心跳保持
     * @param opponentId 对手id
     * @return state
     */
    @Override
    public String heartBeat(String opponentId) {
        return heartBeatPool.heartBeat(opponentId);
    }

    @Override
    public void submit(Submission submission) {
    }

    /**
     * 退出对局，删除对局
     */
    @Override
    public void quit() {
        heartBeatPool.quit();
    }


}