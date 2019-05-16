package com.yuanwang.finace.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuanwang.finace.dao.ReviewMapper;
import com.yuanwang.finace.entity.Review;
import com.yuanwang.common.core.BaseService;
/**
 * ReviewService
 * 
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * 时间：2019-05-16 11:15:07
 */
@Service
public class ReviewService implements BaseService<Review, ReviewMapper, Integer> {
	
	@Resource
	private ReviewMapper reviewMapper;
	@Override
	public ReviewMapper getEntityDao() {
		return reviewMapper;
	}
	
	
}
