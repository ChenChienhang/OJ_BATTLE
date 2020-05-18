package com.team10.ojbattle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.team10.ojbattle.dao.SysRoleUserDao;
import com.team10.ojbattle.entity.SysRoleUser;
import com.team10.ojbattle.service.SysRoleUserService;
import org.springframework.stereotype.Service;

/**
 * (SysUserRole)表服务实现类
 *
 * @author 陈健航
 * @since 2020-04-08 09:49:42
 */
@Service("sysUserRoleService")
public class SysRoleUserServiceImpl extends ServiceImpl<SysRoleUserDao, SysRoleUser> implements SysRoleUserService {

}