package com.team10.ojbattle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.team10.ojbattle.entity.Game;
import com.team10.ojbattle.entity.Submission;

import java.util.Map;

/**
 * (Game)表服务接口
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:13
 */
public interface GameService extends IService<Game> {
    /**
     * 对局匹配
     *
     * @return
     */
    void battleMatch();

    /**
     * 匹配轮询
     *
     * @return 可能抛出2种异常，1.还在匹配中。2.自身已经被匹配。顺利执行返回表示该用户成功匹配对手，返回map。
     * batlleId和battleName
     */
    Map<String, String> firstShakeHand();


    /**
     * 心跳保持
     *
     * @param battleId
     * @return
     */
    String heartBeat(String battleId);

    /**
     * 提交
     *
     * @param submission
     */
    void submit(Submission submission);

    /**
     * 退出游戏
     */
    void quit();

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