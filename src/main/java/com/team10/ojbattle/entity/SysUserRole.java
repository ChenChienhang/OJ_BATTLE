package com.team10.ojbattle.entity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * (SysUserRole)表实体类
 *
 * @author 陈健航
 * @since 2020-04-08 09:49:42
 */
@Data
@NoArgsConstructor
@SuppressWarnings("serial")
public class SysUserRole extends Model<SysUserRole> {


    /**
     * id
     */
     @TableId(type = IdType.AUTO)
     private Long id;

    
    /**
     * 角色id
     */
     private Long roleId;

    
    /**
     * 用户id
     */
     private Long userId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}