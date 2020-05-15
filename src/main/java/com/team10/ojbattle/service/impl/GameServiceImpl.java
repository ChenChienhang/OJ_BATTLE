package com.team10.ojbattle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Override
    public IPage<Game> listPageByUserId(String userId, Integer current, Integer size) {
        LambdaQueryWrapper<Game> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Game::getPlayer1Id, userId).or().eq(Game::getPlayer2Id, userId);
        return page(new Page<>(current, size), wrapper);
    }
}