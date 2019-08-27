package com.yuanwang.sys.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import com.yuanwang.common.utils.MD5Util;
import com.yuanwang.sys.entity.Office;
import com.yuanwang.sys.entity.UserRole;
import com.yuanwang.sys.service.OfficeService;
import com.yuanwang.sys.service.RoleService;
import com.yuanwang.sys.service.UserRoleService;
import com.yuanwang.sys.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.ModelMap;
import com.github.pagehelper.PageInfo;
import com.yuanwang.common.config.ProjectDefined;
import com.yuanwang.common.core.BaseController;
import com.yuanwang.common.enums.OperatorEnum;
import com.yuanwang.common.result.Result;
import com.yuanwang.common.result.ResultUtil;
import com.yuanwang.common.utils.excelexport.IChange;
import com.yuanwang.common.utils.excelexport.ExcelFacts;
import com.yuanwang.sys.entity.Role;
import com.yuanwang.sys.entity.User;
import com.yuanwang.sys.entity.enums.BuiltInEnum;
import com.yuanwang.sys.entity.enums.StatusEnum;
/**
 * UserController
 * 用户表
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * 时间：2018-09-04 15:12:54
 */
@Controller
@RequestMapping("/sys/user/")
public class UserController extends BaseController<User>{

	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
	@Resource
	private OfficeService officeService;
	@Resource
	private UserRoleService userRoleService;
	
	/**跳转主页面
	 * @param map 传值对象,通过这个对象给前台传值
	 */
	@RequestMapping(CONSTANT_INDEX)
	public void indexJump(ModelMap map) {
		
	}
	
	/**分页查询
	 * @param t 查询条件
	 * @param map 传值对象,通过这个对象给前台传值
	 * @param session 会话对象获取当前会话信息
	 * @param page 当前页
	 * @param limit 页大小
	 * @return 查询结果
	 */
	@RequestMapping(CONSTANT_LIST)
	@ResponseBody
	public Result index(User user, ModelMap map,HttpSession session,Integer page,Integer limit){
		Map<String,Object> search=new HashMap<String,Object>();
		search.put("userName",user.getUserName());
		search.put("realName",user.getRealName());
		search.put("officeName",user.getOfficeName());
		search.put("phone",user.getPhone());
		PageInfo<User> pageinfo = userService.findByPage(search,ProjectDefined.DEFAULT_ORDER_BY,(page==null?1:page),(limit==null?99999:limit));
		return ResultUtil.success("查询成功", (int)pageinfo.getTotal(), pageinfo.getList());
	}
	
	/**跳转新增页面
	 * @param map 传值对象,通过这个对象给前台传值
	 */
	@RequestMapping(CONSTANT_BUILD)
	public void createJump(ModelMap map){
		List<Role> roles=roleService.findAll();
		map.put("roles",roles);
		//获取科室列表
		List<Office>  officeList=officeService.findAll();
		map.put("officeList", officeList);

	}
	
	/**新增功能
	 * @param user 新增对象
	 * @return 新增结果
	 * @throws Exception 抛出异常
	 */
	@RequestMapping(CONSTANT_CREATE)
	@ResponseBody
	public Result create(User user,String roleIdsStr) {
		if(user != null){
			Map<String,Object> map=new HashMap<String,Object>();
			try {
				user.setPassword(MD5Util.md5(MD5Util.md5(user.getPassword())));
			}catch (Exception e){
				e.printStackTrace();
			}
			user.setErrorNum(0);
			user.setStatus(StatusEnum.ENABLED);
			user.setBuiltIn(BuiltInEnum.FALSE);
			Integer flag=userService.save(roleIdsStr,user,map,OperatorEnum.AND);
			if(flag==2) {
				return ResultUtil.error("已存在");
			}else if(flag==1) {
				return ResultUtil.success("新增成功");
			}else {
				return ResultUtil.error("新增失败");
			}
		}
		return ResultUtil.error("空数据");
	}
	
	/**跳转编辑页面
	 * @param id 编辑对象id
	 * @param map 传值对象,通过这个对象给前台传值
	 */
	@RequestMapping(CONSTANT_EDIT)
	public void updateJump(Integer id, ModelMap map){
		User result = userService.find(id);
		map.put("result", result);
		List<Role> roles=roleService.findAll();
		map.put("roles",roles);
		//获取科室列表
		List<Office>  officeList=officeService.findAll();
		map.put("officeList", officeList);
	}
	
