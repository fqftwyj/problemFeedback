package com.yuanwang.common.result;

/**返回枚举
 * @author crj
 *
 */
public enum ResultEnum {
	
	FAULURE(-1, "失败"),
	SUCCESS(0, "成功");
	
	private int code;
	private String msg;
	
	private ResultEnum(Integer code, String msg) {
	    this.code = code;
	    this.msg = msg;
	}

	public Integer getCode() {
	    return code;
	}

	public String getMsg() {
	    return msg;
	}
	
	
}
