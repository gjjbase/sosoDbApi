package com.dev.proj.controller;

import com.dev.base.controller.BaseController;
import com.dev.base.enums.*;
import com.dev.base.exception.ValidateException;
import com.dev.base.exception.code.ErrorCode;
import com.dev.base.jdbc.datasource.DataSourceContextHolder;
import com.dev.base.json.JsonUtils;
import com.dev.base.util.Pager;
import com.dev.base.util.WebPaginate;
import com.dev.base.util.WebUtil;
import com.dev.base.utils.DateUtil;
import com.dev.base.utils.MapUtils;
import com.dev.base.utils.ValidateUtils;
import com.dev.doc.entity.ApiDoc;
import com.dev.doc.entity.Inter;
import com.dev.doc.entity.InterParam;
import com.dev.doc.entity.Module;
import com.dev.doc.service.*;
import com.dev.proj.entity.Project;
import com.dev.proj.service.ProjectMemberService;
import com.dev.proj.service.ProjectService;
import com.dev.proj.service.ResfulService;
import com.dev.proj.vo.ProjectInfo;
import com.dev.user.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * <p>Title: 项目相关</p>
 * <p>Description: 描述（简要描述类的职责、实现方式、使用注意事项等）</p>
 * <p>CreateDate: 2015年7月11日下午5:39:29</p>
 */
