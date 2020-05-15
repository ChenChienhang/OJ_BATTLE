package com.team10.ojbattle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.team10.ojbattle.dao.SubmissionDao;
import com.team10.ojbattle.entity.Submission;
import com.team10.ojbattle.service.SubmissionService;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * (Submission)表服务实现类
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:01
 */
@Service("submissionService")
public class SubmissionServiceImpl extends ServiceImpl<SubmissionDao, Submission>
    implements SubmissionService {

  @Override
  public IPage<Submission> listByGameIdAndUserId(
      Integer gameId, Integer userId, Integer current, Integer size) {
    LambdaQueryWrapper<Submission> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(Submission::getPlayerId, userId);
    if (gameId != null) {
      wrapper.eq(Submission::getGameId, gameId);
    }
    return page(new Page<>(current, size), wrapper);
  }
}
