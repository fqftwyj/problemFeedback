package com.yuanwang.common.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.mybatis.spring.SqlSessionTemplate;

import com.github.pagehelper.PageInfo;
import com.yuanwang.common.config.ProjectDefined;


/**所有dao层Mapper类的父类
 * @author crj
 *
 * @param <T> 对象
 * @param <P> 主键
 */
public class BaseMapper<T, P extends Serializable> {
	private String namespace;
	
	@Resource
    private SqlSessionTemplate sqlSession;
    
	/**
	 * 构造器获取泛型类
	 */
	public BaseMapper(){
		namespace=this.getClass().getName()+".";
	}
	
	
    /**根据主键id查找对象
     * @param id 主键id
     * @return 对象
     */
    public T findOneById(final P id) {
		return sqlSession.selectOne(namespace+"findById", id);
	}
    
    /**根据条件查找对象
     * @param map 条件map
     * @return 返回对象
     */
    public T findOneByMap(Map<String,Object> map) {
		return sqlSession.selectOne(namespace+"findByMap", map);
	}
    
    /**根据Map条件查询
     * @param methodId 自定义方法id
     * @param map map条件
     * @return 查找对象
     */
    public T findOneByMap(String methodId,Map<String,Object> map) {
		return sqlSession.selectOne(namespace+methodId, map);
	}
    
	/**根据Map条件查询
	 * @param map map条件
	 * @return list 全部数据
	 */
	public List<T> findListByMap(Map<String,Object> map){
		return sqlSession.selectList(namespace+"findByMap", map);
	}
	
	/**根据Map条件查询
	 * @param methodId 自定义方法id
	 * @param map map条件
	 * @return list
	 */
	public List<T> findListByMap(String methodId,Map<String,Object> map){
		return sqlSession.selectList(namespace+methodId, map);
	}
	
	/**根据Map条件查询 返回map对象
	 * @param methodId 自定义方法id
	 * @param map map条件
	 * @return map 
	 */
	public List<Map<String,Object>> findListMapByMap(String methodId,Map<String,Object> map){
		return sqlSession.selectList(namespace+methodId, map);
	}
	
	/**无条件获取全部数据
	 * @return list 全部数据
	 */
	public List<T> findAll() {
		return findAll("findAll");
	}
	
	/**自定义无条件获取全部数据
	 * @param methodId 自定义方法id
	 * @return 全部数据
	 */
	public List<T> findAll(String methodId) {
		return sqlSession.selectList(namespace+methodId);
	}
	
	
	/**excel导出要查询的方法
	 * @param map 查询条件
	 * @return 返回map的list
	 */
	public List<Map<String,Object>> excelExportList(Map<String,Object> map){
		return sqlSession.selectList(namespace+"excelExportListByMap", map);
	}
	
	/**传入实体对象插入
	 * @param entity 实体对象
	 * @return 插入条数
	 */
	public Integer insert(final T entity) {
		return insert("insert", entity);
	}
	
	/**自定义传入实体对象插入
	 * @param methodId 自定义方法id
	 * @param entity 实体对象
	 * @return 插入条数
	 */
	public Integer insert(String methodId,final T entity) {
		setNullTime(entity);
		return sqlSession.insert(namespace+methodId, entity);
	}
	
	/**传入Map对象插入
	 * @param map map对象
	 * @return 插入条数
	 */
	public Integer insert(Map<String, Object> map){
		setNullTime(map);
		return sqlSession.insert(namespace+"insert", map);
	}
	
	/**自定义方法传入Map对象插入
	 * @param methodId 自定义方法id
	 * @param map map对象
	 * @return 插入条数
	 */
	public Integer insert(String methodId,Map<String, Object> map){
		setNullTime(map);
		return sqlSession.insert(namespace+methodId, map);
	}
	
	/**批量插入
	 * @param list list对象
	 * @return 插入条数
	 */
	public Integer insertBatch(final List<T> list) {
		return insertBatch("insertBatch", list);
	}
	
