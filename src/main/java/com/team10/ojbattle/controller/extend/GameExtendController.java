package com.team10.ojbattle.controller.extend;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.team10.ojbattle.controller.GameController;

import com.team10.ojbattle.entity.Game;
import org.springframework.web.bind.annotation.*;


/**
 * (Game)控制层扩展类，一般初次生成，后续不再覆盖。这个类提供编写自己定义的方法。
 *
 * @author 陈健航
 * @since 2020-04-04 23:50:13
 */
@RestController
@RequestMapping("/game")
public class GameExtendController extends GameController {

    @GetMapping
    public R<IPage<Game>> selectPageByUserId(@RequestParam String userId,
                                             @RequestParam(defaultValue = "1", value = "pageNum") Integer current,
                                             @RequestParam(defaultValue = "10", value = "pageSize") Integer size) {
        return R.ok(gameService.listPageByUserId(userId, current, size));
    }

}