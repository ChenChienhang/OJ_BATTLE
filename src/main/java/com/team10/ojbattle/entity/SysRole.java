package com.team10.ojbattle.entity;
import java.time.LocalDateTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @TableId(type = IdType.AUTO)
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