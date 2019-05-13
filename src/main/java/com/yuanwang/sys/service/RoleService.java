package com.yuanwang.sys.service;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.yuanwang.sys.dao.RoleMapper;
import com.yuanwang.sys.entity.Role;
import com.yuanwang.common.core.BaseService;

import java.util.List;
import java.util.Map;

/**
 * RoleService
 * 角色表
 *
 * @author crj
 * @version v1.0.0
 * 描述：自动生成的代码
 * 时间：2018-08-07 15:00:08
 */
@Service
public class RoleService implements BaseService<Role, RoleMapper, Integer> {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public RoleMapper getEntityDao() {
        return roleMapper;
    }

    /**
     *回显分配权限框
     * @param idMap
     * @return
     */
    public List<Map<String, Object>> getRoleIdForCheck(Map<String, Object> idMap) {
        return roleMapper.findListMapByMap("getRoleIdForCheck", idMap);
    }

}
