package com.dev.base.mybatis.service;

import java.util.List;
import java.util.Map;

public interface BaseMapMybatisService {
	/**
	 * 
			*@name 新增对象
			*@Description  
			*@CreateDate 2015年8月21日下午1:33:58
	 */
	Map<String, Object> add(Map<String, Object> param);

	/**
	 * 
			*@name 批量新增
			*@Description  
			*@CreateDate 2015年8月21日下午1:34:07
	 */
	void batchAdd(List<Map<String, Object>> batchList);
	
	/**
	 * 
			*@name 修改对象
			*@Description  
			*@CreateDate 2015年8月21日下午1:34:16
	 */
	Map<String, Object> update(Map<String, Object> param);

	/**
	 * 
			*@name 删除对象
			*@Description  
			*@CreateDate 2015年8月21日下午1:34:26
	 */
	void delete(Map<String, Object> param);

	/**
	 * 
			*@name 根据id进行删除
			*@Description  
			*@CreateDate 2015年8月21日下午1:34:34
	 */
	void deleteById(String id);
	
	/**
	 * 
			*@name 分页查询列表
			*@Description  
			*@CreateDate 2015年8月21日下午1:34:43
	 */
	List<Map<String, Object>> listPage(int pageNumber,int pageSize,Map<String,Object> paramMap);

	/**
	 * 
			*@name 查询数目
			*@Description  
			*@CreateDate 2015年8月21日下午1:34:53
	 */
	long count(Map paramMap);

	/**
	 * 
			*@name 获取指定对象记录
			*@Description  
			*@CreateDate 2015年8月21日下午1:35:01
	 */
	Map<String, Object> get(Map paramMap);
	
	/**
	 * 
			*@name 根据id获取对象
			*@Description  
			*@CreateDate 2015年8月21日下午1:35:09
	 */
	Map<String, Object> getById(String id);
}
