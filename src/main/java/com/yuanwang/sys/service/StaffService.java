package com.yuanwang.sys.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuanwang.sys.dao.StaffMapper;
import com.yuanwang.sys.entity.Staff;
import com.yuanwang.common.core.BaseService;
/**
 * StaffService
 * 
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * 时间：2019-05-16 09:47:26
 */
@Service
public class StaffService implements BaseService<Staff, StaffMapper, Integer> {
	
	@Resource
	private StaffMapper staffMapper;
	@Override
	public StaffMapper getEntityDao() {
		return staffMapper;
	}
	
	
}
