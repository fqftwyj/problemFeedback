package com.yuanwang.common.shrio;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.yuanwang.sys.entity.Config;
import com.yuanwang.sys.service.ConfigService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.yuanwang.common.utils.MD5Util;
import com.yuanwang.sys.entity.Permission;
import com.yuanwang.sys.entity.Role;
import com.yuanwang.sys.entity.User;
import com.yuanwang.sys.entity.enums.StatusEnum;
import com.yuanwang.sys.service.RoleService;
import com.yuanwang.sys.service.UserService;



/**重写shrio的AuthorizingRealm类，主要是两个验证授权的方法
 * @author ywpc41
 *
 */
public class ShiroRealm extends AuthorizingRealm{
	
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
	@Resource
	private ConfigService configService;
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		User user=(User)SecurityUtils.getSubject().getPrincipal();
		
	    List<Role> userRoles = user.getRoles();
	    List<Permission> userPermissions = user.getPermissions();
	    //角色名的集合
	    Set<String> roles = new HashSet<String>();
	    //权限名的集合
	    Set<String> permissions = new HashSet<String>();
	    
	    for(Role role:userRoles){
	    	roles.add(role.getName());
	    }
	    for(Permission permission:userPermissions){
	    	permissions.add(permission.getPermissionCode());
	    }
	    
	    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
	    
	    authorizationInfo.addRoles(roles);
	    authorizationInfo.addStringPermissions(permissions);
	    return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// TODO Auto-generated method stub
		String username = (String) token.getPrincipal();  
		String password=new String((char[])token.getCredentials());
		/**
		  * 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以在此判断或自定义实现  
		  */
		if (username != null && !"".equals(username)) {
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("userName", username);
			User user = userService.find("findUserRolesPermissionByMap",map);
		    if(user==null){
		    	//木有找到用户
		    	throw new UnknownAccountException();
			}
		    //判断账户状态
		    if(user.getStatus()==StatusEnum.DISABLED){
		    	throw new DisabledAccountException();
		    }
		    //判断用户密码 更新密码错误次数
		    try {
				if(!user.getPassword().equals(MD5Util.md5(password))){
					user.setErrorNum(user.getErrorNum()+1);
					map.put("configName","MAX_ERRORPASS_NUM");
					Config config=configService.find(map);
					Integer errorPassNum=Integer.parseInt(config.getConfigValue());
					if(user.getErrorNum()>=errorPassNum){
						user.setStatus(StatusEnum.DISABLED);
					}
					//错误次数超过最大值，禁用账户
					userService.update(user);
				}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        if (user != null) {
	        	return new SimpleAuthenticationInfo(user, user.getPassword(),getName()); 
		    }
	    }
	    return null; 
	}
	
	@Override
	public String getName() {
	    return getClass().getName();
	}
	@Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    /**
     * 清除所有缓存授权信息
     */
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 清除所有缓存授权信息
     */
    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 清除所有缓存授权信息
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
}
