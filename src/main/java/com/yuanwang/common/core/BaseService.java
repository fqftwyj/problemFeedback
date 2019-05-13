package com.yuanwang.common.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuanwang.common.config.ProjectDefined;
import com.yuanwang.common.enums.OperatorEnum;

/**所有service层的父类
 * @author crj
 *
 * @param <T> 对象
 * @param <D> 持久层
 * @param <P> 主键
 */
public interface  BaseService<T,D extends BaseMapper<T, P>, P  extends Serializable> {
	
	/**获取持久层对象
	 * @return 持久层对象
	 */
	public abstract D getEntityDao();
	
	/**excel导出
	 * @param map 查询条件
	 * @param orderBy 排序 
	 * @param pageNo 页号
	 * @param pageSize 每页显示记录大小
	 * @return 返回结果集
	 */
	public default List<Map<String,Object>> excelExportList(Map<String,Object> map,String orderBy,
			@NotNull int pageNo,@NotNull int pageSize){
		PageHelper.orderBy(orderBy);
    	PageHelper.startPage(pageNo, pageSize);
    	return getEntityDao().excelExportList(map);
	}
	
	/**自定义mapper分页查询（采用默认最大页大小）
	 * @param methodId 自定义methodid
	 * @param map 查询条件
	 * @param orderBy 排序 
	 * @param pageNo 页号
	 * @return 返回分页对象
	 */
	public default PageInfo<T> findByPage(String methodId,Map<String,Object> map,String orderBy,@NotNull int pageNo){
    	PageHelper.orderBy(orderBy);
    	PageHelper.startPage(pageNo, ProjectDefined.DEFAULT_MAX_PAGE_SIZE);
    	List<T> list=getEntityDao().findListByMap(methodId, map);
        return new PageInfo<>(list);
    }
	
	/**mapper分页查询（采用默认最大页大小）
	 * @param map 查询条件
	 * @param orderBy 排序 
	 * @param pageNo 页号
	 * @return 返回分页对象
	 */
	public default PageInfo<T> findByPage(Map<String,Object> map,String orderBy,@NotNull int pageNo){
    	PageHelper.orderBy(orderBy);
    	PageHelper.startPage(pageNo, ProjectDefined.DEFAULT_MAX_PAGE_SIZE);
    	List<T> list=getEntityDao().findListByMap(map);
        return new PageInfo<>(list);
    }
	
	/**自定义mapper分页查询（自定义最大页大小）
	 * @param methodId 自定义methodid
	 * @param map 查询条件
	 * @param orderBy 排序 
	 * @param pageNo 页号
	 * @param pageSize 每页显示记录大小
	 * @return 返回分页对象
	 */
	public default PageInfo<T> findByPage(String methodId,Map<String,Object> map,String orderBy,@NotNull int pageNo,@NotNull int pageSize){
		PageHelper.orderBy(orderBy);
    	PageHelper.startPage(pageNo, pageSize);
    	List<T> list=getEntityDao().findListByMap(methodId, map);
        return new PageInfo<>(list);
    }
	
	/**mapper分页查询（自定义最大页大小）
	 * @param map 查询条件
	 * @param orderBy 排序 
	 * @param pageNo 页号
	 * @param pageSize 每页显示记录大小
	 * @return 返回分页对象
	 */
	public default PageInfo<T> findByPage(Map<String,Object> map,String orderBy,@NotNull int pageNo,@NotNull int pageSize){
		PageHelper.orderBy(orderBy);
    	PageHelper.startPage(pageNo, pageSize);
    	List<T> list=getEntityDao().findListByMap(map);
        return new PageInfo<>(list);
    }
    
    
    /**存储过程分页查询（采用默认最大页大小）返回总数一定要是v_total
     * @param methodId mapper自定义方法id
     * @param pageNo 当前页
     * @return 返回分页对象
     */
    public default PageInfo<T> findListByProceAndPage(@NotNull String methodId,@NotNull int pageNo){
        return getEntityDao().findListByProceAndPage(methodId,pageNo);
    }
    
    
    /**存储过程分页查询（自定义最大页大小）返回总数一定要是v_total
     * @param methodId mapper自定义方法id
     * @param pageNo 当前页
     * @param pageSize 页大小
     * @return 返回分页对象
     */
    public default PageInfo<T> findListByProceAndPage(@NotNull String methodId,
    		@NotNull int pageNo,@NotNull int pageSize){
    	return getEntityDao().findListByProceAndPage(methodId,pageNo,pageSize);
    }
    
