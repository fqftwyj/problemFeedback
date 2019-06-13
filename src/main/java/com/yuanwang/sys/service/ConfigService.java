package com.yuanwang.sys.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.yuanwang.sys.dao.ConfigMapper;
import com.yuanwang.sys.entity.Config;
import com.yuanwang.common.core.BaseService;
/**
 * ConfigService
 * 系统配置表
 * @author  crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * 时间：2019-06-13 08:12:16
 */
@Service
public class ConfigService implements BaseService<Config, ConfigMapper, Integer> {
	
	@Resource
	private ConfigMapper configMapper;
	@Override
	public ConfigMapper getEntityDao() {
		return configMapper;
	}
	
	
}
