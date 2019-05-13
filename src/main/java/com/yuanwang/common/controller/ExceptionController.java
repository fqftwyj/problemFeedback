package com.yuanwang.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import com.yuanwang.common.result.ResultUtil;

/**统一异常处理
 * @author crj
 *
 */
@RestControllerAdvice
public class ExceptionController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);
	
	public static final String ERROR_VIEW="error/error";
	
	@org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
	public Object errorHandler(HttpServletRequest request,HttpServletResponse response,Exception e) throws Exception{

		LOGGER.error("统一处理异常抛出：{}",e);
		
		if(isAjax(request)){
			return ResultUtil.error("发生"+e.getMessage()+"异常");
		}else{
			ModelAndView mav=new ModelAndView();
			mav.addObject("exception",e);
			mav.addObject("message",e.getMessage());
			mav.setViewName(ERROR_VIEW);
			return mav;
		}
	}
	
	/**判断是不是ajax请求
	 * @param request 请求
	 * @return 是否
	 */
	public static boolean isAjax(HttpServletRequest request){
		return (request.getHeader("X-Requested-with") != null
					&& "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()) );
	}
}
