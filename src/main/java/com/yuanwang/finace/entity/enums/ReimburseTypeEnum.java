package com.yuanwang.finace.entity.enums;

/**
 * 的枚举类
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * @since 2019-08-14 15:53:34
 */
public enum ReimburseTypeEnum{

    OUTTRAIN(0,"外出培训");
	
	private int value;
	private String desc;
	
	private ReimburseTypeEnum(int value, String desc){
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
