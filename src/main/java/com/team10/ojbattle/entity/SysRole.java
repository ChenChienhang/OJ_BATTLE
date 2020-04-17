package com.team10.ojbattle.entity;
import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (SysRole)表实体类
 *
 * @author 陈健航
 * @since 2020-04-17 10:38:09
 */
@Data
@NoArgsConstructor
@SuppressWarnings("serial")
public class SysRole extends Model<SysRole> {

    @TableId
    /**
     * id
     */
     private Long id;

    
    /**
     * 角色名
     */
     private String name;

    
    /**
     * 描述
     */
     private String description;

    
    /**
     * 创建时间
     */
     private Date createTime;

    
    /**
     * 更新时间
     */
     private Date updateTime;
}