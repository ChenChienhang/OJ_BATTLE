package com.team10.ojbattle.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (SysRole)表实体类
 *
 * @author 陈健航
 * @since 2020-04-08 09:48:09
 */
@Data
@NoArgsConstructor
@SuppressWarnings("serial")
public class SysRole extends Model<SysRole> {

    
    /**
     * id
     */
    @TableId
    private String roleId;

    
    /**
     * 角色名
     */
    private String roleName;

    
    /**
     * 描述
     */
    private String description;
}