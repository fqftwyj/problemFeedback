package com.yuanwang.sys.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import com.yuanwang.sys.dao.PermissionMapper;
import com.yuanwang.sys.dao.RolePermissionMapper;
import com.yuanwang.sys.entity.Permission;
import com.yuanwang.common.core.BaseService;
/**
 * PermissionService
 * 权限表
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * 时间：2018-08-07 15:00:08
 */
@Service
public class PermissionService implements BaseService<Permission, PermissionMapper, Integer> {
	
	@Resource
	private PermissionMapper permissionMapper;
	@Resource
	private RolePermissionMapper rolePermissionMapper;
	
	@Override
	public PermissionMapper getEntityDao() {
		return permissionMapper;
	}
	
	
	/**新增权限对象
	 * @param permission
	 * @return
	 */
	public Integer addPermission(Permission permission){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("parentId", permission.getId());
		Permission maxIdPermission = permissionMapper.findOneByMap("findMaxIdPermission", map);
		if(maxIdPermission==null){
			permission.setId(permission.getId()*100+1);
			permission.setParentId(Integer.parseInt(map.get("parentId").toString()));
		}else{
			permission.setId(maxIdPermission.getId()+1);
			permission.setParentId(Integer.parseInt(map.get("parentId").toString()));
		}
		Integer falg = permissionMapper.insert(permission);
		return falg;
	}
	
	@Override
	public Integer delete(List<Integer> ids) {
		Integer flag=permissionMapper.delete(ids);
		if(flag>0){
			Map<String,Object> map=new HashMap<String,Object>();
			for(Integer id:ids){
				map.put("permissionId",id);
				rolePermissionMapper.delete(map);
			}
		}
		return flag;
	}
}
