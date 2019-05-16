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
public class Office extends BaseEntity{
	private static final long serialVersionUID = 1L;

	//科室编号
	private String officeCode;	
	//科室名称
	private String officeName;	
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
	/**获取科室名称
	*/
	public String getOfficeName(){
		return this.officeName;
	}
	/**设置科室名称
	 * @param officeName
	 */
	public void  setOfficeName(String officeName){
		this.officeName = officeName;
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
