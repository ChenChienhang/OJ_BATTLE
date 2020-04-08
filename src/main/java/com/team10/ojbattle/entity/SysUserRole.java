package com.team10.ojbattle.entity;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @TableId
    /**
     * id
     */
     private String id;

    
    /**
     * 角色id
     */
     private String roleId;

    
    /**
     * 用户id
     */
     private String userId;
}