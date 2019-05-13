package com.yuanwang.common.core;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.yuanwang.sys.entity.User;


/**所有controller类的父类
 * @author crj
 *
 * @param <T> 实体对象
 */
public class BaseController<T>{
	
	public static final String CONSTANT_INDEX="index";
	public static final String CONSTANT_LIST="list";
	public static final String CONSTANT_BUILD="build";
	public static final String CONSTANT_CREATE="create";
	public static final String CONSTANT_EDIT="edit";
	public static final String CONSTANT_UPDATE="update";
	public static final String CONSTANT_DELETE="delete";
	public static final String CONSTANT_SHOW="show";
	public static final String CONSTANT_EXPORT="export";
	
	/**获取登录用户
	 * @return
	 */
	public User getLoginUser(){
		Subject currentUser = SecurityUtils.getSubject();
		User user=(User) currentUser.getPrincipal();
		return user;
	}
	
	
}
