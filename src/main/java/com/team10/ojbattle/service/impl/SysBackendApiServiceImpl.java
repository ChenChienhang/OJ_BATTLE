package com.team10.ojbattle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.team10.ojbattle.dao.SysBackendApiDao;
import com.team10.ojbattle.entity.SysBackendApi;
import com.team10.ojbattle.service.SysBackendApiService;
import org.springframework.stereotype.Service;

/**
 * (SysBackendApi)表服务实现类
 *
 * @author 陈健航
 * @since 2020-05-18 13:53:46
 */
@Service("sysBackendApiService")
public class SysBackendApiServiceImpl extends ServiceImpl<SysBackendApiDao, SysBackendApi> implements SysBackendApiService {

}