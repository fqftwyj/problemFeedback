package com.yuanwang.finace.controller;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuanwang.common.controller.FileController;
import com.yuanwang.common.utils.ChineseNumber;
import com.yuanwang.common.utils.FremarkerExcel;
import com.yuanwang.common.utils.StringHelper;
import com.yuanwang.common.utils.TestSms;
import com.yuanwang.finace.entity.Review;
import com.yuanwang.finace.entity.enums.ReviewStateEnum;
import com.yuanwang.finace.service.ReimburseService;
import com.yuanwang.finace.service.ReviewService;
import com.yuanwang.sys.entity.Office;
import com.yuanwang.sys.entity.User;
import com.yuanwang.sys.service.OfficeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
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
	private ReviewService reviewService;
	@Resource
	private OfficeService officeService;
	private static String prefixPath;

	@PostConstruct //指定该方法在对象被创建后马上调用 相当于配置文件中的init-method属性
	public void setPrefixPath() throws FileNotFoundException {
	/*	File path = new File(ResourceUtils.getURL("classpath:").getPath());
		File upload = new File(path.getAbsolutePath(), "static/tmpupload/");*/
		//指定文件上传的地址为系统安装的路径
		File upload=new File(ResourceUtils.getURL("classpath:").getPath());
		if(!upload.exists()){
			upload.mkdirs();
		}
		ReimburseController.prefixPath = upload.getAbsolutePath();
	}
	
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
	public Result index(Reimburse reimburse, Review review, ModelMap map, HttpSession session, Integer page, Integer limit){
		Map<String,Object> search=new HashMap<String,Object>();
		search.put("reimburseType", reimburse.getReimburseType());
		search.put("reimburseState", reimburse.getReimburseState());
		search.put("reimburseMembers", reimburse.getReimburseMembers());
		search.put("reviewState", review.getReviewState());
		search.put("sql_keyword_orderBy","updateTime");
		search.put("sql_keyword_sort","desc");
		User user=(User)session.getAttribute("user");
		search.put("staffCode", user.getUserName());
		if(reimburse.getReimburseDate()!=null){
			String reimburseDate=reimburse.getReimburseDate();
			if( reimburseDate.indexOf(" - ")!=-1){
				String[] reimburseDateArr=reimburseDate.split( " - ");
				search.put("reimburseStartDate", reimburseDateArr[0]);
				search.put("reimburseEndDate", reimburseDateArr[1]);
			}
		}

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
	public void createJump(ModelMap map,HttpSession session){
		//获取科室列表
		List<Office>  officeList=officeService.findAll();
		map.put("officeList", officeList);
		map.put("reimburseStateEnum", ReimburseStateEnum.values());
		User user=(User)session.getAttribute("user");
		map.put("userName", user.getUserName());
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
			reimburse.setReimburseDate("1900-1-1");
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
	public void updateJump(Integer id, ModelMap map,HttpSession session){
        User user=(User)session.getAttribute("user");
        map.put("userName", user.getUserName());
		//获取科室列表
		List<Office>  officeList=officeService.findAll();
		map.put("officeList", officeList);
		map.put("reimburseStateEnum", ReimburseStateEnum.values());
		Reimburse result = reimburseService.find(id);
		map.put("result", result);
		JSONArray json =  JSONArray.parseArray(result.getReimburseItems() ); // 首先把字符串转成 JSONArray  对象
		//组装车船费列表
		List<Map<String, String>> carboatfeesList =new ArrayList<Map<String, String>>();
		//组装出差补贴
		List<Map<String, String>> travelAllowanceList =new ArrayList<Map<String, String>>();
		//组装其他费用
		List<Map<String, String>> otherFeeList =new ArrayList<Map<String, String>>();
		//组装合计费用
		JSONObject  totalFeeObj=new JSONObject();
		List<Map<String, String>> totalFeeList =new ArrayList<Map<String, String>>();
		if(json.size()>0) {
			JSONArray carboatfeeArray = (JSONArray) json.getJSONObject(0).get("carboatfeeiItemsData");
			JSONArray travelAllowanceArray = (JSONArray) json.getJSONObject(0).get("travelAllowanceItemsData");
			JSONArray otherFeeArray = (JSONArray) json.getJSONObject(0).get("otherFeeItemsData");
			//组装车船费列表
			 carboatfeesList = JSONArray.parseObject(carboatfeeArray.toJSONString(), List.class);
			//组装出差补贴
			travelAllowanceList = JSONArray.parseObject(travelAllowanceArray.toJSONString(), List.class);
			//组装其他费用
			otherFeeList = JSONArray.parseObject(otherFeeArray.toJSONString(), List.class);
		}
		JSONArray tataljson =  JSONArray.parseArray(result.getReimburseCost() ); // 首先把字符串转成 JSONArray  对象
		if(tataljson.size()>0){
			totalFeeList = JSONArray.parseObject(tataljson.toJSONString(), List.class);
		}
		map.put("carboatfeesList", carboatfeesList);
		map.put("travelAllowanceList", travelAllowanceList);
		map.put("otherFeeList", otherFeeList);
		map.put("totalCarBoatTravel", totalFeeList.get(0).get("totalCarBoatTravel"));
		map.put("totalotherFee", totalFeeList.get(0).get("totalotherFee"));
	}
	
	/**编辑功能
	 * @param t 编辑对象
	 * @return 编辑结果
	 * @throws Exception 抛出异常
	 */
	@RequestMapping(CONSTANT_UPDATE)
	@ResponseBody
	public Result update(Reimburse reimburse,int type){
		//id为null 看到了
		if(reimburse != null && reimburse.getId()!=null){
			Map<String,Object> map=new HashMap<String,Object>();
			/**
			 * 放入查重字段 map.put("name","测试");
			 */
			Integer flag =-1;
			//type 2:修改上报状态  3：伪删除
			if(type==2){
				//修改上报状态
				reimburse = reimburseService.find(reimburse.getId());
				reimburse.setReimburseState(ReimburseStateEnum.HASSUBMIT);
				//获取当前时间 年-月-日
				Date d=new Date();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				reimburse.setReimburseDate(sdf.format(d));

				Review review=new Review();
				Map<String,Object> reviewmap=new HashMap<String,Object>();
				reviewmap.put("reimburseId",reimburse.getId());
				List<Review> reviewList= reviewService.findList(reviewmap);
				if(!reviewList.isEmpty()){
					review=reviewList.get(0);
					review.setReviewState(ReviewStateEnum.NOTREVIEW);
					reviewService.update(review,reviewmap,OperatorEnum.AND);
				}else{
					review.setReimburseId(reimburse.getId());
					review.setReviewState(ReviewStateEnum.NOTREVIEW);
					review.setReviewOpinion("");
					reviewService.save(review,reviewmap,OperatorEnum.AND);
				}
			}else  if(type==3){
				//重新上报的时候的伪删除
				reimburse = reimburseService.find(reimburse.getId());
				reimburse.setDelFlag(1);
			}
			flag = reimburseService.update(reimburse,map,OperatorEnum.AND);
			if(flag==2) {
				return ResultUtil.error("已存在");
			}else if(flag==1) {
				if(type==3){
					return ResultUtil.success("删除成功");
				}else if(type==2){
					//提交上报后，发送待审查短信提醒财务

					//TestSms.sendphoneMain("你有待审查的报销流程，请确认（财务报销系统）","15925638980");
					TestSms.sendphoneMain("你有待审查的报销流程，请确认（财务报销系统）","15988864336");
				}
				return ResultUtil.success("更新成功");
			}else {
				return ResultUtil.error("更新失败");
			}
		}
		return ResultUtil.error("空数据");
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
	/**
	 * 下载报销单
	 * @param t 查询条件
	 * @param session 会话对象获取当前会话信息
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return 导出对象
	 */
	@RequestMapping("exportReimburseDetail")
	@ResponseBody
	public String exportReimburseDetail(Integer id,HttpSession session,
						 HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> reMap =new HashMap<String, Object>();
		Map<String, Object> dataMap =new HashMap<String, Object>();
		//获取报销详情
		Reimburse result = reimburseService.find(id);
		reMap.put("userName",result.getStaffCode());
		dataMap.put("result", result);
		reMap.put("reimburseDate",result.getReimburseDate());
		JSONArray json =  JSONArray.parseArray(result.getReimburseItems() ); // 首先把字符串转成 JSONArray  对象
		//组装车船费列表
		List<Map<String, String>> carboatfeesList =new ArrayList<Map<String, String>>();
		//组装出差补贴
		List<Map<String, String>> travelAllowanceList =new ArrayList<Map<String, String>>();
		//组装其他费用
		List<Map<String, String>> otherFeeList =new ArrayList<Map<String, String>>();
		//组装合计费用
		JSONObject  totalFeeObj=new JSONObject();
		List<Map<String, String>> totalFeeList =new ArrayList<Map<String, String>>();
		if(json.size()>0) {
			JSONArray carboatfeeArray = (JSONArray) json.getJSONObject(0).get("carboatfeeiItemsData");
			JSONArray travelAllowanceArray = (JSONArray) json.getJSONObject(0).get("travelAllowanceItemsData");
			JSONArray otherFeeArray = (JSONArray) json.getJSONObject(0).get("otherFeeItemsData");
			//组装车船费列表
			carboatfeesList = JSONArray.parseObject(carboatfeeArray.toJSONString(), List.class);
			//组装出差补贴
			travelAllowanceList = JSONArray.parseObject(travelAllowanceArray.toJSONString(), List.class);
			//组装其他费用
			otherFeeList = JSONArray.parseObject(otherFeeArray.toJSONString(), List.class);
		}


        //获取最大的size
		Integer carSize=carboatfeesList.size();
		Integer travelSize=travelAllowanceList.size();
		Integer otherSize=otherFeeList.size();
		ArrayList<Integer> sizesList=new ArrayList<Integer>();
		sizesList.add(carSize);
		sizesList.add(travelSize);
		sizesList.add(otherSize);
		int[] sizes = sizesList.stream().mapToInt(Integer::valueOf).toArray();
		int maxSize=StringHelper.getMaxValue(sizes);
		dataMap.put("maxSize",maxSize);
		//填充车船费的空白list
		addBlankMap(carSize,maxSize,carboatfeesList);
		//填充出差补贴的空白list
		addBlankMap(travelSize,maxSize,travelAllowanceList);
		//填充其它费用的的空白list
		addBlankMap(otherSize,maxSize,otherFeeList);


		//组装最终的list,所有的类别合成一个map
		List<Map<String, String>>  collectList=new ArrayList<>();
		for(int i=0;i<maxSize;i++){
			Map<String, String> map=new HashMap<>();
			map.put("departureTime",carboatfeesList.get(i).get("departureTime"));
			map.put("departurePlace",carboatfeesList.get(i).get("departurePlace"));
			map.put("arrivalTime",carboatfeesList.get(i).get("arrivalTime"));
			map.put("arrivalPlace",carboatfeesList.get(i).get("arrivalPlace"));
			map.put("carboatType",carboatfeesList.get(i).get("carboatType"));
			map.put("docNumber",carboatfeesList.get(i).get("docNumber"));
			map.put("carboatfee",carboatfeesList.get(i).get("carboatfee"));
			map.put("days",travelAllowanceList.get(i).get("days"));
			map.put("travelStandard",travelAllowanceList.get(i).get("travelStandard"));
			map.put("travelmoney",travelAllowanceList.get(i).get("travelmoney"));
			map.put("itemname",otherFeeList.get(i).get("itemname"));
			map.put("otherdocnumber",otherFeeList.get(i).get("otherdocnumber"));
			map.put("otherfeemoney",otherFeeList.get(i).get("otherfeemoney"));
			collectList.add(map);
		}
		JSONArray tataljson =  JSONArray.parseArray(result.getReimburseCost()); // 首先把字符串转成 JSONArray  对象
		if(tataljson.size()>0){
			totalFeeList = JSONArray.parseObject(tataljson.toJSONString(), List.class);
		}
		dataMap.put("officeName", result.getOfficeName());
		dataMap.put("reimburseMembers", result.getReimburseMembers());
		dataMap.put("reimburseDate", result.getReimburseDate().equals("1900-01-01")?"":result.getReimburseDate());
		dataMap.put("reimburseReason", result.getReimburseReason());
		dataMap.put("collectList", collectList);
		dataMap.put("totalCarBoatTravel", totalFeeList.get(0).get("totalCarBoatTravel"));
		dataMap.put("totalotherFee", totalFeeList.get(0).get("totalotherFee"));
		Double totalCarBoatTravelDou=Double.parseDouble(totalFeeList.get(0).get("totalCarBoatTravel"));
		Double totalotherFeeDou=Double.parseDouble(totalFeeList.get(0).get("totalotherFee"));
		Double totalFee=totalCarBoatTravelDou+totalotherFeeDou;
		//最终的合计，四舍五入保留两位小数
		dataMap.put("totalFee", (ChineseNumber.getChineseNumber((double)Math.round(totalFee*100)/100)));
		System.out.println(String.valueOf(dataMap.get("totalFee")));
		try {
			FremarkerExcel fexcle=new FremarkerExcel();
			fexcle.createWord(prefixPath+File.separator+"excel","core-office-new.ftl",dataMap, reMap,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 *@author： Fangqun
	 *@description:  不满的填充空白的List
	 *@param:  [size, maxSize, list]
	 *@return: void
	 *@exception:
	 *@date:  上午 9:43 2019/6/10 0010
	 **/
	public void addBlankMap(int size,int maxSize,List<Map<String, String>> list){

		for(int i=size;i<=maxSize;i++){
			Map<String, String> map=new HashMap<>();
			list.add(map);
		}
	}
	/**
	 * 下载报销单粘贴单
	 * @param response 响应对象
	 * @return 导出对象
	 *//*
	@RequestMapping("exportReimburseAttach")
	@ResponseBody
	public String exportReimburseAttach(HttpServletResponse response) {
		try {
			FremarkerExcel fexcle=new FremarkerExcel();
			fexcle.dowloadAttachExcel(prefixPath+File.separator+"excel","日常费用报销原始单据粘贴单.xlsx",response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/


}
