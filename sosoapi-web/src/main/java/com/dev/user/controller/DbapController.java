package com.dev.user.controller;

import com.dev.base.enums.ProjectStatus;
import com.dev.base.util.Pager;
import com.dev.proj.service.ProjectService;
import com.dev.proj.vo.ProjectInfo;
import io.vertx.ext.web.RoutingContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServlet;
import java.util.List;

/**
 * Created by koukouken on 2017/11/6.
 */
public class DbapController extends HttpServlet {


    public void getAll(RoutingContext routingContext){
        //userDetailService.getDetailByUserId(1L);

        ProjectService projectService = WebApplicationContextUtils.
                getWebApplicationContext(getServletConfig().getServletContext()).getBean(ProjectService.class);
        //configService.initConfig();
        List<ProjectInfo> list = projectService.listByUserId(1L, "", "", ProjectStatus.open,
                new Pager(1, 10));
        //logger.info("长度："+list.size());
        routingContext.response().end("123\n");
    }
}
