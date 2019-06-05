package com.yuanwang.sys.entity;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yuanwang.common.core.BaseEntity;
import com.yuanwang.sys.entity.enums.BuiltInEnum;
import com.yuanwang.sys.entity.enums.StatusEnum;

/**
 * 用户表
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * @since 2018-08-07 15:00:08
 */
public class User extends BaseEntity{
	private static final long serialVersionUID = 1L;

	//用户名
	private String userName;
	//密码
	private String password;

	private List<Role> roles;
	//用户状态
	private StatusEnum status;

	//错误次数
	private Integer errorNum;
	//是否内置(0:FALSE:,1:是:TRUE)
	private BuiltInEnum builtIn;
	//真实姓名
    private String realName;

    //科室名称
	private String officeName;
	//联系电话
	private String phone;

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public StatusEnum getStatus() {
		return status;
	}
	public void setStatus(StatusEnum status) {
		this.status = status;
	}
	private List<Permission> permissions;
	/**获取用户名
	 */
	public String getUserName(){
		return this.userName;
	}
	/**设置用户名
	 * @param userName
	 */
	public void  setUserName(String userName){
		this.userName = userName;
	}
	/**获取密码
	 */
	public String getPassword(){
		return this.password;
	}
	/**设置密码
	 * @param password
	 */
	public void  setPassword(String password){
		this.password = password;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public List<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
	public Integer getErrorNum() {
		return errorNum;
	}
	public void setErrorNum(Integer errorNum) {
		this.errorNum = errorNum;
	}
	/**获取是否内置【enum】(0:FALSE:,1:是:TRUE)
	 */
	public BuiltInEnum getBuiltIn(){
		return this.builtIn;
	}
	/**设置是否内置【enum】(0:FALSE:,1:是:TRUE)
	 * @param builtIn
	 */
	public void  setBuiltIn(BuiltInEnum builtIn){
		this.builtIn = builtIn;
	}
}
