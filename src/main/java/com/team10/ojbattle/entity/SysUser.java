package com.team10.ojbattle.entity;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (SysUser)表实体类
 *
 * @author 陈健航
 * @since 2020-04-17 12:08:04
 */
@Data
@NoArgsConstructor
@SuppressWarnings("serial")
public class SysUser extends Model<SysUser> {

    @TableId(type = IdType.AUTO)
   /**
    * 用户id
    */
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
    * ranking值
    */
    private Integer ranking;

    
   /**
    * 用户角色（方便扩展）
    */
    private String roleId;

   /**
    * 用户状态，-1:删除（注销） 0：正常
    */
    private Integer flag;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
   /**
    * 创建时间（这里可视为注册时间）
    */
    private LocalDateTime createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
   /**
    * 更新时间
    */
    private LocalDateTime updateTime;
}