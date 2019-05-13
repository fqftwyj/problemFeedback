package com.yuanwang.common.shrio.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.yuanwang.common.shrio.ShiroRealm;

/**
 * Shiro 配置
 * @author yincl
 * @date 2016年9月2日
 */
@Configuration 
public class ShiroConfiguration { 
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ShiroConfiguration.class);
	
	@Bean
	public EhCacheManager ehCacheManager() {
		EhCacheManager em = new EhCacheManager(); 
		em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml"); 
		return em;  
	} 
	
	/**
	 * 注册DelegatingFilterProxy（Shiro）
	 * @return 返回
	 */
	@Bean 
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean(); 
		filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter")); 
		// 该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理 
		filterRegistration.addInitParameter("targetFilterLifecycle", "true"); 
		filterRegistration.setEnabled(true); 
		filterRegistration.addUrlPatterns("/*"); 
		return filterRegistration; 
	} 
	
	@Bean(name = "lifecycleBeanPostProcessor") 
	public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() { 
		return new LifecycleBeanPostProcessor(); 
	} 
	
	@Bean 
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator(); 
		daap.setProxyTargetClass(true); 
		return daap; 
	} 

	@Bean(name = "hashedCredentialsMatcher")
	public HashedCredentialsMatcher getHashedCredentialsMatcher() {
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //指定加密方式为MD5
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
	}
	
	/**定义shiroRealm对象
	 * @param matcher 钥匙匹配器
	 * @return 返回shiroRealm对象
	 */
	@Bean(name = "shiroRealm") 
	public ShiroRealm getShiroRealm(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher matcher) {
		ShiroRealm shiroRealm = new ShiroRealm();
		shiroRealm.setCredentialsMatcher(matcher);
		return shiroRealm;
	}
	
	@Bean
	public DefaultWebSessionManager sessionManager(){
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		//设置会话时间 半小时
		sessionManager.setGlobalSessionTimeout(1800000);
		return sessionManager;
	}
	
	/**定义securityManager管理器
	 * @param myShiroRealm 自定义shiroRealm对象
	 * @return 返回
	 */
	@Bean(name = "securityManager") 
	public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("shiroRealm") ShiroRealm myShiroRealm) { 
		DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager(); 
		dwsm.setRealm(myShiroRealm); 
		// <!-- 用户授权/认证信息Cache, 采用EhCache 缓存 --> 
		dwsm.setCacheManager(ehCacheManager()); 
		dwsm.setRememberMeManager(rememberMeManager());
		dwsm.setSessionManager(sessionManager());
		return dwsm; 
	} 
	
	/**定义bean
	 * @param myShiroRealm 自定义myShiroRealm对象
	 * @return 返回
	 */
	@Bean 
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(@Qualifier("shiroRealm") ShiroRealm myShiroRealm) {
		AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor(); 
		aasa.setSecurityManager(getDefaultWebSecurityManager(myShiroRealm)); 
		return new AuthorizationAttributeSourceAdvisor(); 
	} 
	 
	/**加载shiroFilter权限控制规则（从数据库读取然后配置)
	 * @param myShiroRealm 自定义的shiroRealm bean类
	 * @return 返回
	 */
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("shiroRealm") ShiroRealm myShiroRealm) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(getDefaultWebSecurityManager(myShiroRealm));
		shiroFilterFactoryBean.setLoginUrl("/login/login");
		shiroFilterFactoryBean.setSuccessUrl("/index");
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>(); 
		filterChainDefinitionMap.put("/static/**", "anon");
		filterChainDefinitionMap.put("/login/**", "anon");
		filterChainDefinitionMap.put("/**/nologin/**", "anon");
		filterChainDefinitionMap.put("/**", "user");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}
	
	

	
	
	/**定义一个cookie对象
	 * @return 返回
	 */
	@Bean  
	public SimpleCookie rememberMeCookie(){  
		//这个参数是cookie的名称，对应前端的checkbox的name = rememberMe  
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe"); 
		//防止跨站点脚本攻击
		simpleCookie.setHttpOnly(true);
		//<!-- 记住我cookie生效时间30天 ,单位秒;-->  
		simpleCookie.setMaxAge(259200);  
		return simpleCookie;  
	}  
		
	/**  
	* cookie管理对象;  
    * @return 返回
	*/  
	@Bean  
	public CookieRememberMeManager rememberMeManager(){  
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();  
		cookieRememberMeManager.setCookie(rememberMeCookie());  
		cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
		return cookieRememberMeManager;  
	} 
}
