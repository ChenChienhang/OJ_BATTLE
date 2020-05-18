package com.team10.ojbattle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.team10.ojbattle.entity.SysBackendApi;
import com.team10.ojbattle.entity.SysRole;
import com.team10.ojbattle.entity.SysUser;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

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
     * 通过邮箱查找用户,一对多联查可访问api
     *
     * @param email
     * @return
     */
    @Select("SELECT id,name,avatar,password,email,credit,ranking,role_id,flag,create_time,update_time " +
            "FROM sys_user " +
            "WHERE flag=0 AND (email = #{email})")
    @Results({
            @Result(column = "email", property = "sysRoleList",
                    many = @Many(select = "getRoleByEmail", fetchType = FetchType.EAGER)),
            @Result(column = "email", property = "sysBackendApiList",
                    many = @Many(select = "getSysBackendApiByEmail", fetchType = FetchType.EAGER))
    })
    SysUser getByEmail(String email);


    /**
     * 根据email查找可访问api
     *
     * @param email
     * @return
     */
    @Select("SELECT DISTINCT\n" +
            "    a.url,\n" +
            "    a.method\n" +
            "FROM sys_backend_api a,\n" +
            "     sys_role b,\n" +
            "     sys_role_backend_api c,\n" +
            "     sys_user d,\n" +
            "     sys_role_user e\n" +
            "WHERE a.id = c.backend_api_id\n" +
            "     AND a.url <> 'none'\n" +
            "     AND b.id = c.role_id\n" +
            "     AND d.id = e.user_id\n" +
            "     AND e.role_id = b.id\n" +
            "     AND d.email = #{email}")
    List<SysBackendApi> getSysBackendApiByEmail(@Param("email") String email);

    /**
     * 根据email找角色
     *
     * @param email
     * @return
     */
    @Select("SELECT DISTINCT\n" +
            "       b.name\n" +
            "FROM sys_role b,\n" +
            "     sys_user d,\n" +
            "     sys_role_user e\n" +
            "WHERE  d.id = e.user_id\n" +
            "     AND e.role_id = b.id\n" +
            "     AND d.email = #{email}")
    List<SysRole> getRoleByEmail(@Param("email") String email);
}