package com.team10.ojbattle.service.impl;

import com.team10.ojbattle.common.enums.TypeEnum;
import com.team10.ojbattle.entity.Game;
import com.team10.ojbattle.entity.Problem;
import com.team10.ojbattle.entity.auth.AuthUser;
import com.team10.ojbattle.service.BattleService;
import com.team10.ojbattle.service.GameService;
import com.team10.ojbattle.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/5/14 22:26
 * @version: 1.0
 */
@Service("battleService")
public class BattleServiceImpl implements BattleService {

    @Autowired
    ProblemService problemService;

    @Autowired
    GameService gameService;

    @Override
    public Long openGame(AuthUser player1, AuthUser player2) {
        Game game = new Game();
        game.setType(TypeEnum.Battle);
        game.setPlayer1Id(player1.getUserId());
        game.setPlayer1Username(player1.getUsername());
        //随机取出一条题目
        Problem problem = problemService.selectOneByRandom();
        game.setProblemTitle(problem.getTitle());
        game.setProblemId(problem.getId());
        game.setProblemDifficulty(problem.getDifficulty());
        game.setPlayer2Id(player2.getUserId());
        game.setPlayer2Username(player2.getUsername());
        gameService.save(game);
        return game.getId();
    }
}
