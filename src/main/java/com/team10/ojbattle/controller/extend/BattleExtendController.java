package com.team10.ojbattle.controller.extend;

import com.baomidou.mybatisplus.extension.api.R;
import com.team10.ojbattle.component.HeartBeatPool;
import com.team10.ojbattle.component.MatchingPool;
import com.team10.ojbattle.controller.BattleController;
import com.team10.ojbattle.entity.Submission;
import com.team10.ojbattle.common.exception.MyErrorCodeEnum;
import com.team10.ojbattle.service.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/5/14 22:24
 * @version: 1.0
 */
@RestController
@RequestMapping("/battle")
public class BattleExtendController extends BattleController {

    @Autowired
    protected BattleService battleService;

    /**
     * 加入匹配池
     *
     * @return 对局数据，包括题目id，对手id
     */
    @PutMapping("/add")
    public R<String> battleMatch() {
        battleService.battleMatch();
        return R.ok(null);
    }

    /**
     * 第一次握手
     *
     * @return
     */
    @PostMapping("/firstShakeHand")
    public R<Map<String, String>> firstShakeHand() {
        Map<String, String> map = battleService.firstShakeHand();
        switch (map.get("res")) {
            //自己被匹配到
            case MatchingPool.RET_HAVE_MATCH: {
                return R.failed(MyErrorCodeEnum.CONFIRM_ERROR);
            }
            //匹配池数量不够
            case MatchingPool.RET_KEEP_MATCH: {
                return R.failed(MyErrorCodeEnum.KEEP_MATCHING_ERROR);
            }
            default:
                break;
        }
        return R.ok(map);
    }

    /**
     * 第二次握手
     *
     * @return 游戏对局id
     */
    @GetMapping("/secondShakeHand")
    public R<Long> secondShakeHand() {
        return R.ok(battleService.secondShakeHand());
    }

    /**
     * 第三次握手
     *
     * @return
     */
    @PostMapping("/thirdShakeHand")
    public R<Integer> thirdShakeHand(@RequestBody Map<String, String> map) {
        String s = battleService.thirdShakeHand(map);
        switch (s) {
            //轮询确认
            case MatchingPool.RET_KEEP_CONFIRM: {
                return R.failed(MyErrorCodeEnum.WAIT_CONFIRM_ERROR);
            }
            //匹配错误
            case MatchingPool.RET_MATCH_ERROR: {
                return R.failed(MyErrorCodeEnum.MATCH_ERROR);
            }
            default:
        }
        //返回了对局id
        return R.ok(Integer.valueOf(s));
    }


    /**
     * 心跳保持
     * @param gameId
     * @param opponentId
     * @return
     */
    @PutMapping("/heartbeat")
    public R<String> heartBeat(@RequestParam String gameId, @RequestParam String opponentId) {
        //返回对方是否还在游戏中,没有抛出异常就是还在对局
        String s = battleService.heartBeat(gameId, opponentId);
        switch (s) {
            case HeartBeatPool.QUIT_RET: {
                return R.failed(MyErrorCodeEnum.QUIT_ERROR);
            }
            case HeartBeatPool.FINISH_RET: {
                return R.failed(MyErrorCodeEnum.PASS_ERROR);
            }
            default:
        }
        return R.ok(null);
    }

    /**
     * 退出对局
     *
     * @return
     */
    @DeleteMapping("/battle/quit")
    public R<String> quit(String gameId) {
        battleService.quit(gameId);
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
