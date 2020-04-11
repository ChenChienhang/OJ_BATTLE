package com.team10.ojbattle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.team10.ojbattle.dao.QuestionDao;
import com.team10.ojbattle.entity.Question;
import com.team10.ojbattle.service.QuestionService;
import org.springframework.stereotype.Service;

/**
 * (Question)表服务实现类
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:21
 */
@Service("questionService")
public class QuestionServiceImpl extends ServiceImpl<QuestionDao, Question> implements QuestionService {

    @Override
    public Question selectOneByRandom() {
        return baseMapper.selectByRandom();
    }
}