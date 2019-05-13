package com.yuanwang.sys.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuanwang.sys.dao.RolePermissionMapper;
import com.yuanwang.sys.entity.RolePermission;
import com.yuanwang.common.core.BaseService;
/**
 * RolePermissionService
 * 角色权限关系表
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * 时间：2018-08-07 15:00:08
 */
@Service
public class RolePermissionService implements BaseService<RolePermission, RolePermissionMapper, Integer> {
	
	@Resource
	private RolePermissionMapper rolePermissionMapper;
	@Override
	public RolePermissionMapper getEntityDao() {
		return rolePermissionMapper;
	}
	
	
}
