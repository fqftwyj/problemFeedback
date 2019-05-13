package com.yuanwang.common.enums;

/**使用状态枚举
 * @ClassName: UserStateEnum 
 * @Description: 使用状态枚举
 * @author <crj>
 * @date 2016-8-19 上午10:54:22  
 */
public enum OperatorEnum{
	/**
	 * or
	 */
	OR(0,"or"),
	/**
	 * and
	 */
	AND(1,"and");
	private int value;
	private String desc;
		
	private OperatorEnum(int value, String desc){
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
