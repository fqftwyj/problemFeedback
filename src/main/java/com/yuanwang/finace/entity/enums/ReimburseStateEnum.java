package com.yuanwang.finace.entity.enums;

/**
 * 的枚举类
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * @since 2019-08-14 15:53:34
 */
public enum ReimburseStateEnum{

    NOTSUBMIT(0,"未上报"),
    HASSUBMIT(1,"已上报"),
    RESUBMIT(2,"重新上报");
	
	private int value;
	private String desc;
	
	private ReimburseStateEnum(int value, String desc){
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
