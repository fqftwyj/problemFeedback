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

/**首页无菜单跳转
 * @author ywpc41
 *
 */
@Controller
@RequestMapping("index")
public class IndexController extends BaseController<Integer>{
	
	private List<Permission> firstPer;
	private List<Permission> secondPer;

	/**首页查询
	 * @param map
	 */
	@RequestMapping("")
	public void index(Integer id,Model map,HttpSession session) {
		id=id==null?101:id;
		User user=getLoginUser();
		List<Permission> permissions=user.getPermissions();
		firstPer = new ArrayList<Permission>();
		for(Permission p:permissions){
			if(p.getParentId()==1){
				firstPer.add(p);
			}
		}
		map.addAttribute("firstPer",firstPer);
		map.addAttribute("id",id);
	}
	
	@RequestMapping("detail")
	public void detail(){
		
	}
	
	@RequestMapping("blank")
	public void blank(){
		
	}
}
