package com.team10.ojbattle.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.team10.ojbattle.common.LanguageConfig;
import com.team10.ojbattle.common.enums.LanguageEnum;
import com.team10.ojbattle.common.enums.ResultStatusEnum;
import com.team10.ojbattle.common.enums.TypeEnum;
import com.team10.ojbattle.common.exception.MyErrorCodeEnum;
import com.team10.ojbattle.common.exception.MyException;
import com.team10.ojbattle.component.JudgeServerClient;
import com.team10.ojbattle.entity.Game;
import com.team10.ojbattle.entity.Problem;
import com.team10.ojbattle.entity.Submission;
import com.team10.ojbattle.entity.SysUser;
import com.team10.ojbattle.entity.auth.AuthUser;
import com.team10.ojbattle.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/5/14 22:26
 * @version: 1.0
 */
@Slf4j
@Service("battleService")
public class BattleServiceImpl implements BattleService {

    @Autowired
    ProblemService problemService;

    @Autowired
    GameService gameService;

    @Autowired
    JudgeServerClient judgeServerClient;

    @Autowired
    SubmissionService submissionService;

    @Autowired
    SysUserService sysUserService;

    @Override
    public Long openGame(AuthUser player1, AuthUser player2) {
        Game game = new Game();
        game.setType(TypeEnum.Battle);
        game.setPlayer1Id(player1.getUserId());
        game.setPlayer1Username(player1.getUsername());
        //随机取出一条题目
        Problem problem = problemService.selectOneByRandom();
        game.setProblemTitle(problem.getTitle());
        game.setProblemId(problem.getId());
        game.setProblemDifficulty(problem.getDifficulty());
        game.setPlayer2Id(player2.getUserId());
        game.setPlayer2Username(player2.getUsername());
        gameService.save(game);
        return game.getId();
    }

    @Override
    public void cLoseGame(AuthUser player, Long gameId, boolean isJudgePass) {
        Game game = gameService.getById(gameId);
        if (game.getWinnerId() == null) {
            //填充胜利者
            game.setWinnerId(player.getUserId());
            game.setWinnerUsername(player.getUsername());
        }
        //填充胜利时间,isPass标识是否因为提交题目通过的
        if (isJudgePass) {
            if (game.getPlayer1Id().equals(player.getUserId()) && game.getPlayer1EndTime() == null) {
                game.setPlayer1EndTime(LocalDateTime.now());
            } else if (game.getPlayer2Id().equals(player.getUserId()) && game.getPlayer2EndTime() == null) {
                game.setPlayer2EndTime(LocalDateTime.now());
            }
        }
        gameService.updateById(game);
    }

    @Override
    public void refreshQuestion(Long problemId, boolean isPass) {
        Problem problem = problemService.getById(problemId);
        problem.setSubmissionCount(problem.getSubmissionCount() + 1);
        if (isPass) {
            problem.setAcCount(problem.getAcCount() + 1);
        }
        problemService.updateById(problem);
    }

    @Override
    public void refreshUser(Long userId, boolean isPass, int difficulty) {
        SysUser user = sysUserService.getById(userId);
        user.setSubmissionCount(user.getSubmissionCount() + 1);
        if (isPass) {
            user.setAcCount(user.getAcCount() + 1);
            user.setRating(user.getRating() + difficulty);
        }
        sysUserService.updateById(user);
    }

