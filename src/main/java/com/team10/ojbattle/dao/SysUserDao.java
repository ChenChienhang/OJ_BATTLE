package com.team10.ojbattle.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.team10.ojbattle.entity.SysUser;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.io.Serializable;
import java.util.List;

/**
 * (User)表数据库访问层
 *
 * @author 陈健航
 * @since 2020-04-04 23:43:17
 */
public interface SysUserDao extends BaseMapper<SysUser> {

    /**
     * 修改头像，数据库存的是头像的图片链接
     *
     * @param id
     * @param url
     * @return
     */
    @Update("UPDATE sys_user SET avatar = #{url} WHERE id = #{id} and flag = 0")
    int updateAvatarById(Long id, String url);

    /**
     * 查询前十按照ranking排序的用户
     *
     * @return
     */
    @Select("SELECT id,`name`,avatar, ranking FROM sys_user WHERE flag = 0 order by ranking desc, id asc limit 10")
    List<SysUser> listTopList();

    /**
     * 根据Id查用户，查出来的信息不包括password
     *
     * @param id
     * @return
     */
    @Select("SELECT id,name,avatar,email,credit,ranking,role_id,create_time,update_time FROM sys_user WHERE id= #{id} and flag = 0")
    SysUser getById(Serializable id);
}