package com.team10.ojbattle.entity;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (SysRole)表实体类
 *
 * @author 陈健航
 * @since 2020-04-17 12:07:57
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

    @TableField(fill = FieldFill.INSERT)
   /**
    * 创建时间
    */
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
   /**
    * 更新时间
    */
    private Date updateTime;
}