    @Override
    public JSONObject judge(AuthUser user, String src, Integer language, Long gameId) {
        Long playerId = user.getUserId();
        Game game = gameService.getById(gameId);
        Long problemId = game.getProblemId();
        Problem problem = problemService.getById(problemId);
        //根据语言选择语言配置文件
        Map<String, Object> languageConfig;
        switch (language) {
            case 0:
                languageConfig = LanguageConfig.C_LANG_CONFIG;
                break;
            case 1:
                languageConfig = LanguageConfig.CPP_LANG_CONFIG;
                break;
            case 2:
                languageConfig = LanguageConfig.JAVA_LANG_CONFIG;
                break;
            case 3:
                languageConfig = LanguageConfig.PY3_LANG_CONFIG;
                break;
            case 4:
                languageConfig = LanguageConfig.PY2_LANG_CONFIG;
                break;
            default:
                throw new MyException(MyErrorCodeEnum.ERROR);
        }

        JSONObject judge = judgeServerClient.judge(
                src,
                languageConfig,
                problem.getMaxCpuTime(),
                problem.getMaxMemory(),
                String.valueOf(problemId),
                true,
                null,
                null,
                null,
                null,
                null
        );
        log.info(judge.toJSONString());
        //返回给前端的信息
        JSONObject ret = new JSONObject();
        ret.put("code", 2);
        //没有异常

        if (judge.get("err") == null) {
            //正常执行，更新submission表
            List<JSONObject> data = judge.getJSONArray("data").toJavaList(JSONObject.class);
            //结果集
            boolean finalResult = data.stream().allMatch(e -> e.getInteger("result").equals(0));
            //运行内存
            AtomicReference<Integer> memory = new AtomicReference<>(0);
            data.forEach(e -> memory.updateAndGet(v -> v + e.getInteger("memory")));
            //运行时间
            AtomicReference<Integer> realTime = new AtomicReference<>(0);
            data.forEach(e -> realTime.updateAndGet(v -> v + e.getInteger("real_time")));
            //结果集
            Integer result = 0;
            for (JSONObject e : data) {
                if (!e.getInteger("result").equals(0)) {
                    result = e.getInteger("result");
                    break;
                }
            }
            //新增submission
            long submissionId = insertSubmission(
                    game.getId(),
                    language,
                    memory.get(),
                    playerId,
                    problemId,
                    problem.getTitle(),
                    result,
                    src,
                    realTime.get()
            );
            //修改question表
            refreshQuestion(problemId, finalResult);
            //修改uer表
            refreshUser(playerId, finalResult, problem.getDifficulty().getValue() + 1);
            //代码能通过就修改game表
            if (finalResult) {
                cLoseGame(user, gameId, true);
            }
            ret.put("data", submissionService.getById(submissionId));
        } else {
            switch (judge.getString("err")) {
                case "CompileError":
                    JSONObject error = new JSONObject();
                    error.put("result", -1);
                    error.put("status", "编译错误：" + judge.getString("error"));
                    ret.put("data", error);
                    insertSubmission(
                            game.getId(),
                            language,
                            0,
                            playerId,
                            problemId,
                            problem.getTitle(),
                            6,
                            src,
                            0
                    );
                    break;
                default:
            }
        }
        return ret;
    }

    /**
     * 插入提交记录
     *
     * @param gameId
     * @param languageEnum
     * @param memory
     * @param playerId
     * @param problemId
     * @param problemTitle
     * @param result
     * @param src
     * @param time
     * @return
     */
    private long insertSubmission(Long gameId, Integer languageEnum,
                                  Integer memory, Long playerId,
                                  Long problemId, String problemTitle,
                                  Integer result,
                                  String src, Integer time) {
        Submission submission = new Submission();
        submission.setGameId(gameId);
        submission.setLanguage(LanguageEnum.valueOf(languageEnum));
        submission.setMemory(memory);
        submission.setPlayerId(playerId);
        submission.setProblemId(problemId);
        submission.setProblemTitle(problemTitle);
        submission.setResult(result.equals(0) ? 0 : -1);
        submission.setStatus(ResultStatusEnum.valueOf(result));
        submission.setSrc(src);
        submission.setTime(time);
        submissionService.save(submission);
        return submission.getId();
    }

    @Override
    public boolean checkGameIsOver(String gameId) {
        Game game = gameService.getById(gameId);
        return game.getWinnerId() != null;
    }
}
