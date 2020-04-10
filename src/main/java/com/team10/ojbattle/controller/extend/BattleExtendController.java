package com.team10.ojbattle.controller.extend;


import com.baomidou.mybatisplus.extension.api.R;
import com.team10.ojbattle.entity.Game;
import com.team10.ojbattle.entity.Submission;
import com.team10.ojbattle.exception.MyErrorCodeEnum;
import com.team10.ojbattle.service.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @return
     */
    @GetMapping("/match")
    public R<Game> battleMatch() {
        Game game = battleService.battleMatch();
        if (game != null) {
            return R.ok(game);
        } else {
            return R.failed(MyErrorCodeEnum.KEEP_MATCHING_ERROR);
        }
    }

    /**
     * 匹配轮询
     * @return
     */
    @GetMapping("/wait")
    public R<Game> waitForMatching() {
        Game game = battleService.waitForMatching();
        if (game != null) {
            return R.ok(game);
        } else {
            return R.failed(MyErrorCodeEnum.KEEP_MATCHING_ERROR);
        }
    }

    /**
     * 心跳保持
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
     * @param submission
     * @return
     */
    @PostMapping("/submission")
    public R<String> submit(@RequestBody Submission submission) {
        battleService.submit(submission);
        return R.ok(null);
    }




}
