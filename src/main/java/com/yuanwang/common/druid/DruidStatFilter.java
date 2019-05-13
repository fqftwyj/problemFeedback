package com.yuanwang.common.druid;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
 
import com.alibaba.druid.support.http.WebStatFilter;
 
/**配置druid过滤器
 * @author ywpc41
 *
 */
@WebFilter(filterName="druidStatFilter",urlPatterns="/*",
initParams={
        @WebInitParam(name="exclusions",value="*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*"),// 忽略资源
        @WebInitParam(name="profileEnable",value="true"),// 忽略资源
        @WebInitParam(name="principalCookieName",value="USER_COOKIE"),// 忽略资源
        @WebInitParam(name="principalSessionName",value="USER_SESSION"),// 忽略资源
})
public class DruidStatFilter extends WebStatFilter {
 
}