package com.team10.ojbattle.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * (SysBackendApi)表实体类
 *
 * @author 陈健航
 * @since 2020-05-18 13:51:27
 */
@Data
@NoArgsConstructor
public class SysBackendApi extends Model<SysBackendApi> {


    /**
     * 主键
     */
    @TableId(type = IdType.INPUT)
    private Long id;


    /**
     * API名称
     */
    private String name;


    /**
     * API请求地址
     */
    private String url;


    /**
     * API请求方式：GET、POST、PUT、DELETE
     */
    private String method;


    /**
     * 父ID
     */
    private Long pid;


    /**
     * 描述
     */
    private String description;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}