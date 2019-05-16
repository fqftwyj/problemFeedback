package com.yuanwang.sys.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yuanwang.common.core.BaseEntity;
/**
 * 
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * @since 2019-05-16 09:47:26
 */
public class Staff extends BaseEntity{
	private static final long serialVersionUID = 1L;

	//科室编号
	private String officeCode;	
	//员工编号
	private String staffCode;	
	//员工名称
	private String staffName;	
	//联系方式
	private String phone;	
	//创建时间
	private Date createTime;	
	//更新时间
	private Date updateTime;	
	
	/**获取科室编号
	*/
	public String getOfficeCode(){
		return this.officeCode;
	}
	/**设置科室编号
	 * @param officeCode
	 */
	public void  setOfficeCode(String officeCode){
		this.officeCode = officeCode;
	}
	/**获取员工编号
	*/
	public String getStaffCode(){
		return this.staffCode;
	}
	/**设置员工编号
	 * @param staffCode
	 */
	public void  setStaffCode(String staffCode){
		this.staffCode = staffCode;
	}
	/**获取员工名称
	*/
	public String getStaffName(){
		return this.staffName;
	}
	/**设置员工名称
	 * @param staffName
	 */
	public void  setStaffName(String staffName){
		this.staffName = staffName;
	}
	/**获取联系方式
	*/
	public String getPhone(){
		return this.phone;
	}
	/**设置联系方式
	 * @param phone
	 */
	public void  setPhone(String phone){
		this.phone = phone;
	}
	/**获取创建时间
	*/
	public Date getCreateTime(){
		return this.createTime;
	}
	/**设置创建时间
	 * @param createTime
	 */
	public void  setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	/**获取更新时间
	*/
	public Date getUpdateTime(){
		return this.updateTime;
	}
	/**设置更新时间
	 * @param updateTime
	 */
	public void  setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	
}
