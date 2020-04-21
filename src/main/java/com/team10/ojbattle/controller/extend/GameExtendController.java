package com.team10.ojbattle.controller.extend;

import com.baomidou.mybatisplus.extension.api.R;
import com.team10.ojbattle.component.HeartBeatPool;
import com.team10.ojbattle.component.MatchingPool;
import com.team10.ojbattle.controller.GameController;

import com.team10.ojbattle.entity.Submission;
import com.team10.ojbattle.exception.MyErrorCodeEnum;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * (Game)控制层扩展类，一般初次生成，后续不再覆盖。这个类提供编写自己定义的方法。
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:13
 */
@RestController
@RequestMapping("/game")
public class GameExtendController extends GameController {

    /**
     * 加入匹配池
     *
     * @return 对局数据，包括题目id，对手id
     */
    @GetMapping("/match")
    public R<String> battleMatch() {
        gameService.battleMatch();
        return R.ok(null);

    }

    /**
     * 第一次握手
     *
     * @return
     */
    @GetMapping("/firstShakeHand")
    public R<Map<String, String>> firstShakeHand() {
        Map<String, String> map = gameService.firstShakeHand();
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
        return R.ok(gameService.secondShakeHand());
    }

    /**
     * 第三次握手
     *
     * @return
     */
    @PostMapping("/thirdShakeHand")
    public R<Long> thirdShakeHand(@RequestBody Map<String, String> map) {
        String s = gameService.thirdShakeHand(map);
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
        return R.ok(Long.valueOf(s));
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
        String s = gameService.heartBeat(battleId);
        switch (s) {
            case HeartBeatPool.RET_QUIT: {
                return R.failed(MyErrorCodeEnum.QUIT_ERROR);
            }
            case HeartBeatPool.RET_FINISH: {
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
    public R<String> quit() {
        gameService.quit();
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
        gameService.submit(submission);
        return R.ok(null);
    }


}