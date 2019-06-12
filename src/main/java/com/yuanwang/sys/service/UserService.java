package com.yuanwang.sys.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import com.yuanwang.sys.dao.RoleMapper;
import com.yuanwang.sys.dao.UserMapper;
import com.yuanwang.sys.dao.UserRoleMapper;
import com.yuanwang.sys.entity.User;
import com.yuanwang.sys.entity.UserRole;
import com.yuanwang.common.core.BaseService;
import com.yuanwang.common.enums.OperatorEnum;
import com.yuanwang.common.result.ResultUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UserService
 * 用户表
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * 时间：2018-08-07 15:00:08
 */
@Service
public class UserService implements BaseService<User, UserMapper, Integer> {
	
	@Resource
	private UserMapper userMapper;
	@Resource
	private UserRoleMapper userRoleMapper;
	
	@Override
	public UserMapper getEntityDao() {
		return userMapper;
	}

	public Integer save(String roleIdsStr,User user,Map<String,Object> map,OperatorEnum operator){
		Integer flag=save(user,map,operator);
		if(flag==1) {
			String idss[] = roleIdsStr.split(",");
			for(String roleId:idss){
				UserRole userRole=new UserRole();
				userRole.setRoleId(Integer.parseInt(roleId));
				userRole.setUserId(user.getId());
				flag=userRoleMapper.insert(userRole);
			}
		}
		return flag;
	}

	public Integer update(String roleIdsStr,User user,Map<String,Object> map,OperatorEnum operator){
		Integer flag=update(user,map,operator);
		Map<String,Object> idMap=new HashMap<String,Object>();
		idMap.put("userId",user.getId());
		userRoleMapper.delete(idMap);
		if(flag==1) {
			String idss[] = roleIdsStr.split(",");
			for(String roleId:idss){
				UserRole userRole=new UserRole();
				userRole.setRoleId(Integer.parseInt(roleId));
				userRole.setUserId(user.getId());
				flag=userRoleMapper.insert(userRole);
			}
		}
		return flag;
	}


	
	@Override
	public Integer delete(List<Integer> ids) {
		Integer flag= userMapper.delete(ids);
		if(flag>0){
			Map<String,Object> map=new HashMap<String,Object>();
			for(Integer userId:ids){
				map.put("userId",userId);
				userRoleMapper.delete(map);
			}
		}
		return flag;
	}
	/*public RetKit updataPwd(Integer id, String oldP, String newP,String conP) {
		Optional<Account> user = accountRepository.findById(id);
		Account account = user.get();
		if (user!=null) {
			if (!HashKit.md5(oldP).equals(user.get().getPassword())) {
				return RetKit.fail("旧密码错误");
			}
			if (!newP.equals(conP)) {
				return RetKit.fail("新密码与确认密码不一致");
			}
			if(HashKit.md5(newP).equals(user.get().getPassword())) {
				return RetKit.fail("新密码与旧密码相同，请重新输入");
			}
			account.setPassword(HashKit.md5(newP));
			try {
				accountRepository.save(account);
			} catch (Exception e) {
				return RetKit.fail("网络异常");
			}

			return RetKit.ok("修改成功");

		}
		return RetKit.fail("用户登陆失效");
	}*/

}
