package com.yuanwang.common.controller;
import java.io.*;
import java.net.URLEncoder;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yuanwang.common.result.Result;
import com.yuanwang.common.result.ResultUtil;

/**文件上传下载接口
 * @author ywpc
 *
 */
@Controller
@RequestMapping("upload")
public class FileController {
	
	private static String prefixPath;

	@PostConstruct //指定该方法在对象被创建后马上调用 相当于配置文件中的init-method属性
	public void setPrefixPath() throws FileNotFoundException {
	/*	File path = new File(ResourceUtils.getURL("classpath:").getPath());
		File upload = new File(path.getAbsolutePath(), "static/tmpupload/");*/
	  //指定文件上传的地址为系统安装的路径
		File upload=new File(ResourceUtils.getURL("/upload/static").getPath());
		if(!upload.exists()){
			upload.mkdirs();
		}
		FileController.prefixPath = upload.getAbsolutePath();
	}

	/**文件上传
	 * @param request 请求
	 * @param files 上传文件
	 * @param module 所属模块
	 * @return 文件相对路径
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping("fileUpload")
	@ResponseBody
	public Result fileUpload(HttpServletRequest request,@RequestParam("file") MultipartFile[] files,String module) throws IllegalStateException, IOException {
		String filePath = "";
		String fileName=null;
		String fileNames="";
		BufferedOutputStream buffStream=null;
		if(StringUtils.isBlank(module)){
			return ResultUtil.error("参数缺失图片所属模块参数");
		}
		long startTime = System.currentTimeMillis();
		if (files!=null && files.length >0) {
			try {
//				String path = request.getSession().getServletContext().getRealPath("/static/upload")+File.separator+module;
				String path = prefixPath+File.separator+module;
				File catalog = new File(path);
				if (!catalog.exists() && !catalog.isDirectory()) {
					catalog.mkdirs();
	            }
				for(int i=0;i<files.length;i++){
					fileName=files[i].getOriginalFilename();
					fileNames=","+files[i].getOriginalFilename();
					byte[] bytes=files[i].getBytes();
					buffStream =new BufferedOutputStream(new FileOutputStream(new File(path+File.separator+startTime+fileName)));
					buffStream.write(bytes);
				/*	filePath=",/static"+File.separator+module+File.separator+startTime+fileName+filePath;*/
					filePath=","+startTime+fileName+filePath;
				}
			} catch (Exception e) {
					return ResultUtil.error(e.getMessage());
			} finally{
					buffStream.close();
			}
			return ResultUtil.success("上传成功",filePath.substring(1)+";"+fileNames.substring(1));
		}else{
			return ResultUtil.error("上传文件为空");
		}
		
		
	}
	
	/**
	 * 下载文件
	 * @param path
	 * @param filename
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("fileDownload")
	@ResponseBody
	public static void fileDownload(String module, String fileName,String filePath,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	    //声明本次下载状态的记录对象
	    //设置响应头和客户端保存文件名
		String path=prefixPath+File.separator+module;
	    response.setContentType("multipart/form-data;charset=utf-8");
		String downloadFileName = URLEncoder.encode(fileName,"UTF-8");
		// 设置响应头，控制浏览器下载该文件
		response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName);


	    //用于记录以完成的下载的数据量，单位是byte
	    try {
	        //打开本地文件流
	        InputStream inputStream = new FileInputStream(path+File.separator+filePath);
	        //激活下载操作
	        OutputStream os = response.getOutputStream();
	        //循环写入输出流
	        byte[] b = new byte[2048];
	        int length;
	        while ((length = inputStream.read(b)) > 0) {
	            os.write(b, 0, length);
	        }
	        // 这里主要关闭。
	        os.close();
	        inputStream.close();
	    } catch (Exception e){
	        throw e;
	    }
	}
}
