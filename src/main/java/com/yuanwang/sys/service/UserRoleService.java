package com.yuanwang.sys.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuanwang.sys.dao.UserRoleMapper;
import com.yuanwang.sys.entity.UserRole;
import com.yuanwang.common.core.BaseService;
/**
 * UserRoleService
 * 用户角色关系表
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * 时间：2018-08-07 15:00:08
 */
@Service
public class UserRoleService implements BaseService<UserRole, UserRoleMapper, Integer> {
	
	@Resource
	private UserRoleMapper userRoleMapper;
	@Override
	public UserRoleMapper getEntityDao() {
		return userRoleMapper;
	}
	
	
}
