package com.team10.ojbattle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.team10.ojbattle.dao.SubmissionDao;
import com.team10.ojbattle.entity.Submission;
import com.team10.ojbattle.service.SubmissionService;
import org.springframework.stereotype.Service;

/**
 * (Submission)表服务实现类
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:01
 */
@Service("submissionService")
public class SubmissionServiceImpl extends ServiceImpl<SubmissionDao, Submission> implements SubmissionService {

}