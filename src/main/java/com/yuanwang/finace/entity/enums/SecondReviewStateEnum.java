package com.yuanwang.finace.entity.enums;

/**
 * 的枚举类
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * @since 2019-08-14 16:42:26
 */
public enum SecondReviewStateEnum{

    NOTREVIEW(0,"未审核"),
    ISREVIEW(1,"审核中"),
    PASSREVIEW(2,"审核通过"),
    NOTPASSREVIEW(3,"审核未通过"),
    UNKONW(4,"未知");
	
	private int value;
	private String desc;
	
	private SecondReviewStateEnum(int value, String desc){
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
