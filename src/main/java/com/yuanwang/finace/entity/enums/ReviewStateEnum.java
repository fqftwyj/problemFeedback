package com.yuanwang.finace.entity.enums;

/**
 * 的枚举类
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * @since 2019-05-24 16:33:50
 */
public enum ReviewStateEnum{

    NOTREVIEW(0,"未审查"),
    ISREVIEW(1,"审查中"),
    PASSREVIEW(2,"审查通过"),
    NOTPASSREVIEW(3,"审查不通过");
	
	private int value;
	private String desc;
	
	private ReviewStateEnum(int value, String desc){
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
