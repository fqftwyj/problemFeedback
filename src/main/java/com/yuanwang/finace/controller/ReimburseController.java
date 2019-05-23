package com.yuanwang.finace.controller;

import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuanwang.finace.entity.enums.ReviewStateEnum;
import com.yuanwang.finace.service.ReimburseService;
import com.yuanwang.sys.entity.Office;
import com.yuanwang.sys.entity.User;
import com.yuanwang.sys.service.OfficeService;
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
import com.yuanwang.finace.entity.Reimburse;
import com.yuanwang.finace.entity.enums.ReimburseTypeEnum;
import com.yuanwang.finace.entity.enums.ReimburseStateEnum;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * ReimburseController
 * 
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * 时间：2019-05-16 11:15:07
 */
@Controller
@RequestMapping("/finace/reimburse/")
public class ReimburseController extends BaseController<Reimburse>{

	@Resource
	private ReimburseService reimburseService;
	@Resource
	private OfficeService officeService;
	
	
	/**跳转主页面
	 * @param map 传值对象,通过这个对象给前台传值
	 */
	@RequestMapping(CONSTANT_INDEX)
	public void indexJump(ModelMap map) {

		map.put("reimburseTypeEnum", ReimburseTypeEnum.values());
		map.put("reimburseStateEnum", ReimburseStateEnum.values());
		map.put("reviewStateEnum", ReviewStateEnum.values());
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
	public Result index(Reimburse reimburse, ModelMap map,HttpSession session,Integer page,Integer limit){
		Map<String,Object> search=new HashMap<String,Object>();
		search.put("reimburseType", reimburse.getReimburseType());
		search.put("reimburseState", reimburse.getReimburseState());
		PageInfo<Reimburse> pageinfo = reimburseService.findByPage(search,ProjectDefined.DEFAULT_ORDER_BY,(page==null?1:page),(limit==null?99999:limit));
		return ResultUtil.success("查询成功", (int)pageinfo.getTotal(), pageinfo.getList());
	}
	/**跳转新增页面-第一步选择报销类型
	 * @param map 传值对象,通过这个对象给前台传值
	 */
	@RequestMapping("buildFirst")
	public void createJumpFirst(ModelMap map){
		map.put("reimburseTypeEnums", ReimburseTypeEnum.values());

	}
	/**跳转新增页面第二步
	 * @param map 传值对象,通过这个对象给前台传值
	 */
	@RequestMapping(CONSTANT_BUILD)
	public void createJump(ModelMap map){
		//获取科室列表
		List<Office>  officeList=officeService.findAll();
		map.put("officeList", officeList);
		map.put("reimburseStateEnum", ReimburseStateEnum.values());
	}
	/**新增功能
	 * @param t 新增对象
	 * @return 新增结果
	 * @throws Exception 抛出异常
	 */
	@RequestMapping(CONSTANT_CREATE)
	@ResponseBody
	public Result create(Reimburse reimburse,HttpSession session){
		if(reimburse != null){
			Map<String,Object> map=new HashMap<String,Object>();
			/**
			 * 放入查重字段 map.put("name","测试");
			 */
			User user=(User)session.getAttribute("user");
			reimburse.setStaffCode(user.getUserName());
			reimburse.setUploadName("");
			reimburse.setUploadPath("");
			reimburse.setReimburseState(ReimburseStateEnum.NOTSUBMIT);
			Integer flag=reimburseService.save(reimburse,map,OperatorEnum.AND);
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
		//获取科室列表
		List<Office>  officeList=officeService.findAll();
		map.put("officeList", officeList);
		map.put("reimburseStateEnum", ReimburseStateEnum.values());
		Reimburse result = reimburseService.find(id);
		map.put("result", result);
		String str = "[{\"carboatfeeiItemsData\":[{\"startoffTime\":\"2019-05-01\",\"startoffLocation\":\"杭州\",\"arriveTime\":\"2019-05-05\",\"type\":\"北京\",\"documentsNum\":2,\"carboatfee\":250},{\"startoffTime\":\"2019-05-01\",\"startoffLocation\":\"杭州\",\"arriveTime\":\"2019-05-05\",\"type\":\"北京\",\"documentsNum\":2,\"carboatfee\":250}],\"travelAllowanceItemsData\":[{\"days\":4,\"standard\":25,\"money\":100},{\"days\":4,\"standard\":25,\"money\":200}],\"otherFeeItemsData\":[{\"item\":\"劳务费\",\"documentsNum\":2,\"money\":100},{\"item\":\"劳务费2\",\"documentsNum\":2,\"money\":200}]}]" ;  // 一个未转化的字符串
		JSONArray json =  JSONArray.parseArray(str ); // 首先把字符串转成 JSONArray  对象
		//组装车船费列表
		List<Map<String, String>> carboatfeesList =new ArrayList<Map<String, String>>();
		//组装出差补贴
		List<Map<String, String>> travelAllowanceList =new ArrayList<Map<String, String>>();
		//组装其他费用
		List<Map<String, String>> otherFeeList =new ArrayList<Map<String, String>>();
		if(json.size()>0) {
			JSONArray carboatfeeArray = (JSONArray) json.getJSONObject(0).get("carboatfeeiItemsData");
			JSONArray travelAllowanceArray = (JSONArray) json.getJSONObject(0).get("travelAllowanceItemsData");
			JSONArray otherFeeArray = (JSONArray) json.getJSONObject(0).get("otherFeeItemsData");
			//组装车船费列表
			 carboatfeesList = JSONArray.parseObject(carboatfeeArray.toJSONString(), List.class);
			//组装出差补贴
			travelAllowanceList = JSONArray.parseObject(carboatfeeArray.toJSONString(), List.class);
			//组装其他费用
			otherFeeList = JSONArray.parseObject(carboatfeeArray.toJSONString(), List.class);
		}
		map.put("carboatfeesList", carboatfeesList);
		map.put("travelAllowanceList", travelAllowanceList);
		map.put("otherFeeList", otherFeeList);
	}
	
	/**编辑功能
	 * @param t 编辑对象
	 * @return 编辑结果
	 * @throws Exception 抛出异常
	 */
	@RequestMapping(CONSTANT_UPDATE)
	@ResponseBody
	public Result update(Reimburse reimburse){ 
		if(reimburse != null&&reimburse.getId()!=null){
			Map<String,Object> map=new HashMap<String,Object>();
			/**
			 * 放入查重字段 map.put("name","测试");
			 */
			Integer flag = reimburseService.update(reimburse,map,OperatorEnum.AND);
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
		Reimburse result = reimburseService.find(id);
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
		Integer flag=reimburseService.delete(idsall);
		if(flag==idsall.size()) {
			return ResultUtil.success("删除成功");
		}else {
			return ResultUtil.error("删除失败");
		}
	}
	
	/**导出功能
	 * @param t 查询条件
	 * @param session 会话对象获取当前会话信息
	 * @param request 请求对象
	 * @param response 响应对象
	 * @param page 页大小
	 * @return 导出对象
	 */
	@RequestMapping(CONSTANT_EXPORT)
	@ResponseBody
	public String export(Reimburse reimburse,HttpSession session,
			HttpServletRequest request, HttpServletResponse response,@NotNull Integer page) {
		Map<String,Object> search=new HashMap<String,Object>();
		List<Map<String, Object>> list = reimburseService.excelExportList(search,ProjectDefined.DEFAULT_ORDER_BY,page,9999);

		Map<String, IChange> replaceIoChange = new HashMap<String, IChange>();
		ExcelFacts excel = null;
		String[] cells = new String[] { "中文id"};
		String[] attrs = new String[] { "id"};

		excel = new ExcelFacts.Builder(cells, attrs).sheetName("应用系统")
				.dataList(list).replaceStrategyMap(replaceIoChange).build();

		excel.exportXLS(request, response);
		return null;
	}
	@ResponseBody
	@RequestMapping("reimburseFilepload")
	public String reimburseFilepload(HttpServletRequest request) {

		CommonsMultipartResolver multipartResolver =
				new CommonsMultipartResolver(request.getSession().getServletContext());
		//检查form中是否有enctype="multipart/form-data"
		JSONObject jsonObject = new JSONObject();
		if (multipartResolver.isMultipart(request)) {
			//将request变成多request
			MultipartHttpServletRequest multiRequest =
					(MultipartHttpServletRequest) request;

			//获取multiRequest中所有的文件名
			Iterator iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				//一次遍历所有的文件
				MultipartFile file = multiRequest.getFile(iter.next().toString());

				if (null != file && file.getSize() > 20 * 1024 * 1024) {

					jsonObject.put("msg", "传输文件过大，上传失败！");
				} else if (file != null) {
					String result = null;
					try {
						/*result = vehicleWhitelistService.batchImportVehicleWhitelistByXls(file);*/
						result="yes";
						if ("yes".equals(result)) {
							jsonObject.put("msg", true);
						} else {
							jsonObject.put("msg", result);
						}

					} catch (Exception e) {
						jsonObject.put("msg", result);
					/*	logger.error(result);*/
					}

				}
			}
		}

		return jsonObject.toString();

	}
	/*
	@RequestMapping("callPro")
	@ResponseBody
	public Result callPro() {
		PageInfo<Reimburse> pageinfo = reimburseService.findListByProceAndPage("pro", 1);
		return ResultUtil.success("查询成功", (int)pageinfo.getTotal(), pageinfo.getList());
	}
	*/
}
