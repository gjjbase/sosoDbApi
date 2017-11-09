package com.dev.proj.service.impl;

import com.dev.base.enums.ProjectStatus;
import com.dev.base.jdbc.datasource.DataSourceContextHolder;
import com.dev.base.test.BaseTest;
import com.dev.base.util.Pager;
import com.dev.proj.entity.Project;
import com.dev.proj.service.ProjectService;
import com.dev.proj.service.ResfulService;
import com.dev.proj.vo.ProjectInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class ProjectServiceImplTest extends BaseTest {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private ResfulService resfulService;

    @Test
    public void testListByUserId() {
        List<ProjectInfo> list = projectService.listByUserId(1L, "", "", ProjectStatus.open,
                new Pager(1, 10));
        System.out.println(list);
    }
    @Test
    public void resfuTest(){
        DataSourceContextHolder.setCurrent("bonecpDataSource");
        List<Map> maps = resfulService.selectAllTableMsg();
        System.out.println("resful:"+maps);
        DataSourceContextHolder.setCurrent("apiDataSource");
        List<Map> maps2 = resfulService.selectAllTableMsg();
        System.out.println("resful:"+maps2);
    }

    @Test
    public void testAdd() {
        for (int i = 1; i < 101; i++) {
            Project project = new Project();
            project.setCode("code" + i);
            project.setName("test" + i);
            project.setStatus(ProjectStatus.open);
            project.setUserId(3L);

            projectService.add(project);
        }
    }
}
