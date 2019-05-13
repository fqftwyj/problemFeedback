package com.yuanwang.sys.entity.enums;

/**
 * 用户表的枚举类
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * @since 2018-12-12 13:17:11
 */
public enum StatusEnum{

    DISABLED(0,"禁用"),
    ENABLED(1,"启用");
	
	private int value;
	private String desc;
	
	private StatusEnum(int value, String desc){
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
