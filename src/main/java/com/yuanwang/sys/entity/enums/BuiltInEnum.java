package com.yuanwang.sys.entity.enums;

/**
 * 用户表的枚举类
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * @since 2018-12-13 10:53:04
 */
public enum BuiltInEnum{

	FALSE(0,"否"),
    TRUE(1,"是");
	
	private int value;
	private String desc;
	
	private BuiltInEnum(int value, String desc){
		this.value = value;
		this.desc = desc;
	}
	
	public int getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

}
