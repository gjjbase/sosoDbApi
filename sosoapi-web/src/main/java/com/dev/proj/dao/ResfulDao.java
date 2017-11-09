package com.dev.proj.dao;

import com.dev.base.mybatis.dao.BaseMybatisDao;
import com.dev.proj.entity.Resful;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by koukouken on 2017/11/6.
 */

public interface ResfulDao extends BaseMybatisDao<Resful,String>{
    List<Map> selectTableAllList(@Param("tableName")String tableName);

    List<Map> selectTableListById(@Param("tableName")String tableName,
                               @Param("tableIdName")String tableIdName,@Param("tableIdValue") String tableIdValue);

    String selectTableKey(@Param("tableName") String tableName);

    List<Map> selectAllTableMsg();

    List<Map> selectPrikByTName(@Param("tableName")String tableName);
}
