package com.yuanwang.sys.entity;
import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yuanwang.common.core.BaseEntity;

/**
 * 角色表
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * @since 2018-08-07 15:00:08
 */
public class Role extends BaseEntity{
	private static final long serialVersionUID = 1L;

	//角色名
	private String name;	
	//描述
	private String description;	
	
	/**获取角色名
	*/
	public String getName(){
		return this.name;
	}
	/**设置角色名
	 * @param name
	 */
	public void  setName(String name){
		this.name = name;
	}
	/**获取描述
	*/
	public String getDescription(){
		return this.description;
	}
	/**设置描述
	 * @param description
	 */
	public void  setDescription(String description){
		this.description = description;
	}
	
}
