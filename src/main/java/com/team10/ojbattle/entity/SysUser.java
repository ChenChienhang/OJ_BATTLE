package com.team10.ojbattle.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * (SysUser)表实体类
 *
 * @author 陈健航
 * @since 2020-04-17 12:08:04
 */
@Data
public class SysUser extends Model<SysUser> {

    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String name;

    /**
     * 用户头像（方便扩展）
     */
    private String avatar;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户加密后密码
     */
    private String password;

    /**
     * 信誉值（方便扩展）
     */
    private Integer credit;

    /**
     * 提交的总次数
     */
    private Integer submissionCount;

    /**
     * 通过的次数
     */
    private Integer acCount;

    /**
     * rating值
     */
    private Integer rating;

    /**
     * 个性签名
     */
    private String profile;

    /**
     * 用户状态，-1:删除（注销） 0：正常
     */
    private Integer flag;

    /**
     * 角色1，不直接关联表对象
     */
    @TableField(exist = false)
    private List<SysRole> sysRoleList;

    /**
     * 可访问api，不直接关联表对象
     */
    @TableField(exist = false)
    private List<SysBackendApi> sysBackendApiList;

    /**
     * 创建时间（这里可视为注册时间）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}