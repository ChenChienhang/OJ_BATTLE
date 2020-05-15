package com.team10.ojbattle.controller.extend;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.team10.ojbattle.controller.SubmissionController;

import com.team10.ojbattle.entity.Submission;
import org.springframework.web.bind.annotation.*;

/**
 * (Submission)控制层扩展类，一般初次生成，后续不再覆盖。这个类提供编写自己定义的方法。
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:01
 */
@RestController
@RequestMapping("/submission")
public class SubmissionExtendController extends SubmissionController {

    @GetMapping
    public R<IPage<Submission>> selectPageByGameIdAndUserId(
            @RequestParam Integer gameId,
            @RequestParam Integer userId,
            @RequestParam(defaultValue = "1", value = "pageNum") Integer current,
            @RequestParam(defaultValue = "10", value = "pageSize") Integer size) {
        return R.ok(submissionService.listByGameIdAndUserId(gameId, userId, current, size));
    }
}