	/**自定义方法批量插入
	 * @param methodId 自定义方法id
	 * @param list list对象
	 * @return 插入条数
	 */
	public Integer insertBatch(String methodId,final List<T> list) {
		for(T t:list){
			setNullTime(t);
		}
		return sqlSession.insert(namespace+methodId, list);
	}
	
	/**传入实体对象更新
	 * @param entity 实体对象
	 * @return 更新条数
	 */
	public Integer update(final T entity) {
		return update("update", entity);
	}
	
	/**自定义传入实体对象更新
	 * @param methodId 自定义方法id
	 * @param entity 实体对象
	 * @return 更新条数
	 */
	public Integer update(String methodId, final T entity) {
		setNullTime(entity);
		return sqlSession.update(namespace+methodId, entity);
	}
	
	/**传入Map对象更新
	 * @param map map对象
	 * @return 更新条数
	 */
	public Integer update(Map<String, Object> map){
		setNullTime(map);
		return sqlSession.update(namespace+"update", map);
	}
	
	/**自定义传入Map对象更新
	 * @param methodId 自定义方法id
	 * @param map map对象
	 * @return 更新条数
	 */
	public Integer update(String methodId, Map<String,Object> map) {
		setNullTime(map);
		return sqlSession.update(namespace+methodId, map);
	}
	
	/**批量更新
	 * @param list list对象
	 * @return 批量更新条数
	 */
	public Integer updateBatch(final List<T> list) {
		return insertBatch("insertBatch", list);
	}
	
	/**自定义方法批量更新
	 * @param methodId 自定义方法id
	 * @param list list对象
	 * @return 批量更新条数
	 */
	public Integer updateBach(String methodId,final List<T> list) {
		for(T t:list){
			setNullTime(t);
		}
		return sqlSession.update(namespace+methodId, list);
	}
	
	/**按主键id删除
	 * @param id 主键id
	 * @return 删除条数
	 */
	public Integer delete(final P id) {
		return sqlSession.delete(namespace+"deleteById", id);
	}
	
	/**多个主键id删除
	 * @param ids 主键id集合
	 * @return 删除个数
	 */
	public Integer delete(final Collection<P> ids) {
		return sqlSession.delete(namespace+"deleteByIds", ids);
	}
	
	/**根据条件删除
	 * @param map 条件
	 * @return 删除条数
	 */
	public Integer delete(Map<String, Object> map) {
		return delete("deleteByMap",map);
	}
	
	/**自定义根据条件删除
	 * @param methodId 自定义方法id
	 * @param map 条件
	 * @return 删除条数
	 */
	public Integer delete(String methodId,Map<String, Object> map) {
		return sqlSession.delete(namespace+methodId, map);
	}
	
	/**批量删除
	 * @param list list对象
	 * @return 删除条数
	 */
	public Integer deleteBatch(final List<T> list) {
		return deleteBatch("deleteBatch", list);
	}
	
	/**自定义批量删除
	 * @param methodId 自定义方法id
	 * @param list list对象
	 * @return 删除条数
	 */
	public Integer deleteBatch(String methodId,final List<T> list) {
		return sqlSession.delete(namespace+methodId, list);
	}
	
	
	/**保存 根据对象是否有id值 判断执行插入和是更新操作
	 * @param entity 要插入或更新的对象
	 * @return 保存条数
	 */
	public Integer save(T entity) {
		if(entity instanceof IdKey) {
			IdKey keyEntity = (IdKey) entity;
			if(keyEntity.getId() != null){
				return update(entity);
			}else{
				return insert(entity);
			}
		}
		return 0;
	}

	/**执行存储过程
	 * @param methodId 自定义方法id
	 * @param map 传入条件
	 * @return 传出参数
	 */
	public Map<String, Object> executeProce(String methodId,Map<String, Object> map) {
		sqlSession.selectList(namespace+methodId, map);
		return map;
	}
	
