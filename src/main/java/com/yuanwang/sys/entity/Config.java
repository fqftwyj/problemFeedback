package com.yuanwang.sys.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yuanwang.common.core.BaseEntity;
import com.yuanwang.sys.entity.enums.BuiltInEnum;
/**
 * 系统配置表
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * @since 2019-06-12 10:55:12
 */
public class Config extends BaseEntity{
	private static final long serialVersionUID = 1L;

	//配置名
	private String configName;	
	//配置值
	private String configValue;	
	//配置描述
	private String description;	
	private BuiltInEnum builtIn;
	
	/**获取配置名
	*/
	public String getConfigName(){
		return this.configName;
	}
	/**设置配置名
	 * @param configName
	 */
	public void  setConfigName(String configName){
		this.configName = configName;
	}
	/**获取配置值
	*/
	public String getConfigValue(){
		return this.configValue;
	}
	/**设置配置值
	 * @param configValue
	 */
	public void  setConfigValue(String configValue){
		this.configValue = configValue;
	}
	/**获取配置描述
	*/
	public String getDescription(){
		return this.description;
	}
	/**设置配置描述
	 * @param description
	 */
	public void  setDescription(String description){
		this.description = description;
	}
	/**获取是否内置【enum】(0:否:FALSE,1:是:TRUE)
	*/
	public BuiltInEnum getBuiltIn(){
		return this.builtIn;
	}
	/**设置是否内置【enum】(0:否:FALSE,1:是:TRUE)
	 * @param builtIn
	 */
	public void  setBuiltIn(BuiltInEnum builtIn){
		this.builtIn = builtIn;
	}
	
}
