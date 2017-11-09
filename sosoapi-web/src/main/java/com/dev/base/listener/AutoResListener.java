package com.dev.base.listener;

import com.dev.proj.controller.ProjController;
import com.dev.proj.service.ResfulService;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by koukouken on 2017/11/6.
 */
public class AutoResListener implements ServletContextListener {
    private static Logger logger = LogManager.getLogger(AutoResListener.class);

     public static String API_PORT = null;
    static{
        InputStream in = null;
        try{
            Properties properties = new Properties();
            in =  ProjController.class.getClassLoader().getResourceAsStream("api-cfg.properties");
            properties.load(new InputStreamReader(in, "utf-8"));
            API_PORT = properties.getProperty("api.port");

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
    public void contextInitialized(ServletContextEvent sce) {
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();

        Router mainRouter = Router.router(vertx);

        Router dbapiRouter = Router.router(vertx);
        mainRouter.mountSubRouter("/dbapi", dbapiRouter);


        Router dbmetaRouter = Router.router(vertx);
        mainRouter.mountSubRouter("/dbmeta", dbmetaRouter);

        // main.dbapi
        ResfulService resfulService = WebApplicationContextUtils.
                getWebApplicationContext(sce.getServletContext()).getBean(ResfulService.class);
        //configService.initConfig();
//        List<ProjectInfo> list = projectService.listByUserId(1L, "", "", ProjectStatus.open,
//                new Pager(1, 10));
//        logger.info("长度："+list.size());

        dbapiRouter.route().handler(routingContext -> {
            // LOG.debug("comming request: {}",
            // routingContext.request().absoluteURI());
            routingContext.response().putHeader("Server", "sosoapi");
            routingContext.response().putHeader("Content-Type", "application/json;charset=UTF-8");
            routingContext.response().putHeader("Access-Control-Allow-Origin", "*");
            //如果存在自定义的header参数，需要在此处添加，逗号分隔
            routingContext.response().putHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, "
                    + "If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, "
                    + "Content-Type, X-E4M-With");
            routingContext.response().putHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            routingContext.response().putHeader("Access-Control-Allow-Credentials", "true");
            routingContext.next();
        });

        dbapiRouter.route(HttpMethod.GET, "/:dbName/:tableName").handler(resfulService::getAll);
         dbapiRouter.route(HttpMethod.GET,
         "/:dbName/:tableName/:id").handler(resfulService::getId);
        // dbapiRouter.route(HttpMethod.DELETE,
        // "/:dbName/:tableName/:id").handler(dbapiAction::delete);
        //
         dbapiRouter.route().handler(BodyHandler.create()); // For POST/PUT
//         dbapiRouter.route(HttpMethod.POST,
//         "/:dbname/:tableName").handler(dbapiAction::post);
//         dbapiRouter.route(HttpMethod.PUT,
//         "/:dbName/:tableName/:id").handler(dbapiAction::put);

        // main.dbmeta
        // DbmetaAction dbmetaAction = new DbmetaAction();
//        String uploadDir = System.getProperty("java.io.tmpdir") + File.separator + "sosoapi";
//
//        dbmetaRouter.route()
//                .handler(BodyHandler.create(uploadDir).setBodyLimit(1024 * 1024).setDeleteUploadedFilesOnEnd(true));
        // dbmetaRouter.route(HttpMethod.POST,
        // "/:dbname").handler(dbmetaAction::upload);

        // handler all requests in RequestHandler
        server.requestHandler(mainRouter::accept);
        logger.error("监听数据库开始");
        int port = Integer.parseInt(System.getProperty("port", API_PORT));
        server.listen(port);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
