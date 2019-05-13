package com.yuanwang.sys.controller;

import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import com.yuanwang.sys.entity.Permission;
import com.yuanwang.sys.service.PermissionService;
import com.yuanwang.sys.service.RolePermissionService;
import com.yuanwang.sys.service.RoleService;
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
import com.yuanwang.sys.entity.RolePermission;
/**
 * RoleController
 * 角色表
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * 时间：2018-09-04 15:12:54
 */
@Controller
@RequestMapping("/sys/role/")
public class RoleController extends BaseController<Role>{

	@Resource
	private RoleService roleService;
	@Resource
	private PermissionService permissionService;
	@Resource
	private RolePermissionService rolePermissionService;
	
	/**跳转主页面
	 * @param map 传值对象,通过这个对象给前台传值
	 */
	@RequestMapping(CONSTANT_INDEX)
	public void indexJump(ModelMap map) {
		
	}
	
	/**分页查询
	 * @param role  查询条件
	 * @param map 传值对象,通过这个对象给前台传值
	 * @param session 会话对象获取当前会话信息
	 * @param page 当前页
	 * @param limit 页大小
	 * @return 查询结果
	 */
	@RequestMapping(CONSTANT_LIST)
	@ResponseBody
	public Result index(Role role, ModelMap map,HttpSession session,Integer page,Integer limit){
		Map<String,Object> search=new HashMap<String,Object>();
		search.put("name",role.getName());
		search.put("description",role.getDescription());
		PageInfo<Role> pageinfo = roleService.findByPage(search,ProjectDefined.DEFAULT_ORDER_BY,(page==null?1:page),(limit==null?99999:limit));
		return ResultUtil.success("查询成功", (int)pageinfo.getTotal(), pageinfo.getList());
	}
	
	/**跳转新增页面
	 * @param map 传值对象,通过这个对象给前台传值
	 */
	@RequestMapping(CONSTANT_BUILD)
	public void createJump(ModelMap map){
	
	}
	
	/**新增功能
	 * @param role 新增对象
	 * @return 新增结果
	 * @throws Exception 抛出异常
	 */
	@RequestMapping(CONSTANT_CREATE)
	@ResponseBody
	public Result create(Role role){
		if(role != null){
			Map<String,Object> map=new HashMap<String,Object>();
			/**
			 * 放入查重字段 map。put("name","测试");
			 */
			Integer flag=roleService.save(role,map,OperatorEnum.AND);
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
		Role result = roleService.find(id);
		map.put("result", result);
	}
	
	/**编辑功能
	 * @param role 编辑对象
	 * @return 编辑结果
	 * @throws Exception 抛出异常
	 */
	@RequestMapping(CONSTANT_UPDATE)
	@ResponseBody
	public Result update(Role role){ 
		if(role != null&&role.getId()!=null){
			Map<String,Object> map=new HashMap<String,Object>();
			/**
			 * 放入查重字段 map。put("name","测试");
			 */
			Integer flag = roleService.update(role,map,OperatorEnum.AND);
			if(flag==2) {
				return ResultUtil.error("已存在");
			}else if(flag==1) {
				return ResultUtil.success("更新成功");
			}else {
				return ResultUtil.error("更新失败");
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
		Role result = roleService.find(id);
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
		Integer flag=roleService.delete(idsall);
		if(flag==idsall.size()) {
			return ResultUtil.success("删除成功");
		}else {
			return ResultUtil.error("删除失败");
		}
	}
	
	/**导出功能
	 * @param role 查询条件
	 * @param session 会话对象获取当前会话信息
	 * @param request 请求对象
	 * @param response 响应对象
	 * @param page 页大小
	 * @return 导出对象
	 */
	@RequestMapping(CONSTANT_EXPORT)
	@ResponseBody
	public String export(Role role,HttpSession session,
			HttpServletRequest request, HttpServletResponse response,@NotNull Integer page) {
		Map<String,Object> search=new HashMap<String,Object>();
		List<Map<String, Object>> list = roleService.excelExportList(search,ProjectDefined.DEFAULT_ORDER_BY,page,9999);

		Map<String, IChange> replaceIoChange = new HashMap<String, IChange>();
		ExcelFacts excel = null;
		String[] cells = new String[] { "中文id"};
		String[] attrs = new String[] { "id"};

		excel = new ExcelFacts.Builder(cells, attrs).sheetName("应用系统")
				.dataList(list).replaceStrategyMap(replaceIoChange).build();

		excel.exportXLS(request, response);
		return null;
	}
	
	/**跳转权限分配页面
	 * @param id 编辑对象id
	 */
	@RequestMapping("distribute")
	public void distribute(Integer id,Model model){
		Map<String,Object> idMap=new HashMap<String,Object>();
		idMap.put("roleId",id);
		List<RolePermission> roleList = rolePermissionService.findList(idMap);
		model.addAttribute("roleList",roleList);
	}

	/**获取权限分配的树结构
	 * @param parentId  parentId
	 */
	@RequestMapping("queryPermissionTree")
	@ResponseBody
	public List<Map<String,Object>> queryPermissionTree(String parentId){
		Map<String,Object> map=new HashMap<String,Object>();
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		map.put("parentId",parentId);
		List<Permission> permissions=permissionService.findList(map);
		for(Permission p:permissions){
			Map<String,Object> m=new HashMap<String,Object>();
			m.put("id",p.getId());
			m.put("pId",p.getParentId());
			m.put("name",p.getPermissionName());
			m.put("isParent",!p.getSonPermission().isEmpty());
			result.add(m);
		}
		return result;
	}

	/**权限分配功能
	 * @param roleId  roleId
	 * @param privilegeStr  privilegeStrArr
	 */
	@RequestMapping("permissionDistribute")
	@ResponseBody
	public Result permissionDistribute(String roleId,String privilegeStr){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("roleId",roleId);
        List<RolePermission> permissionList = new ArrayList<RolePermission>();
        String[] idss = privilegeStr.split(",");
        for (int i = 0; i < idss.length; i++) {
        	RolePermission rolePermission=new RolePermission();
        	rolePermission.setRoleId(Integer.parseInt(roleId));
        	rolePermission.setPermissionId(Integer.parseInt(idss[i]));
            permissionList.add(rolePermission);
        }
        //先删除原有数据
        rolePermissionService.delete(map);
        //批量插入新的数据
		int flag = rolePermissionService.insertBatch(permissionList);
		if(flag >0) {
			return ResultUtil.success("权限分配成功");
		}else {
			return ResultUtil.error("权限分配失败");
		}
	}
}
