package com.eap.project.controller;

import com.eap.common.response.ObjectRestResponse;
import com.eap.project.entity.Project;
import com.eap.project.mapper.ProjectMapper;
import com.eap.project.service.ProjectService;
import com.eap.project.vo.ProjectVo;
import com.eap.common.controller.BaseController;
import com.eap.common.response.TableResultResponse;
import com.eap.common.util.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * Created with IntelliJ IDEA.
 * Description:
 * User: silence
 * Date: 2019-05-29
 * Time: 上午10:45
 */
@RestController
@RequestMapping("/project")
public class ProjectController extends BaseController<ProjectService, Project> {

    @GetMapping("list")
    public TableResultResponse<ProjectVo> list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        return baseService.selectByName(query);
    }

    @PostMapping(value = "/addObj")
    public ObjectRestResponse<Project> add(@RequestBody Project data) {
        String id =  baseService.insertSelectiveProject(data);
        return new ObjectRestResponse<Project>().rel(true).data(id);
    }

    @PostMapping(value = "/addProjectUser")
    public ObjectRestResponse<Project> addProUser(@RequestBody ProjectVo entity) {
        baseService.addProUser(entity);
        return new ObjectRestResponse<Project>().rel(true);
    }

    @DeleteMapping(value = "delProUser")
    public  ObjectRestResponse<Project> delProUser(String projectId,String userId){
        Boolean flag = baseService.getCreateProjectUser(userId,projectId);
        if(flag){
            return new ObjectRestResponse<Project>().rel(true).data(flag);
        }
        baseService.delProUser(projectId,userId);
        return new ObjectRestResponse<Project>().rel(true);
    }

    @GetMapping("/projectsByUser")
    public ObjectRestResponse<Project> getProjectsByUserId(@RequestParam String userId){
        List<Map> orgs = baseService.getProjectsByUserId(userId);
        return new ObjectRestResponse<List<Map>>().rel(true).data(orgs);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<Project> update(@RequestBody Project entity) {
        Project project =  baseService.updateProjectById(entity);
        return new ObjectRestResponse<Project>().rel(true).data(project);
    }

}
