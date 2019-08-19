package com.yuanwang.finace.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuanwang.common.utils.TestSms;
import com.yuanwang.finace.entity.Reimburse;
import com.yuanwang.finace.entity.enums.*;
import com.yuanwang.finace.service.ReimburseService;
import com.yuanwang.finace.service.ReviewService;
import com.yuanwang.sys.entity.Office;
import com.yuanwang.sys.entity.User;
import com.yuanwang.sys.service.OfficeService;
import com.yuanwang.sys.service.UserService;
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
import com.yuanwang.finace.entity.Review;

/**
 * ReviewController
 * 
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * 时间：2019-05-16 11:15:07
 */
@Controller
@RequestMapping("/finace/review/")
public class ReviewController extends BaseController<Review>{

	@Resource
	private ReviewService reviewService;

	@Resource
	private ReimburseService reimburseService;
	@Resource
	private OfficeService officeService;
	@Resource
	private UserService userService;

	/**跳转主页面
	 * @param map 传值对象,通过这个对象给前台传值
	 */
	@RequestMapping(CONSTANT_INDEX)
	public void indexJump(ModelMap map,int type,int flag) {
		map.put("reimburseTypeEnum", ReimburseTypeEnum.values());
		map.put("type", type);
		map.put("flag", flag);
        if(type==2){
        	if(flag==1){
				map.put("secondReviewStateEnum", SecondReviewStateEnum.values());
			}else{
				map.put("reviewStateEnum", ReviewStateEnum.values());
			}

        }
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
	public Result index(Review review,Reimburse reimburse,int type,int flag, ModelMap map,HttpSession session,Integer page,Integer limit){
		Map<String,Object> search=new HashMap<String,Object>();
		search.put("reimburseType", reimburse.getReimburseType());
		search.put("staffCode", reimburse.getStaffCode());
		search.put("reviewState", review.getReviewState());
		search.put("reimburseMembers", reimburse.getReimburseMembers());
		search.put("sql_keyword_orderBy","updateTime");
		search.put("sql_keyword_sort","desc");
		if(reimburse.getReimburseDate()!=null){
			String reimburseDate=reimburse.getReimburseDate();
			if( reimburseDate.indexOf(" - ")!=-1){
				String[] reimburseDateArr=reimburseDate.split( " - ");
				search.put("reimburseStartDate", reimburseDateArr[0]);
				search.put("reimburseEndDate", reimburseDateArr[1]);
			}
		}
		search.put("type", type);
        map.put("type",type);
		search.put("flag", flag);
		map.put("flag",flag);
		PageInfo<Review> pageinfo = reviewService.findByPage(search,ProjectDefined.DEFAULT_ORDER_BY,(page==null?1:page),(limit==null?99999:limit));
		return ResultUtil.success("查询成功", (int)pageinfo.getTotal(), pageinfo.getList());
	}
	
	/**跳转新增页面
	 * @param map 传值对象,通过这个对象给前台传值
	 */
	@RequestMapping(CONSTANT_BUILD)
	public void createJump(ModelMap map){
		map.put("reviewStateEnum", ReviewStateEnum.values());
	}
	
	/**新增功能
	 * @param t 新增对象
	 * @return 新增结果
	 * @throws Exception 抛出异常
	 */
	@RequestMapping(CONSTANT_CREATE)
	@ResponseBody
	public Result create(Review review){
		if(review != null){
			Map<String,Object> map=new HashMap<String,Object>();
			/**
			 * 放入查重字段 map.put("name","测试");
			 */
			Integer flag=reviewService.save(review,map,OperatorEnum.AND);
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
	public void updateJump(Integer id,Integer reimburseId,Integer type,Integer flag, ModelMap map,HttpSession session){
		//设置经费来源枚举列表
		map.put("foundSourceEnum", FoundSourceEnum.values());
		//获取科室列表
		List<Office>  officeList=officeService.findAll();
		map.put("officeList", officeList);
		map.put("reimburseStateEnum", ReimburseStateEnum.values());
		Reimburse result = reimburseService.find(reimburseId);
		map.put("result", result);
		map.put("userName", result.getStaffCode());
		map.put("id",id);
		map.put("type",type);
		map.put("flag",flag);
		JSONArray json =  JSONArray.parseArray(result.getReimburseItems() ); // 首先把字符串转成 JSONArray  对象
		//组装车船费列表
		List<Map<String, String>> carboatfeesList =new ArrayList<Map<String, String>>();
		//组装出差补贴
		List<Map<String, String>> travelAllowanceList =new ArrayList<Map<String, String>>();
		//组装其他费用
		List<Map<String, String>> otherFeeList =new ArrayList<Map<String, String>>();
		//组装合计费用
		JSONObject totalFeeObj=new JSONObject();
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
		//查看报销详情时审核状态改为审核中
		Map<String,Object> reviewmap=new HashMap<String,Object>();
		reviewmap.put("reimburseId",reimburseId);
		List<Review> reviewList= reviewService.findList(reviewmap);
		Review review=new Review();
		if(!reviewList.isEmpty() && type==1 ){
			review=reviewList.get(0);
			int foundSource=result.getFoundSource().getValue();
			//审核中按情况分
			//只有财务科审核的情形
			if(foundSource==2 && result.getReimburseState().equals(ReimburseStateEnum.HASSUBMIT) && ReviewStateEnum.NOTREVIEW.equals(review.getReviewState())){
				review.setReviewState(ReviewStateEnum.ISREVIEW);
				//护理科教科第一级审核的情形
			}else if(foundSource!=2 && result.getReimburseState().equals(ReimburseStateEnum.HASSUBMIT) && ReviewStateEnum.NOTREVIEW.equals(review.getReviewState())){
					review.setReviewState(ReviewStateEnum.ISREVIEW);
				//护理科教科第二级审核的情形
			}else if(foundSource!=2  && result.getReimburseState().equals(ReimburseStateEnum.HASSUBMIT) && review.getReviewState().equals(ReviewStateEnum.PASSREVIEW) && SecondReviewStateEnum.NOTREVIEW.equals(review.getReviewState())){
				review.setSecondReviewState(SecondReviewStateEnum.ISREVIEW);
			}
			reviewService.update(review,reviewmap,OperatorEnum.AND);
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
	public Result update(Review review,HttpSession session){
		if(review != null && review.getId()!=null){
			Map<String,Object> map = new HashMap<String,Object>();
			/**
			 * 放入查重字段 map.put("name","测试");
			 */
			//如果审核未通过要修改报销状态为重新上传
			Integer flag0 =0;
			int id=review.getReimburseId();
			Reimburse result = reimburseService.find(id);
			String staffCode=result.getStaffCode();
			Map<String,Object> mapPhone=new HashMap<String,Object>();
			//根据工号获取电话号码
			mapPhone.put("userName",staffCode);
			User user=userService.find(mapPhone);
			String phone=user.getPhone();
			//财务科未通过或者护理，科教未通过都通知申请人已打回
			ReviewStateEnum reviewStateEnum=review.getReviewState();
			SecondReviewStateEnum secondReviewStateEnum=review.getSecondReviewState();
			if(reviewStateEnum.equals(ReviewStateEnum.NOTPASSREVIEW) || (secondReviewStateEnum !=null && secondReviewStateEnum.equals(SecondReviewStateEnum.NOTPASSREVIEW))){
				result.setReimburseState(ReimburseStateEnum.RESUBMIT);
				if((secondReviewStateEnum !=null && secondReviewStateEnum.equals(SecondReviewStateEnum.NOTPASSREVIEW))){
					//二級打回时一级的审核结果和意见复制二级的
					review.setReviewState(ReviewStateEnum.NOTPASSREVIEW);
					review.setReviewOpinion(review.getSecondReviewOpinion());
				}
				flag0=reimburseService.update(result, map,OperatorEnum.AND);
				//重新打回后，发送重新上报的短信提醒报销的账户
				TestSms.sendphoneMain("你有报销流程审核未通过被打回，请确认（财务报销系统）",phone);
			}
			Integer flag = reviewService.update(review, map,OperatorEnum.AND);
			if(flag==2) {
				return ResultUtil.error("已存在");
				//通过或不通过都更新成功
			}else if(flag==1 && (flag0==1|| flag0==0)) {
				//你的报销流程审核已通过，通知报销人员
				if(flag==1 && flag0==0){
					int foundSource=result.getFoundSource().getValue();
					int secondReviewState=review.getSecondReviewState().getValue();
					int reviewState=review.getReviewState().getValue();
					if(foundSource==2 || (foundSource!=2 && secondReviewState==2 )) {
						TestSms.sendphoneMain("你的报销流程审核已通过，请确认（财务报销系统）", phone);
					}else if(foundSource!=2 && reviewState==2 && secondReviewState==4){
						Map<String,Object> mapPhone_cw=new HashMap<String,Object>();
						mapPhone_cw.put("userName",session.getAttribute("CW_REVIEW_STAFFCODE"));
						user=userService.find(mapPhone_cw);
						Map<String,Object> reviewmap=new HashMap<String,Object>();
						reviewmap.put("reimburseId",result.getId());
						review.setSecondReviewState(SecondReviewStateEnum.NOTREVIEW);
						review.setSecondReviewOpinion("");
						reviewService.update(review,reviewmap,OperatorEnum.AND);
						TestSms.sendphoneMain("你有待审核的报销流程(财务)，请确认（财务报销系统）", user.getPhone());
					}

				}
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
		Review result = reviewService.find(id);
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
		Integer flag=reviewService.delete(idsall);
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
	public String export(Review review,HttpSession session,
			HttpServletRequest request, HttpServletResponse response,@NotNull Integer page) {
		Map<String,Object> search=new HashMap<String,Object>();
		List<Map<String, Object>> list = reviewService.excelExportList(search,ProjectDefined.DEFAULT_ORDER_BY,page,9999);

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
		PageInfo<Review> pageinfo = reviewService.findListByProceAndPage("pro", 1);
		return ResultUtil.success("查询成功", (int)pageinfo.getTotal(), pageinfo.getList());
	}
	*/
}
