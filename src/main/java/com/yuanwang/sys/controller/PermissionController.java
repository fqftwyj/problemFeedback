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
import com.yuanwang.sys.service.PermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
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
import com.yuanwang.sys.entity.Permission;
import com.yuanwang.sys.entity.enums.PermissionTypeEnum;
/**
 * PermissionController
 * 权限表
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * 时间：2018-09-10 10:41:59
 */
@Controller
@RequestMapping("/sys/permission/")
public class PermissionController extends BaseController<Permission>{

	@Resource
	private PermissionService permissionService;
	
	
	/**跳转主页面
	 * @param map 传值对象,通过这个对象给前台传值
	 */
	@RequestMapping(CONSTANT_INDEX)
	public void indexJump(ModelMap map) {
		map.put("permissionTypeEnum", PermissionTypeEnum.values());
	}
	
	/**分页查询
	 * @param  permission 查询条件
	 * @param map 传值对象,通过这个对象给前台传值
	 * @param session 会话对象获取当前会话信息
	 * @param page 当前页
	 * @param limit 页大小
	 * @return 查询结果
	 */
	@RequestMapping(CONSTANT_LIST)
	@ResponseBody
	public Result index(Permission permission,ModelMap map,HttpSession session,Integer page,Integer limit,String parentId){
		Map<String,Object> search=new HashMap<String,Object>();
		search.put("permissionType", permission.getPermissionType());
		search.put("parentId", parentId);
		search.put("permissionName",permission.getPermissionName());
		search.put("permissionCode",permission.getPermissionCode());
		search.put("permissionUrl",permission.getPermissionUrl());
		PageInfo<Permission> pageinfo = permissionService.findByPage(search,"sort asc",(page==null?1:page),(limit==null?99999:limit));
		return ResultUtil.success("查询成功", (int)pageinfo.getTotal(), pageinfo.getList());
	}
	
	/**跳转新增页面
	 * @param map 传值对象,通过这个对象给前台传值
	 */
	@RequestMapping(CONSTANT_BUILD)
	public void createJump(ModelMap map,String parentId){
		map.put("permissionTypeEnum", PermissionTypeEnum.values());
		map.put("parentId",parentId);
	}
	
	/**新增功能
	 * @param permission 新增对象
	 * @return 新增结果
	 * @throws Exception 抛出异常
	 */
	@RequestMapping(CONSTANT_CREATE)
	@ResponseBody
	public Result create(Permission permission){
		if(permission != null){
			Integer flag=permissionService.addPermission(permission);
			/*if(flag==2) {
				return ResultUtil.error("已存在");
			}else */if(flag==1) {
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
		Permission result = permissionService.find(id);
		map.put("result", result);
		map.put("permissionTypeEnum", PermissionTypeEnum.values());
	}
	
	/**编辑功能
	 * @param permission 编辑对象
	 * @return 编辑结果
	 * @throws Exception 抛出异常
	 */
	@RequestMapping(CONSTANT_UPDATE)
	@ResponseBody
	public Result update(Permission permission){ 
		if(permission != null&&permission.getId()!=null){
			Map<String,Object> map=new HashMap<String,Object>();
			/**
			 * 放入查重字段 map.put("name","测试");
			 */
			Integer flag = permissionService.update(permission,map,OperatorEnum.AND);
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
		Permission result = permissionService.find(id);
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
		Integer flag=permissionService.delete(idsall);
		if(flag==idsall.size()) {
			return ResultUtil.success("删除成功");
		}else {
			return ResultUtil.error("删除失败");
		}
	}
	
	/**导出功能
	 * @param permission 查询条件
	 * @param session 会话对象获取当前会话信息
	 * @param request 请求对象
	 * @param response 响应对象
	 * @param page 页大小
	 * @return 导出对象
	 */
	@RequestMapping(CONSTANT_EXPORT)
	@ResponseBody
	public String export(Permission permission,HttpSession session,
			HttpServletRequest request, HttpServletResponse response,@NotNull Integer page) {
		Map<String,Object> search=new HashMap<String,Object>();
		List<Map<String, Object>> list = permissionService.excelExportList(search,ProjectDefined.DEFAULT_ORDER_BY,page,9999);

		Map<String, IChange> replaceIoChange = new HashMap<String, IChange>();
		ExcelFacts excel = null;
		String[] cells = new String[] { "中文id"};
		String[] attrs = new String[] { "id"};

		excel = new ExcelFacts.Builder(cells, attrs).sheetName("应用系统")
				.dataList(list).replaceStrategyMap(replaceIoChange).build();

		excel.exportXLS(request, response);
		return null;
	}
	
	/*
	@RequestMapping("callPro")
	@ResponseBody
	public Result callPro() {
		PageInfo<Permission> pageinfo = permissionService.findListByProceAndPage("pro", 1);
		return ResultUtil.success("查询成功", (int)pageinfo.getTotal(), pageinfo.getList());
	}
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
	
}
