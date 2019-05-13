package com.yuanwang.common.enums;

/**使用状态枚举
 * @ClassName: UserStateEnum 
 * @Description: 使用状态枚举
 * @author <crj>
 * @date 2016-8-19 上午10:54:22  
 */
public enum UserStateEnum{
	/**
	 * 禁用
	 */
	DISABLE(0,"禁用"),
	/**
	 * 启用
	 */
	ENABLE(1,"启用");
	private int value;
	private String desc;
		
	private UserStateEnum(int value, String desc){
		this.value = value;
		this.desc = desc;
	}
		
	public int getValue(){
		return value;
	}
		
	public String getDesc(){
		return desc;
	}
}
