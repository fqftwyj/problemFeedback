package com.yuanwang.finace.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yuanwang.common.core.BaseEntity;
import com.yuanwang.finace.entity.enums.ReimburseStateEnum;
import com.yuanwang.finace.entity.enums.ReimburseTypeEnum;
import com.yuanwang.finace.entity.enums.ReviewStateEnum;
/**
 * 
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * @since 2019-05-16 11:15:07
 */
public class Review extends BaseEntity{
	private static final long serialVersionUID = 1L;
	//报销id
	private Integer reimburseId;	
	//审查意见
	private String reviewOpinion;	
	private ReviewStateEnum reviewState;
	//创建时间
	private Date createTime;	
	//更新时间
	private Date updateTime;
	//员工编号
	private String staffCode;
	private ReimburseTypeEnum reimburseType;
	// 报销日期
	private String reimburseDate;
	//报销成员
	private String reimburseMembers;

	public String getStaffCode() {
		return staffCode;
	}



	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public ReimburseTypeEnum getReimburseType() {
		return reimburseType;
	}

	public void setReimburseType(ReimburseTypeEnum reimburseType) {
		this.reimburseType = reimburseType;
	}

	public String getReimburseDate() {
		return reimburseDate;
	}

	public void setReimburseDate(String reimburseDate) {
		this.reimburseDate = reimburseDate;
	}

	public String getReimburseMembers() {
		return reimburseMembers;
	}

	public void setReimburseMembers(String reimburseMembers) {
		this.reimburseMembers = reimburseMembers;
	}

	/**获取报销id
	*/
	public Integer getReimburseId(){
		return this.reimburseId;
	}
	/**设置报销id
	 * @param reimburseId
	 */
	public void  setReimburseId(Integer reimburseId){
		this.reimburseId = reimburseId;
	}
	/**获取审查意见
	*/
	public String getReviewOpinion(){
		return this.reviewOpinion;
	}
	/**设置审查意见
	 * @param reviewOpinion
	 */
	public void  setReviewOpinion(String reviewOpinion){
		this.reviewOpinion = reviewOpinion;
	}
	/**获取报销类别【enum】(0:未审查:notReview,1:已审查:hasReview)
	*/
	public ReviewStateEnum getReviewState(){
		return this.reviewState;
	}
	/**设置审查状态【enum】(0:未审查:notReview,1:审查中:isReview,2:审查通过:passReview,3:审查不通过:notpassReview)
	 * @param reviewState
	 */
	public void  setReviewState(ReviewStateEnum reviewState){
		this.reviewState = reviewState;
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
