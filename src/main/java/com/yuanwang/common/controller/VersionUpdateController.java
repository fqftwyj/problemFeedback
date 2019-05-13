package com.yuanwang.common.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yuanwang.common.result.Result;
import com.yuanwang.common.result.ResultUtil;

/**版本更新升级
 * @author ywpc
 *
 */
@Controller
@RequestMapping("versionupdate")
public class VersionUpdateController {
	
	@Value("${web.upload-path}")
	private static String prefixPath;
	
	@RequestMapping("update")
	@ResponseBody
	public Result update(HttpServletRequest request,@RequestParam("file") MultipartFile[] files) throws IOException{
		String fileName=null;
		BufferedOutputStream buffStream=null;
		if (files!=null && files.length >0) {
			try {
				String path = request.getSession().getServletContext().getRealPath("/");
				System.out.println(path);
				File catalog = new File(path);
				if (!catalog.exists() && !catalog.isDirectory()) {
					catalog.mkdirs();
	            }
				for(int i=0;i<files.length;i++){
					fileName=files[i].getOriginalFilename();
					byte[] bytes=files[i].getBytes();
					buffStream =new BufferedOutputStream(new FileOutputStream(new File(path+File.separator+fileName)));
					buffStream.write(bytes);
				}
			} catch (Exception e) {
					return ResultUtil.error(e.getMessage());
			} finally{
					buffStream.close();
			}
			return ResultUtil.success("上传成功");
		}else{
			return ResultUtil.error("上传文件为空");
		}
	}
	
	public static void copyFile(File file,String targetPath){
		if(file!=null){
			try {
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