	/**存储过程查询
	 * @param methodId 自定义方法id
	 * @param map 传入条件
	 * @return 返回分页结果集
	 */
	public List<T> findListByProce(String methodId,Map<String, Object> map) {
		return sqlSession.selectList(namespace+methodId, map);
	}
	
	/** 存储过程分页查询(自定义最大页大小)
	 * @param methodId 自定义方法id
	 * @param pageNo 页号
	 * @return 返回分页结果集
	 */
	public PageInfo<T> findListByProceAndPage(String methodId,@NotNull int pageNo) {
		Map<String, Object> map = new HashMap<String,Object>();
		Integer pageSize = ProjectDefined.DEFAULT_MAX_PAGE_SIZE;
		map.put("pageNum", pageNo);
    	map.put("pageSize", pageSize);
    	map.put("startRow", (pageNo-1)*pageSize);
    	map.put("endRow", (pageNo)*pageSize);
		List<T> list=sqlSession.selectList(namespace+methodId, map);
		PageInfo<T> result=new PageInfo<>(list);
		map.put("size", list.size());
		initProcePageInfo(result,map);
		return result;
	}
	
	/**存储过程分页查询（采用默认最大页大小）
	 * @param methodId 自定义方法id
	 * @param pageNo 当前页
	 * @param pageSize 页大小
	 * @return 返回分页结果集
	 */
	public PageInfo<T> findListByProceAndPage(String methodId,@NotNull int pageNo,@NotNull int pageSize) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("pageNum", pageNo);
    	map.put("pageSize", pageSize);
    	map.put("startRow", (pageNo-1)*pageSize);
    	map.put("endRow", (pageNo)*pageSize);
		List<T> list=sqlSession.selectList(namespace+methodId, map);
		PageInfo<T> result=new PageInfo<>(list);
		map.put("size", list.size());
		initProcePageInfo(result,map);
		return result;
	}
	
	/**初始化存储过程分页对象
	 * @param pageInfo
	 * @param map
	 */
	private void initProcePageInfo(PageInfo<T> pageInfo,Map<String, Object> map) {
		int pageNum=(int) map.get("pageNum");
		int pageSize=(int) map.get("pageSize");
		int size=(int) map.get("size");
		int startRow=(int) map.get("startRow");
		int endRow=(int) map.get("endRow");
		int total=(int) map.get("v_total");
		int pages=(int) Math.ceil((float)total/pageSize);
		pageInfo.setPages(pages);
		pageInfo.setHasNextPage(pageNum<pages?true:false);
		pageInfo.setHasPreviousPage((pageNum>1&&pageNum<pages)?true:false);
		pageInfo.setIsFirstPage(pageNum==1?true:false);
		pageInfo.setIsLastPage(pageNum==pages?true:false);
		pageInfo.setNextPage((pageNum<pages)?(pageNum+1):0);
		pageInfo.setPageNum(pageNum);
		pageInfo.setPageSize(pageSize);
		pageInfo.setPrePage((pageNum>1&&pageNum<pages)?(pageNum-1):0);
		pageInfo.setTotal(total);
		pageInfo.setSize(size);
		pageInfo.setStartRow(startRow);
		pageInfo.setEndRow(endRow);
	}
	
	/**维护创建时间跟更新时间
	 * @param entity 操作对象
	 */
	private void setNullTime(Object entity){
		if(entity instanceof BaseEntity){
			BaseEntity model=(BaseEntity) entity;
			if(model.getId()==null){
				model.setCreatedTime(new Date());
			}
			model.setUpdatedTime(new Date());
		}else if(entity instanceof Map){
			Map<String,Object> model=(Map<String,Object>) entity;
			if(!model.containsKey("id")){
				model.put("createdAt", new Date());
			}
			model.put("updatedAt", new Date());
		}
	}
}
