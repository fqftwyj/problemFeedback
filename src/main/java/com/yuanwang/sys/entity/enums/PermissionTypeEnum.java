package com.yuanwang.sys.entity.enums;

public enum PermissionTypeEnum{
    
	MENU(0,"普通菜单"),
	LINK(1,"链接"),
	METHOD(2,"功能");
	
	private int value;
	private String desc;
	
	private PermissionTypeEnum(int value, String desc){
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
