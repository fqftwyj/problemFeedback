package com.yuanwang.finace.controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuanwang.finace.entity.Review;
import com.yuanwang.finace.entity.enums.ReviewStateEnum;
import com.yuanwang.finace.service.ReimburseService;
import com.yuanwang.sys.entity.Office;
import com.yuanwang.sys.entity.User;
import com.yuanwang.sys.service.OfficeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
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
	public Result index(Reimburse reimburse, Review review, ModelMap map, HttpSession session, Integer page, Integer limit){
		Map<String,Object> search=new HashMap<String,Object>();
		search.put("reimburseType", reimburse.getReimburseType());
		search.put("reimburseState", reimburse.getReimburseState());
		search.put("reimburseMembers", reimburse.getReimburseMembers());
	/*	search.put("reviewState", review.getReviewState());*/
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
	public void updateJump(Integer id, ModelMap map){
		//获取科室列表
		List<Office>  officeList=officeService.findAll();
		map.put("officeList", officeList);
		map.put("reimburseStateEnum", ReimburseStateEnum.values());
		Reimburse result = reimburseService.find(id);
		map.put("result", result);
		/*String str = "[{\"carboatfeeiItemsData\":[{\"startoffTime\":\"2019-05-01\",\"startoffLocation\":\"杭州\",\"arriveTime\":\"2019-05-05\",\"type\":\"北京\",\"documentsNum\":2,\"carboatfee\":250},{\"startoffTime\":\"2019-05-01\",\"startoffLocation\":\"杭州\",\"arriveTime\":\"2019-05-05\",\"type\":\"北京\",\"documentsNum\":2,\"carboatfee\":250}],\"travelAllowanceItemsData\":[{\"days\":4,\"standard\":25,\"money\":100},{\"days\":4,\"standard\":25,\"money\":200}],\"otherFeeItemsData\":[{\"item\":\"劳务费\",\"documentsNum\":2,\"money\":100},{\"item\":\"劳务费2\",\"documentsNum\":2,\"money\":200}]}]" ;  // 一个未转化的字符串
	*/
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
			totalFeeObj=(JSONObject) tataljson.get(0);
		}
		map.put("carboatfeesList", carboatfeesList);
		map.put("travelAllowanceList", travelAllowanceList);
		map.put("otherFeeList", otherFeeList);
		map.put("totalFeeObj", totalFeeObj);
	}
	
	/**编辑功能
	 * @param t 编辑对象
	 * @return 编辑结果
	 * @throws Exception 抛出异常
	 */
	@RequestMapping(CONSTANT_UPDATE)
	@ResponseBody
	public Result update(Reimburse reimburse,int type){
		if(reimburse != null&&reimburse.getId()!=null){
			Map<String,Object> map=new HashMap<String,Object>();
			/**
			 * 放入查重字段 map.put("name","测试");
			 */
			if(type==1){
				reimburse.setUploadName("");
				reimburse.setUploadPath("");
				reimburse.setReimburseState(ReimburseStateEnum.NOTSUBMIT);
			}else{//修改上报状态
				reimburse = reimburseService.find(reimburse.getId());
				reimburse.setReimburseState(ReimburseStateEnum.HASSUBMIT);
				//获取当前时间 年-月-日
				Date d=new Date();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				reimburse.setReimburseDate(sdf.format(d));
			}
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


    //上传文件2
	/*public String uploadImg(MultipartFile file) {
		if (null != file) {
			String myFileName = file.getOriginalFilename();// 文件原名称
			String fileName = BasePath.getImgPath("yyyyMMddHHmmss")+
					Integer.toHexString(new Random().nextInt()) +"."+ myFileName.
					substring(myFileName.lastIndexOf(".") + 1);
			String pat=FileProperties.getFilePath()+"/src/main/webapp/";//获取文件保存路径
			String sqlPath="static/images/upload/storeHead/"+BasePath.getImgPath("yyyyMMdd")+"/";

			File fileDir=new File(pat+sqlPath);
			if (!fileDir.exists()) { //如果不存在 则创建
				fileDir.mkdirs();
			}
			String path=pat+sqlPath+fileName;
			File localFile = new File(path);
			try {
				file.transferTo(localFile);
				return sqlPath+fileName;
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.println("文件为空");
		}
		return null;
	}*/

	//下载

	@RequestMapping("download")
	public String downLoad(HttpServletResponse response){
		String filename="2.jpg";
		String filePath = "F:/test" ;
		File file = new File(filePath + "/" + filename);
		if(file.exists()){ //判断文件父目录是否存在
			response.setContentType("application/force-download");
			response.setHeader("Content-Disposition", "attachment;fileName=" + filename);

			byte[] buffer = new byte[1024];
			FileInputStream fis = null; //文件输入流
			BufferedInputStream bis = null;

			OutputStream os = null; //输出流
			try {
				os = response.getOutputStream();
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				int i = bis.read(buffer);
				while(i != -1){
					os.write(buffer);
					i = bis.read(buffer);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("----------file download" + filename);
			try {
				bis.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
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