@Controller
@RequestMapping("/auth/proj")
public class ProjController extends BaseController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private CopyService copyService;

    @Autowired
    private ApiDocService apiDocService;
    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ResfulService resfulService;

    @Autowired
    private InterService interService;

    @Autowired
    private InterParamService interParamService;
    /**
     * @name 新增项目信息
     * @Description
     * @CreateDate 2015年8月6日下午5:14:18
     */
    @RequestMapping(value = "/json/add.htm", method = RequestMethod.POST)
    public
    @ResponseBody
    Map add(HttpServletRequest request, Project project) {
        ValidateUtils.notNull(project.getCode(), ErrorCode.SYS_001, "项目编码不能为空");

        Long userId = getUserId(request);
        projectService.add(userId, project);

        //重新加载项目权限
        reloadProjAuth(request);

        return JsonUtils.createSuccess();
    }

    /**
     * @name 新增项目信息
     * @Description
     * @CreateDate 2015年8月6日下午5:14:18
     */
    @RequestMapping(value = "/json/copy.htm", method = RequestMethod.POST)
    public
    @ResponseBody
    Map copy(HttpServletRequest request, Long projId) {
        ValidateUtils.notNull(projId, ErrorCode.SYS_001, "项目id不能为空");

        UserInfo userInfo = getUserInfo(request);
        ProjectInfo projectInfo = copyService.copyProj(userInfo.getUserId(), projId);
        //更新当前登陆用户缓存信息
        if (projectInfo != null) {
            userInfo.getProjMap().put(projectInfo.getProjId(), Role.admin);
            userInfo.getDocMap().put(projectInfo.getDocId(), Role.admin);
            saveUserInfo(request, userInfo);
        }

        return JsonUtils.createSuccess();
    }

    /**
     * @name 更新项目信息
     * @Description
     * @CreateDate 2015年8月6日下午5:14:18
     */
    @RequestMapping(value = "/json/update.htm", method = RequestMethod.POST)
    public
    @ResponseBody
    Map update(HttpServletRequest request, Project project, Long projId) {
        ValidateUtils.notNull(projId, ErrorCode.SYS_001, "项目id不能为空");

        project.setId(projId);
        projectService.update(project);

        return JsonUtils.createSuccess();
    }

    /**
     * @name 删除项目信息
     * @Description
     * @CreateDate 2015年8月6日下午5:14:18
     */
    @RequestMapping(value = "/json/del.htm")
    public
    @ResponseBody
    Map update(HttpServletRequest request, Long projId) {
        ValidateUtils.notNull(projId, ErrorCode.SYS_001, "项目id不能为空");

        projectService.deleteById(projId);

        return JsonUtils.createSuccess();
    }

    /**
     * @name 退出项目
     * @Description
     * @CreateDate 2015年8月22日下午2:07:39
     */
    @RequestMapping("/json/quit.htm")
    public
    @ResponseBody
    Map quit(HttpServletRequest request, Long projId) {
        ValidateUtils.notNull(projId, ErrorCode.SYS_001, "项目id不能为空");

        Long userId = getUserId(request);
        projectMemberService.quit(userId, projId);

        return JsonUtils.createSuccess();
    }

    /**
     * @name 项目列表
     * @Description
     * @CreateDate 2015年7月11日下午2:05:24
     */
    @RequestMapping("/list.htm")
    public String list(HttpServletRequest request, Model model, String code, String name,
                       String status, Integer pageNumber, Integer pageSize) {
        Long userId = getUserId(request);

        ProjectStatus projectStatus = null;
        if (!StringUtils.isEmpty(status)) {
            projectStatus = ProjectStatus.valueOf(status);
        }

        Pager pager = new Pager(pageNumber, pageSize);
        List<ProjectInfo> list = projectService.listByUserId(userId, code, name, projectStatus, pager);
        int count = projectService.countByUserId(userId, code, name, projectStatus);
        pager.setTotalCount(count);
        pager.setList(list);

        model.addAttribute("pager", pager);
        model.addAttribute("paginate", WebPaginate.build(request, pageNumber, pageSize, count));

        return "project/projList";
    }

    /**
     * @name 项目基本信息
     * @Description
     * @CreateDate 2015年7月11日下午2:05:24
     */
    @RequestMapping("/info.htm")
    public String getInfo(HttpServletRequest request, Long projId, Model model) {
        ValidateUtils.notNull(projId, ErrorCode.SYS_001, "项目id不能为空");

        Long userId = getUserId(request);
        ProjectInfo projectInfo = projectService.getInfo(userId, projId);
        model.addAttribute("projInfo", projectInfo);

        Map<Long, ProjectInfo> sessionMap = MapUtils.newMap();
        sessionMap.put(projId, projectInfo);
        WebUtil.setSessionAttr(request, "projTempMap", sessionMap);

        return "project/projInfo";
    }

    /**
     * @name 导入swagger json创建新项目
     * @Description
     * @CreateDate 2015年10月15日下午11:03:06
     */
    @RequestMapping(value = "/upload.htm", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> upload(HttpServletRequest request, @RequestParam(value = "file") MultipartFile file) {
        ValidateUtils.isTrue(file.getOriginalFilename().endsWith(".json"), ErrorCode.DOC_002);
        Map<String, Object> swaggerInfo = null;
        try {
            swaggerInfo = JsonUtils.toObject(new String(file.getBytes(), "UTF-8"), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidateException(ErrorCode.DOC_002);
        }

        Long userId = getUserId(request);
        projectService.upload(userId, swaggerInfo);

        return JsonUtils.createSuccess();
    }


    @RequestMapping(value = "/init.htm")
    public
    @ResponseBody
    Map init(HttpServletRequest request) {
        //如果已经存在为DB_NAME的project,则不初始化
        Project isproject = projectService.selectByCode(DB_NAME);
        if (isproject!=null){
            return JsonUtils.createSuccess("项目已经初始化");
        }

        Long userId = getUserId(request);
        Project project = new Project();
        //创建时间

        //yyyy-MM-dd HH:mm:ss格式的时间
        String s = DateUtil.formatNowByLong();
        Date createData = DateUtil.parseByLong(s);
        project.setCreateDate(createData);
        //修改时间
        project.setModifyDate(createData);
        //创建人ID
        project.setUserId(userId);
        //项目编码 数据库名
        project.setCode(DB_NAME);
        //项目名称 数据库描述
        project.setName(DB_DESC);
        //项目描述
        project.setName(DB_DESC);
        //项目状态
        project.setStatus(ProjectStatus.close);
        projectService.add(userId, project);
        reloadProjAuth(request);
        //projectService.
        //项目创建成功，开始初始化文档信息
        ApiDoc apiDoc = apiDocService.selectByCreatData(DateUtil.formatByLong(createData));
        Long docId = apiDoc.getId();
        //文档标题
        apiDoc.setTitle(DB_DESC);
        //访问主机
        apiDoc.setHost("localhost:8081");
        //接口基路径
        apiDoc.setBasePath("/dbapi/"+DB_NAME);
        //文档版本
        apiDoc.setVersion("V1.0");
        //文档说明
        //是否公开
        apiDoc.setPub(true);
        //是否发布
        apiDoc.setOpen(true);
        apiDocService.update(apiDoc);

        //文档信息初始化完成，开始初始化模块信息
        //为每张表生成一个默认模块
        //获取到所有的数据表名
        DataSourceContextHolder.setCurrent(DB_SOURCE);
        List<Map> maps = resfulService.selectAllTableMsg();
        DataSourceContextHolder.setCurrent("bonecpDataSource");
        int idx = 0;
        for (Map map:
             maps) {
            //表名
            String tableName = map.get("tableName").toString();
            //表注释
            String tableComment=map.get("tableComment").toString();
            //根据数据表名成一个模块
            Module module = new Module();
            module.setCreateDate(createData);
            module.setModifyDate(createData);
            module.setDocId(docId);
            module.setName(tableName);
            module.setDescription(tableComment);
            module.setSortWeight(idx);
            moduleService.add(module);
            Long moduleId = moduleService.selectByName(tableName).getId();


            //模块初始化完成，生成一个获取所有的数据的方法
            Inter interlist = new Inter();
            interlist.setCreateDate(createData);
            interlist.setModifyDate(createData);
            interlist.setDocId(docId);
            interlist.setModuleId(moduleId);
            interlist.setName("select"+tableName.replace("_","")+"List");
            interlist.setPath("/"+tableName);
            interlist.setMethod(ReqMethod.GET);
            interlist.setScheme(ReqScheme.HTTP);
            interlist.setConsume("application/json");
            interlist.setProduce("application/json");
            interlist.setDescription(tableComment);
            interlist.setSummary("获取数据库所有的json数据");
            interlist.setSortWeight(idx);
            interService.add(interlist);
            idx+=50;
            //获取所有的数据的方法完成，生成一个根据ID获取数据的方法
            //根据表名获取主键名
            DataSourceContextHolder. setCurrent(DB_SOURCE);
            List<Map> tables = resfulService.selectPrikByTName(tableName);
            String columnName = tables.get(0).get("columnName").toString();
            DataSourceContextHolder. setCurrent("bonecpDataSource");

            Inter interbyId = new Inter();
            interbyId.setCreateDate(createData);
            interbyId.setModifyDate(createData);
            interbyId.setDocId(docId);
            interbyId.setModuleId(moduleId);
            String interName="select"+tableName.replace("_","")+"ListBy"+columnName.replace("_","");
            interbyId.setName(interName);
            interbyId.setPath("/"+tableName+"/{"+columnName+"}");
            interbyId.setMethod(ReqMethod.GET);
            interbyId.setScheme(ReqScheme.HTTP);
            interbyId.setConsume("application/json");
            interbyId.setProduce("application/json");
            interbyId.setDescription(tableComment);
            interbyId.setSummary("根据主键获取数据库的json数据");
            interbyId.setSortWeight(idx);

            interService.add(interbyId);
            //向参数表中追加数据
            //获取inter的ID
            Inter inter = interService.selectByName(interName);
            Long interId = inter.getId();
            Map<String,String> mapByid=new HashMap<>();
            mapByid.put("code",columnName);
            mapByid.put("description","");
            mapByid.put("position","path");
            mapByid.put("type","sys_string");
            mapByid.put("defValue","");
            mapByid.put("required","true");
            mapByid.put("refSchemaId",null);
            mapByid.put("extSchema","");
            List<Map<String,String>> paramList = new ArrayList<>();
            paramList.add(mapByid);

            List<InterParam> interParamList = parseParamList(docId,interId, paramList);
            interParamService.batchAdd(docId,interId, interParamList);

        }

        return JsonUtils.createSuccess("初始化完成");
    }
    //解析请求参数列表
    private List<InterParam> parseParamList(Long docId,Long interId,List<Map<String,String>> paramMapList){
        List<InterParam> result = new ArrayList<InterParam>();
        String code;
        String refSchemaIdStr;
        InterParam interParam = null;
        for (Map<String,String> paramMap : paramMapList) {
            code = (String)paramMap.get("code");

            if (StringUtils.isEmpty(code)) {//参数编码非空
                continue ;
            }

            interParam = new InterParam();
            interParam.setCode(code);
            interParam.setName(paramMap.get("name"));
            interParam.setDefValue(paramMap.get("defValue"));
            interParam.setDescription(paramMap.get("description"));
            interParam.setInterId(interId);
            interParam.setDocId(docId);
            interParam.setPosition(ParamPosition.valueOf(paramMap.get("position")));
            interParam.setRequired(Boolean.parseBoolean(paramMap.get("required")));
            interParam.setType(SchemaType.valueOf(paramMap.get("type")));
            interParam.setExtSchema(paramMap.get("extSchema"));

            refSchemaIdStr = paramMap.get("refSchemaId");
            if (!StringUtils.isEmpty(refSchemaIdStr)) {
                interParam.setRefSchemaId(Long.parseLong(refSchemaIdStr));
            }

            result.add(interParam);
        }

        return result;
    }
    public static String DB_NAME = null;
    public static String DB_DESC = null;
    public static String DB_SOURCE=null;
    static{
        InputStream in = null;
        try{
            Properties properties = new Properties();
            in =  ProjController.class.getClassLoader().getResourceAsStream("api-cfg.properties");
            properties.load(new InputStreamReader(in, "utf-8"));
            DB_NAME = properties.getProperty("DB_NAME");
            DB_DESC = properties.getProperty("DB_DESC");
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

}
