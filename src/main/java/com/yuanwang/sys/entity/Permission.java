package com.yuanwang.sys.entity;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yuanwang.common.core.BaseEntity;
import com.yuanwang.sys.entity.enums.PermissionTypeEnum;
/**
 * 权限表
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * @since 2018-09-10 17:49:08
 */
public class Permission extends BaseEntity{
	private static final long serialVersionUID = 1L;

	//权限名
	private String permissionName;	
	//编号
	private String permissionCode;	
	private PermissionTypeEnum permissionType;
	//权限url
	private String permissionUrl;	
	//
	private String permissionIcon;	
	//描述
	private String description;	
	//排序
	private Integer sort;	
	//父权限id
	private Integer parentId;	
	//子权限集合
	private List<Permission> sonPermission;
	/**获取权限名
	*/
	public String getPermissionName(){
		return this.permissionName;
	}
	/**设置权限名
	 * @param permissionName
	 */
	public void  setPermissionName(String permissionName){
		this.permissionName = permissionName;
	}
	/**获取编号
	*/
	public String getPermissionCode(){
		return this.permissionCode;
	}
	/**设置编号
	 * @param permissionCode
	 */
	public void  setPermissionCode(String permissionCode){
		this.permissionCode = permissionCode;
	}
	/**获取权限类型【enum】(0:普通菜单:menu,1:链接:link,2:功能:method)
	*/
	public PermissionTypeEnum getPermissionType(){
		return this.permissionType;
	}
	/**设置权限类型【enum】(0:普通菜单:menu,1:链接:link,2:功能:method)
	 * @param permissionType
	 */
	public void  setPermissionType(PermissionTypeEnum permissionType){
		this.permissionType = permissionType;
	}
	/**获取权限url
	*/
	public String getPermissionUrl(){
		return this.permissionUrl;
	}
	/**设置权限url
	 * @param permissionUrl
	 */
	public void  setPermissionUrl(String permissionUrl){
		this.permissionUrl = permissionUrl;
	}
	/**获取
	*/
	public String getPermissionIcon(){
		return this.permissionIcon;
	}
	/**设置
	 * @param permissionIcon
	 */
	public void  setPermissionIcon(String permissionIcon){
		this.permissionIcon = permissionIcon;
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
	/**获取排序
	*/
	public Integer getSort(){
		return this.sort;
	}
	/**设置排序
	 * @param sort
	 */
	public void  setSort(Integer sort){
		this.sort = sort;
	}
	/**获取父权限id
	*/
	public Integer getParentId(){
		return this.parentId;
	}
	/**设置父权限id
	 * @param parentId
	 */
	public void  setParentId(Integer parentId){
		this.parentId = parentId;
	}
	public List<Permission> getSonPermission() {
		return sonPermission;
	}
	public void setSonPermission(List<Permission> sonPermission) {
		this.sonPermission = sonPermission;
	}
	
}
