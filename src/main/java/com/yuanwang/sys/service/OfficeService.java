package com.yuanwang.sys.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuanwang.sys.dao.OfficeMapper;
import com.yuanwang.sys.entity.Office;
import com.yuanwang.common.core.BaseService;
/**
 * OfficeService
 * 
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * 时间：2019-05-16 09:47:26
 */
@Service
public class OfficeService implements BaseService<Office, OfficeMapper, Integer> {
	
	@Resource
	private OfficeMapper officeMapper;
	@Override
	public OfficeMapper getEntityDao() {
		return officeMapper;
	}
	
	
}
