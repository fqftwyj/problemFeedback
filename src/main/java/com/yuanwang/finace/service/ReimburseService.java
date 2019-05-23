package com.yuanwang.finace.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuanwang.finace.dao.ReimburseMapper;
import com.yuanwang.finace.entity.Reimburse;
import com.yuanwang.common.core.BaseService;
/**
 * ReimburseService
 * 
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * 时间：2019-05-23 09:26:49
 */
@Service
public class ReimburseService implements BaseService<Reimburse, ReimburseMapper, Integer> {
	
	@Resource
	private ReimburseMapper reimburseMapper;
	@Override
	public ReimburseMapper getEntityDao() {
		return reimburseMapper;
	}
	
	
}
