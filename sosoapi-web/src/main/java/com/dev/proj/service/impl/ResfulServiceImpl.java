package com.dev.proj.service.impl;

import com.dev.base.jdbc.datasource.DataSourceContextHolder;
import com.dev.base.json.JsonUtils;
import com.dev.base.mybatis.service.impl.BaseMybatisServiceImpl;
import com.dev.base.util.Pager;
import com.dev.proj.controller.ProjController;
import com.dev.proj.dao.ResfulDao;
import com.dev.proj.entity.Resful;
import com.dev.proj.service.ResfulService;
import io.vertx.ext.web.RoutingContext;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by koukouken on 2017/11/6.
 */
@Service
public class ResfulServiceImpl
        extends BaseMybatisServiceImpl<Resful, String, ResfulDao>
        implements ResfulService {
    public static String DB_SOURCE=null;
    static{
        InputStream in = null;
        try{
            Properties properties = new Properties();
            in =  ProjController.class.getClassLoader().getResourceAsStream("api-cfg.properties");
            properties.load(new InputStreamReader(in, "utf-8"));
            DB_SOURCE = properties.getProperty("DB_SOURCE");

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(in != null){
                try{
                    in.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void getAll(RoutingContext routingContext) {
        // Map<String, Object> byId = getMybatisDao().getById("1");
        DataSourceContextHolder. setCurrent(DB_SOURCE);
        String dbName = routingContext.request().getParam("dbName");
        String tableName = routingContext.request().getParam("tableName");
        List<Map> list = getMybatisDao().selectTableAllList(tableName);
        DataSourceContextHolder. setCurrent("bonecpDataSource");
        routingContext.response().end(JsonUtils.toJson(list));
    }

    @Override
    public void getId(RoutingContext routingContext) {
        //String dbName = routingContext.request().getParam("dbName");
        String tableName = routingContext.request().getParam("tableName");
        String id = routingContext.request().getParam("id");
        //获取数据库的表名的主键
        DataSourceContextHolder. setCurrent(DB_SOURCE);
        String priKey = getMybatisDao().selectTableKey("'"+tableName+"'");
        List<Map> list = getMybatisDao().selectTableListById(tableName, priKey, id);
        DataSourceContextHolder. setCurrent("bonecpDataSource");
        routingContext.response().end(JsonUtils.toJson(list));
    }


    @Override
    public List<Map<String, Object>> listAll(String code, String nickName, String status, Pager pager) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Map> selectAllTableMsg() {
        return getMybatisDao().selectAllTableMsg();
    }

    @Override
    public List<Map> selectPrikByTName(String tableName) {
        return getMybatisDao().selectPrikByTName(tableName);
    }


}
