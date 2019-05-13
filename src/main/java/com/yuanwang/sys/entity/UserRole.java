package com.yuanwang.sys.entity;
import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yuanwang.common.core.BaseEntity;

/**
 * 用户角色关系表
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * @since 2018-08-07 15:00:08
 */
public class UserRole extends BaseEntity{
	private static final long serialVersionUID = 1L;

	//用户id
	private Integer userId;	
	//角色id
	private Integer roleId;	
	
	/**获取用户id
	*/
	public Integer getUserId(){
		return this.userId;
	}
	/**设置用户id
	 * @param userId
	 */
	public void  setUserId(Integer userId){
		this.userId = userId;
	}
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
	
}