	/**编辑功能
	 * @param user 编辑对象
	 * @return 编辑结果
	 * @throws Exception 抛出异常
	 */
	@RequestMapping(CONSTANT_UPDATE)
	@ResponseBody
	public Result update(User user,String roleIdsStr){
		if(user != null&&user.getId()!=null){
			Map<String,Object> map=new HashMap<String,Object>();
			try {
				user.setPassword(MD5Util.md5(MD5Util.md5(user.getPassword())));
			}catch (Exception e){
				e.printStackTrace();
			}
			//找出该用户所有的角色删除（根据用户ID删除用户角色），再编辑
			Map<String,Object>  delMap=new HashMap<String,Object>();
			delMap.put("userId",user.getId());
			Integer flag=userRoleService.delete(delMap);
			Integer flag2 =-1;
			if(flag==0 || flag>0){
				//更新的时候往user_role里插入数据
				flag2 = userService.update(roleIdsStr,user,map,OperatorEnum.AND);
				if(flag2==2) {
					return ResultUtil.error("已存在");
				}else if(flag2==1) {
					return ResultUtil.success("更新成功");
				}else {
					return ResultUtil.error("更新失败");
				}
			}
		}
		return ResultUtil.error("空数据");
	}
	
	/**查看功能
	 * @param id 主键id
	 * @return 结果
	 * @throws Exception 抛出异常
	 */
	@RequestMapping(CONSTANT_SHOW)
	@ResponseBody
	public Result show(Integer id){
		if(id==null){
			return ResultUtil.error("id不能为空");
		}
		User result = userService.find(id);
		if(result!=null) {
			return ResultUtil.success("查询成功",result);
		}else {
			return ResultUtil.error("没有该数据");
		}
	}
	
	/**删除功能
	 * @param ids 主键id字符串，以逗号相连
	 * @return 删除结果
	 * @throws Exception 抛出异常
	 */
	@RequestMapping(CONSTANT_DELETE)
	@ResponseBody
	public Result delete(String ids){
		if(StringUtils.isBlank(ids)) {
			return ResultUtil.error("id不能为空");
		}
		List<Integer> idsall=new ArrayList<Integer>();
		String[] keys=ids.split(",");
		if(keys != null && keys.length > 0){
			for(String id :keys){
				idsall.add(Integer.parseInt(id));
			}
		}
		Integer flag=userService.delete(idsall);
		if(flag>0) {
			return ResultUtil.success("删除成功");
		}else {
			return ResultUtil.error("删除失败");
		}
	}
	
	/**导出功能
	 * @param user 查询条件
	 * @param session 会话对象获取当前会话信息
	 * @param request 请求对象
	 * @param response 响应对象
	 * @param page 页大小
	 * @return 导出对象
	 */
	@RequestMapping(CONSTANT_EXPORT)
	@ResponseBody
	public String export(User user,HttpSession session,
			HttpServletRequest request, HttpServletResponse response,@NotNull Integer page) {
		Map<String,Object> search=new HashMap<String,Object>();
		List<Map<String, Object>> list = userService.excelExportList(search,ProjectDefined.DEFAULT_ORDER_BY,page,9999);

		Map<String, IChange> replaceIoChange = new HashMap<String, IChange>();
		ExcelFacts excel = null;
		String[] cells = new String[] { "中文id"};
		String[] attrs = new String[] { "id"};

		excel = new ExcelFacts.Builder(cells, attrs).sheetName("应用系统")
				.dataList(list).replaceStrategyMap(replaceIoChange).build();

		excel.exportXLS(request, response);
		return null;
	}

	/**重置密码功能
	 * @param id 主键id
	 */
	@RequestMapping("resetPassword")
	@ResponseBody
	public Result resetPassword(Integer id){
		Map<String,Object> map = new HashMap<String,Object>();
		Integer flag = 0;
		try{
			User user=new User();
			user.setId(id);
			user.setPassword(MD5Util.md5(MD5Util.md5("123456")));
			flag=userService.update(user);
		}catch (Exception e){
			e.printStackTrace();
		}
		if(flag>0) {
			return ResultUtil.success("重置成功");
		}else {
			return ResultUtil.error("重置失败");
		}
	}

	/**跳转角色分配页面
	 * @param id 编辑对象id
	 */
	@RequestMapping("distribute")
	public void distribute(Integer id,ModelMap map){
		map.put("userId",id);
	}
	
	@RequestMapping("updateStatus")
	@ResponseBody
	public Result updateStatus(Integer userId,String status){
		User user=new User();
		user.setId(userId);
		user.setStatus(StatusEnum.valueOf(status));
		Integer flag=userService.update(user);
		if(flag==1){
			return ResultUtil.success();
		}else{
			return ResultUtil.error();
		}
	}
	/**
	 * 去修改密码页面
	 * @param
	 */
	@RequestMapping("toEditPassWord")
	public void toEditPassWord() {

	}

	/**
	 *修改密码
	 * @param  session
	 */
	@RequestMapping("editPassWord")
	@ResponseBody
	public Result editPassWord(String oldPassword,String newPassword,HttpSession session) {
		User user = (User) session.getAttribute("user");
		Integer flag = 0;
		try {
			oldPassword = MD5Util.md5(MD5Util.md5(oldPassword));
			if (user.getPassword().equals(oldPassword)) {
				user.setPassword(MD5Util.md5(MD5Util.md5(newPassword)));
				flag = userService.update(user);
			} else {
				return ResultUtil.error("旧密码输入有误");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (flag == 1) {
			return ResultUtil.success();
		} else {
			return ResultUtil.error();
		}
	}


}
