package com.yuanwang.sys.entity;
import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yuanwang.common.core.BaseEntity;

/**
 * 角色权限关系表
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * @since 2018-08-07 15:00:08
 */
public class RolePermission extends BaseEntity{
	private static final long serialVersionUID = 1L;

	//角色id
	private Integer roleId;	
	//权限id
	private Integer permissionId;	
	
	/**获取角色id
	*/
	public Integer getRoleId(){
		return this.roleId;
	}
	/**设置角色id
	 * @param roleId
	 */
	public void  setRoleId(Integer roleId){
		this.roleId = roleId;
	}
	/**获取权限id
	*/
	public Integer getPermissionId(){
		return this.permissionId;
	}
	/**设置权限id
	 * @param permissionId
	 */
	public void  setPermissionId(Integer permissionId){
		this.permissionId = permissionId;
	}
	
}
