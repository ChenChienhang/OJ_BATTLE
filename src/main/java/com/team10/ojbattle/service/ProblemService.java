package com.team10.ojbattle.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.team10.ojbattle.entity.Problem;

/**
 * (Question)表服务接口
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:21
 */
public interface ProblemService extends IService<Problem> {

    /**
     * 随机查一道题出来
     * @return
     */
    Problem selectOneByRandom();
}