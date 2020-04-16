package com.team10.ojbattle.controller.extend;


import com.baomidou.mybatisplus.extension.api.R;
import com.team10.ojbattle.entity.Submission;
import com.team10.ojbattle.service.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/4/7 21:53
 * @version: 1.0
 */
@RestController
@RequestMapping("/battle")
public class BattleExtendController {

    @Autowired
    BattleService battleService;

    /**
     * 匹配开始
     *
     * @return 对局数据，包括题目id，对手id
     */
    @GetMapping("/match")
    public R<String> battleMatch() {
        battleService.battleMatch();
        return R.ok(null);
    }

    /**
     * 匹配轮询
     *
     * @return
     */
    @GetMapping("/wait")
    public R<Map<String, String>> waitForMatching() {
        Map<String, String> map = battleService.waitForMatching();
        return R.ok(map);
    }

    /**
     * 心跳保持
     *
     * @param battleId 对局id
     * @return
     */
    @PutMapping("/heartbeat/{battleId}")
    public R<String> heartBeat(@PathVariable String battleId) {
        //返回对方是否还在游戏中,没有抛出异常就是还在对局
        battleService.heartBeat(battleId);
        return R.ok(null);
    }

    @DeleteMapping("/battle/quit")
    public R<String> quit() {
        battleService.quit();
        return R.ok(null);
    }

    /**
     * 该功能未完成
     *
     * @param submission
     * @return
     */
    @PostMapping("/submission")
    public R<String> submit(@RequestBody Submission submission) {
        battleService.submit(submission);
        return R.ok(null);
    }


}
