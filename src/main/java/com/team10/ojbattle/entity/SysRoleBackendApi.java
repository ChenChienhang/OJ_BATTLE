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
 * (SysRoleBackendApi)表实体类
 *
 * @author 陈健航
 * @since 2020-05-18 13:51:17
 */
@Data
@NoArgsConstructor
public class SysRoleBackendApi extends Model<SysRoleBackendApi> {

    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private Integer roleId;

    private Integer backendApiId;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}