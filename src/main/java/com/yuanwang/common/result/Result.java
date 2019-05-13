package com.yuanwang.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**http请求返回的最外层对象
 * @author crj
 *
 */
@JsonInclude(Include.NON_NULL)
public class Result {
	
	 /**
	 * 状态码
	 */
	private int code;
	 
     /**
     * 信息
     */
    private String msg;

    /**
     * 分页用的数据总数
     */
    private Integer count;
     /**
     * 数据
     */
    private Object data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
    
    
     
}
