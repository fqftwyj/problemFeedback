package com.yuanwang.finace.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yuanwang.common.core.BaseEntity;
import com.yuanwang.finace.entity.enums.ReimburseTypeEnum;
import com.yuanwang.finace.entity.enums.ReimburseStateEnum;
/**
 * 
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * @since 2019-05-23 09:26:49
 */
public class Reimburse extends BaseEntity{
	private static final long serialVersionUID = 1L;

	//员工编号
	private String staffCode;
	//科室编号
	private String officeCode;	
	private String officeName;
	private ReimburseTypeEnum reimburseType;
	private ReimburseStateEnum reimburseState;
	// 报销日期
	private String reimburseDate;
	//报销成员
	private String reimburseMembers;	
	//报销理由
	private String reimburseReason;	
	//报销事项
	private String reimburseItems;	
	//报销费用
	private String reimburseCost;	
	//上传文件路径
	private String uploadPath;	
	//上传文件名称
	private String uploadName;	
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;
	private int delFlag=0;//伪删除标记

	public int getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}

	private Review review;

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
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
	/**获取报销类别【enum】(1:外出培训:outtrain)
	*/
	public ReimburseTypeEnum getReimburseType(){
		return this.reimburseType;
	}
	/**设置报销类别【enum】(1:外出培训:outtrain)
	 * @param reimburseType
	 */
	public void  setReimburseType(ReimburseTypeEnum reimburseType){
		this.reimburseType = reimburseType;
	}
	/**获取报销状态【enum】(1:已上报:hasSubmit,0:未上报:notSubmit)
	*/
	public ReimburseStateEnum getReimburseState(){
		return this.reimburseState;
	}
	/**设置报销状态【enum】(1:已上报:hasSubmit,0:未上报:notSubmit)
	 * @param reimburseState
	 */
	public void  setReimburseState(ReimburseStateEnum reimburseState){
		this.reimburseState = reimburseState;
	}

    public String getReimburseDate() {
        return reimburseDate;
    }

    public void setReimburseDate(String reimburseDate) {
        this.reimburseDate = reimburseDate;
    }

    /**获取报销成员
	*/
	public String getReimburseMembers(){
		return this.reimburseMembers;
	}
	/**设置报销成员
	 * @param reimburseMembers
	 */
	public void  setReimburseMembers(String reimburseMembers){
		this.reimburseMembers = reimburseMembers;
	}
	/**获取报销理由
	*/
	public String getReimburseReason(){
		return this.reimburseReason;
	}
	/**设置报销理由
	 * @param reimburseReason
	 */
	public void  setReimburseReason(String reimburseReason){
		this.reimburseReason = reimburseReason;
	}
	/**获取报销事项
	*/
	public String getReimburseItems(){
		return this.reimburseItems;
	}
	/**设置报销事项
	 * @param reimburseItems
	 */
	public void  setReimburseItems(String reimburseItems){
		this.reimburseItems = reimburseItems;
	}
	/**获取报销费用
	*/
	public String getReimburseCost(){
		return this.reimburseCost;
	}
	/**设置报销费用
	 * @param reimburseCost
	 */
	public void  setReimburseCost(String reimburseCost){
		this.reimburseCost = reimburseCost;
	}
	/**获取上传文件路径
	*/
	public String getUploadPath(){
		return this.uploadPath;
	}
	/**设置上传文件路径
	 * @param uploadPath
	 */
	public void  setUploadPath(String uploadPath){
		this.uploadPath = uploadPath;
	}
	/**获取上传文件名称
	*/
	public String getUploadName(){
		return this.uploadName;
	}
	/**设置上传文件名称
	 * @param uploadName
	 */
	public void  setUploadName(String uploadName){
		this.uploadName = uploadName;
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
