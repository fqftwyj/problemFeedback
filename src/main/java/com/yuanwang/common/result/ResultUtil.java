package com.yuanwang.common.result;

/**返回对象工具类
 * @author crj
 *
 */
public class ResultUtil {
	
	
	/**正确时返回的值 不带值
     * @return 返回对象
     */
    public static Result success(){
    	Result result = new Result();
    	result.setCode(ResultEnum.SUCCESS.getCode());
    	result.setMsg(ResultEnum.SUCCESS.getMsg());
    	result.setData(null);
        return result;
    }
   
    /**正确时返回的值
     * @param msg 自定义正确信息
     * @return 返回对象
     */
    public static Result success(String msg){
    	Result result = new Result();
    	result.setCode(ResultEnum.SUCCESS.getCode());
    	result.setMsg(msg);
    	result.setData(null);
        return result;
    }
    
    /**正确时返回的值 带值
     * @param msg 自定义正确信息
     * @param data 返回对象
     * @return 返回对象
     */
    public static Result success(String msg,Object data){
        Result result = new Result();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
    
    /**分页成功返回的值
     * @param msg 提示信息
     * @param count 总数量
     * @param data 数据
     * @return 返回对象
     */
    public static Result success(String msg,Integer count,Object data){
        Result result = new Result();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(msg);
        result.setCount(count);
        result.setData(data);
        return result;
    }
 
    /**默认错误时返回的值
     * @return 返回对象
     */
    public static Result error(){
        Result result = new Result();
        result.setCode(ResultEnum.FAULURE.getCode());
        result.setMsg(ResultEnum.FAULURE.getMsg());
        return result;
    }
    
    /**错误时返回的值
     * @param msg 自定义错误信息
     * @return 返回对象
     */
    public static Result error(String msg){
        Result result = new Result();
        result.setCode(ResultEnum.FAULURE.getCode());
        result.setMsg(msg);
        return result;
    }
    
    /**错误时返回的值
     * @param code 自定义错误编号
     * @param msg 自定义错误信息
     * @return 返回对象
     */
    public static Result error(Integer code,String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
    
    
}
