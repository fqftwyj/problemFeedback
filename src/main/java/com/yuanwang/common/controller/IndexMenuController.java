package com.yuanwang.common.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yuanwang.common.core.BaseController;
import com.yuanwang.sys.entity.Permission;
import com.yuanwang.sys.entity.User;
import com.yuanwang.sys.entity.UserRole;

/**首页有菜单跳转
 * @author ywpc41
 *
 */
@Controller
@RequestMapping("indexMenu")
public class IndexMenuController extends BaseController<Integer>{
	
	private List<Permission> firstPer;
	private List<Permission> secondPer;

	/**首页查询
	 * @param map
	 */
	@RequestMapping("")
	public void index(Integer id,Model map,HttpSession session) {
		User user=getLoginUser();
		List<Permission> permissions=user.getPermissions();
		firstPer = new ArrayList<Permission>();//第一级菜单
		secondPer = new ArrayList<Permission>();//第一级下的菜单
		for(Permission p:permissions){//获取第一级菜单
			if(p.getParentId()==1){
				firstPer.add(p);
			}
		}
		Integer firstId=0;//默认不选中
		if(!firstPer.isEmpty()){//有一级菜单 就把第一个id给默认显示
			firstId=firstPer.get(0).getId();
		}
		id=(id==null?firstId:id);
		for(Permission p:permissions){//获取第一级菜单下的菜单
			if(p.getParentId().toString().startsWith(String.valueOf(id))){
				secondPer.add(p);
			}
		}
		map.addAttribute("firstPer",firstPer);
		map.addAttribute("secondPer",secondPer);
		map.addAttribute("id",id);
		map.addAttribute("realName",user.getRealName());
	}
	
}
