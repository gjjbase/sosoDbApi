package com.dev.proj.service;

import com.dev.base.mybatis.service.BaseMybatisService;
import com.dev.base.util.Pager;
import com.dev.proj.entity.Resful;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.Map;

/**
 * Created by koukouken on 2017/11/6.
 */
public interface ResfulService
        extends BaseMybatisService<Resful, String>
{
    void getAll(RoutingContext routingContext);
    void getId(RoutingContext routingContext);
    List<Map<String,Object>> listAll(String code, String nickName,
                                     String status, Pager pager);
    List<Map> selectAllTableMsg();
    List<Map> selectPrikByTName(String tableName);

}
