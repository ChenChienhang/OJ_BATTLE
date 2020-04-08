package com.team10.ojbattle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.team10.ojbattle.dao.GameDao;
import com.team10.ojbattle.entity.Game;
import com.team10.ojbattle.service.GameService;
import org.springframework.stereotype.Service;

/**
 * (Game)表服务实现类
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:13
 */
@Service("gameService")
public class GameServiceImpl extends ServiceImpl<GameDao, Game> implements GameService {

}