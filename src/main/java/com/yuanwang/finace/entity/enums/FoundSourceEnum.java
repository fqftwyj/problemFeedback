package com.yuanwang.finace.entity.enums;

/**
 * 的枚举类
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * @since 2019-08-14 15:53:34
 */
public enum FoundSourceEnum{

    CLINICALMEDICINE(0,"医院1（临床、医技科室）"),
    NURSE(1,"医院2（护理）"),
    ADMINLOGISTIC(2,"医院3（行政后勤）"),
      KEYDISCIPLINES(3,"重点学科专项"),
      RESEARCHTOPICS(4,"科研课题专项"),
    TALENTTRAIN(5,"人才培养专项");
	
	private int value;
	private String desc;
	
	private FoundSourceEnum(int value, String desc){
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
