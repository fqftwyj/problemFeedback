package com.yuanwang.finace.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yuanwang.common.core.BaseEntity;
import com.yuanwang.finace.entity.enums.ReimburseStateEnum;
import com.yuanwang.finace.entity.enums.ReimburseTypeEnum;
import com.yuanwang.finace.entity.enums.ReviewStateEnum;
import com.yuanwang.finace.entity.enums.SecondReviewStateEnum;

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
	//审核意见
	private String reviewOpinion;	
	private ReviewStateEnum reviewState;
	private SecondReviewStateEnum secondReviewState;
	//第二级审核意见
	private String secondReviewOpinion;
	//创建时间
	private String createTime;
	//更新时间
	private String updateTime;
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
	/**获取审核意见
	*/
	public String getReviewOpinion(){
		return this.reviewOpinion;
	}
	/**设置审核意见
	 * @param reviewOpinion
	 */
	public void  setReviewOpinion(String reviewOpinion){
		this.reviewOpinion = reviewOpinion;
	}
	/**获取报销类别【enum】(0:未审核:notReview,1:已审核:hasReview)
	*/
	public ReviewStateEnum getReviewState(){
		return this.reviewState;
	}
	/**设置审核状态【enum】(0:未审核:notReview,1:审核中:isReview,2:审核通过:passReview,3:审核不通过:notpassReview)
	 * @param reviewState
	 */
	public void  setReviewState(ReviewStateEnum reviewState){
		this.reviewState = reviewState;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	/**获取审核类别【enum】(0:未审核:notReview,1:审核中:isReview,2:审核通过:passReview,3:审核不通过:notpassReview)
	 */
	public SecondReviewStateEnum getSecondReviewState(){
		return this.secondReviewState;
	}
	/**设置审核类别【enum】(0:未审核:notReview,1:审核中:isReview,2:审核通过:passReview,3:审核不通过:notpassReview)
	 * @param secondReviewState
	 */
	public void  setSecondReviewState(SecondReviewStateEnum secondReviewState){
		this.secondReviewState = secondReviewState;
	}
	/**获取第二级审核意见
	 */
	public String getSecondReviewOpinion(){
		return this.secondReviewOpinion;
	}
	/**设置第二级审核意见
	 * @param secondReviewOpinion
	 */
	public void  setSecondReviewOpinion(String secondReviewOpinion){
		this.secondReviewOpinion = secondReviewOpinion;
	}
}
