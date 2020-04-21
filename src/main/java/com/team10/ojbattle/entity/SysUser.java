package com.team10.ojbattle.entity;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
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

    @TableId(type = IdType.ASSIGN_ID)
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
    * 用户状态，1：正常，0：注销
    */
    private Integer state;

    @TableField(fill = FieldFill.INSERT)
   /**
    * 创建时间（这里可视为注册时间）
    */
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
   /**
    * 更新时间
    */
    private Date updateTime;
}