    /**执行存储过程
     * @param methodId mapper自定义方法id
     * @param map IN参数
     * @return 返回存储过程OUT参数
     */
    public default Map<String, Object> executeProce(String methodId,Map<String, Object> map) {
    	return getEntityDao().executeProce(methodId,map);
    }
    
    /**存储过程查询（不分页）
     * @param methodId mapper自定义方法id
     * @param map 传入参数
     * @return 返回list对象
     */
    public default List<T> findListByProce(String methodId,Map<String, Object> map) {
   	 return getEntityDao().findListByProce(methodId,map);
   }
    
	/**保存
	 * @param entity 保存对象
	 * @param map 查重条件
	 * @return 返回修改条数
	 */
	public default Integer save(T entity,Map<String,Object> map,OperatorEnum operator){
		if(!map.isEmpty()) {
			if(operator==OperatorEnum.OR){
				map.put("operator", operator.getDesc());
			}
			List<T> exist=getEntityDao().findListByMap(map);
			if(!exist.isEmpty()) {
				return 2;
			}
		}
		
		return getEntityDao().save(entity);
	}
	
	/**根据主键id查找对象
	 * @param id 主键id
	 * @return 返回对象
	 */
	public default T find(P id) {
		return getEntityDao().findOneById(id);
	}
	
	/**根据条件查找对象
	 * @param map 条件map
	 * @return 返回对象
	 */
	public default T find(Map<String,Object> map) {
		return (T) getEntityDao().findOneByMap(map);
	}

	/**自定义sql根据内容查找对象
	 * @param methodId
	 * @param map
	 * @return
	 */
	public default T find(String methodId,Map<String,Object> map) {
		return (T) getEntityDao().findOneByMap(methodId,map);
	}
	
	/**根据条件查找多个对象
	 * @param map 查询条件
	 * @return 返回查询对象
	 */
	public default List<T> findList(Map<String,Object> map) {
		return  getEntityDao().findListByMap(map);
	}
	
	/**查找全部
	 * @return 返回全部对象
	 */
	public default List<T> findAll() {
		return getEntityDao().findAll();
	}
	
	/**插入对象
	 * @param object 对象
	 * @return 返回插入条数
	 */
	public default Integer insert(T object){
		return this.getEntityDao().insert(object);
	}
	
	/**批量插入
	 * @param objects 插入对象
	 * @return 返回插入条数
	 */
	public default Integer insertBatch(List<T> objects){
		return this.getEntityDao().insertBatch(objects);
	}
	
	/**更新对象
	 * @param object 对象
	 * @return 返回更新条数
	 */
	public default Integer update(T object){
		return this.getEntityDao().update(object);
	}
	
	/**更新对象
	 * @param object object 对象
	 * @param map map 查重条件
	 * @param operator 查重逻辑运算符，or还是and
	 * @return 返回更新条数
	 */
	public default Integer update(T object,Map<String,Object> map,OperatorEnum operator){
		if(!map.isEmpty()) {
			if(object instanceof IdKey){
				map.put("excludeId", ((IdKey)object).getId());
			}
			if(operator==OperatorEnum.OR){
				map.put("operator", operator.getDesc());
			}
			List<T> exist=getEntityDao().findListByMap(map);
			if(!exist.isEmpty()) {
				return 2;
			}
		}
		return this.getEntityDao().update(object);
	}

	/**根据主键id删除对象
	 * @param id 主键id
	 * @return 返回删除条数
	 */
	public default Integer delete(P id) {
		return getEntityDao().delete(id);
	}
	
	
	/**根据主键id集合删除对多个对象
	 * @param ids 主键id集合
	 * @return 返回删除个数
	 */
	public default Integer delete(List<P> ids) {
		return getEntityDao().delete(ids);
	}
	
	
	/**根据map条件删除对象
	 * @param map map条件
	 * @return 返回删除个数
	 */
	public default Integer delete(Map<String,Object> map){
		return getEntityDao().delete(map);
	}
}
