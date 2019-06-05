package com.yuanwang.common.controller;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuanwang.common.result.Result;
import com.yuanwang.common.result.ResultUtil;
import com.yuanwang.sys.entity.User;

/**登录登出
 * @author ywpc41
 *
 */
@Controller
@RequestMapping("login")
public class LoginController {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	@RequestMapping("login")
	public void login() {
		
	}
	
	
	@RequestMapping("dologin")
	@ResponseBody
	public Result dologin(String name,String password,HttpSession session){
		try {
			UsernamePasswordToken token = new UsernamePasswordToken(name, password.toUpperCase(),true); 
			Subject currentUser = SecurityUtils.getSubject();
			try{
				currentUser.login(token);
			}catch(AuthenticationException a){
			}
		/*	currentUser.login(token);//验证角色和权限   */
			User user=(User) currentUser.getPrincipal();
			session.setAttribute("user", user);
			return ResultUtil.success("登录成功");
		}catch(UnknownAccountException e){
			return ResultUtil.error("没有该用户");
		}catch(IncorrectCredentialsException e){
			return ResultUtil.error("密码错误");
		}catch(DisabledAccountException e){
			return ResultUtil.error("该账号不可用");
		}catch (Exception e) {
			LOGGER.error("login error:", e);
			return ResultUtil.error("登录异常，请联系管理员");
		}
		
	}
	
	@RequestMapping("logOut")
	public String logOut(HttpSession session) {
	    Subject subject = SecurityUtils.getSubject();
	    subject.logout();
	    return "login/login";
	}
}
