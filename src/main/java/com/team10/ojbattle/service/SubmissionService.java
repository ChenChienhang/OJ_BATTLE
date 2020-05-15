package com.team10.ojbattle.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.team10.ojbattle.entity.Submission;

/**
 * (Submission)表服务接口
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:01
 */
public interface SubmissionService extends IService<Submission> {

    /**
     * 查询某用户某次对局全部提交记录
     * @param gameId
     * @param userId
     * @param current
     * @param size
     * @return
     */
    IPage<Submission> listByGameIdAndUserId(Integer gameId, Integer userId, Integer current, Integer size);
}