package com.team10.ojbattle.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
     * 根据用户id查询对局
     * @param userId
     * @param current
     * @param size
     * @return
     */
    IPage<Game> listPageByUserId(Integer userId, Integer current, Integer size);
}