package com.team10.ojbattle.entity;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (User)表实体类
 *
 * @author 陈健航
 * @since 2020-04-05 16:43:47
 */
@Data
@NoArgsConstructor
@SuppressWarnings("serial")
public class SysUser extends Model<SysUser> {

    
    /**
     * 用户id
     */
    @TableId
    private String userId;

    
    /**
     * 用户昵称
     */
    private String userName;

    
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
    private Integer reputation;

    
    /**
     * ranking值
     */
    private Integer ranking;

    
    /**
     * 注册时间
     */
    private Date regDate;

    
    /**
     * 用户角色（方便扩展）
     */
    private String roleId;

    
    /**
     * 用户状态，1：正常，0：注销
     */
    private String state;
}