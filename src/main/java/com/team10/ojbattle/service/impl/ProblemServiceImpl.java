package com.team10.ojbattle.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.team10.ojbattle.dao.ProblemDao;
import com.team10.ojbattle.entity.Game;
import com.team10.ojbattle.entity.Problem;
import com.team10.ojbattle.entity.Submission;
import com.team10.ojbattle.service.GameService;
import com.team10.ojbattle.service.ProblemService;
import com.team10.ojbattle.service.SubmissionService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (Question)表服务实现类
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:21
 */
@Service("problemService")
public class ProblemServiceImpl extends ServiceImpl<ProblemDao, Problem> implements ProblemService {

    @Autowired
    SubmissionService submissionService;

    @Autowired
    GameService gameService;

    @Override
    public Problem selectOneByRandom() {
        return baseMapper.selectByRandom();
    }

    @Override
    public boolean updateById(Problem entity) {
        //更新标题
        if (!StringUtil.isNullOrEmpty(entity.getTitle())) {
            LambdaUpdateWrapper<Submission> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Submission::getProblemId, entity.getId()).set(Submission::getProblemTitle, entity.getTitle());
            submissionService.update(wrapper);

            LambdaUpdateWrapper<Game> wrapper1 = new LambdaUpdateWrapper<>();
            wrapper1.eq(Game::getProblemId, entity.getId()).set(Game::getProblemTitle, entity.getTitle());
            gameService.update(wrapper1);
        }
        //更新题目难度
        if (entity.getDifficulty() != null) {
            LambdaUpdateWrapper<Game> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Game::getProblemId, entity.getId()).set(Game::getProblemDifficulty, entity.getDifficulty());
            gameService.update(wrapper);
        }
        return super.updateById(entity);
    }
}