package com.team10.ojbattle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.team10.ojbattle.dao.QuestionDao;
import com.team10.ojbattle.entity.Question;
import com.team10.ojbattle.entity.Submission;
import com.team10.ojbattle.service.QuestionService;
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
@Service("questionService")
public class QuestionServiceImpl extends ServiceImpl<QuestionDao, Question> implements QuestionService {

    @Autowired
    SubmissionService submissionService;

    @Override
    public Question selectOneByRandom() {
        return baseMapper.selectByRandom();
    }

    @Override
    public boolean updateById(Question entity) {
        //刷新其他表的字段
        if (!StringUtil.isNullOrEmpty(entity.getTitle())) {
            LambdaUpdateWrapper<Submission> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Submission::getQuestionId, entity.getId()).set(Submission::getQuestionTitle, entity.getTitle());
            submissionService.update(wrapper);
        }
        return super.updateById(entity);
    }
}