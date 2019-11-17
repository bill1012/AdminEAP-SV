package com.eap.project.service;

import com.eap.admin.entity.User;
import com.eap.admin.mapper.UserMapper;
import com.eap.common.util.CodeUtil;
import com.eap.common.util.EntityUtil;
import com.eap.project.entity.Project;
import com.eap.project.entity.Stage;
import com.eap.project.entity.Task;
import com.eap.project.mapper.ProjectMapper;
import com.eap.project.mapper.StageMapper;
import com.eap.project.mapper.TaskMapper;
import com.eap.project.vo.ProjectVo;
import com.eap.common.response.TableResultResponse;
import com.eap.common.service.BaseService;
import com.eap.common.util.Query;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/***
 * Created with IntelliJ IDEA.
 * Description:
 * User: silence
 * Date: 2019-05-29
 * Time: 上午10:41
 */
@Service
public class ProjectService extends BaseService<ProjectMapper, Project> {

    @Autowired
    private StageMapper stageMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 项目分页
     * @param query
     * @return
     */
    public TableResultResponse<ProjectVo> selectByName(Query query) {
        ProjectVo project = new ProjectVo();
        if (!StringUtils.isBlank((String)query.get("name"))) {
            project.setName("%" + query.get("name").toString() + "%");
        }
        if (!StringUtils.isBlank((String)query.get("dictType"))) {
            project.setDictType(query.get("dictType").toString());
        }
        if (!StringUtils.isBlank((String)query.get("dictStatus"))) {
            project.setDictStatus(query.get("dictStatus").toString());
        }
        project.setQueryUserId(query.get("userId").toString());
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<ProjectVo> list = mapper.selectVoByExample(project);
        return new TableResultResponse<>(result.getTotal(), list);
    }

    /**
     * 创建项目同时创建stage状态
     * @param entity
     */
    @Transactional
    public String insertSelectiveProject(Project entity) {
        entity.setCreateTime(new Date());
        String pId = UUID.randomUUID().toString().replaceAll("-","");
        entity.setId(pId);
        mapper.insertSelective(entity);
        Map<String,Object> map = new HashMap<>();
        map.put("userId",entity.getCreateUser());
        map.put("projectId",entity.getId());
        map.put("createTime",new Date());
        map.put("createUser",entity.getCreateUser());
        map.put("deleted","N");
        String id = UUID.randomUUID().toString().replaceAll("-","");
        map.put("id",id);
        mapper.insertProjectUser(map);
        List<String> list = new LinkedList<>();
        list.add("待处理");
        list.add("进行中");
        list.add("待审核");
        list.add("已完成");
        String sort = "000000";
        for(String s : list){
            sort = CodeUtil.nextCode("", sort, 6);
            Stage stage = new Stage();
            stage.setProjectId(entity.getId());
            stage.setName(s);
            stage.setCreateTime(new Date());
            stage.setCreateUser(entity.getCreateUser());
            stage.setSort(sort);

            stageMapper.insertSelective(stage);
        }
        return pId;
    }

    /**
     * 删除项目同时删除stage和任务和项目成员（逻辑删除）
     * @param id
     */
    @Transactional
    public void deleteById(Object id) {
        mapper.deleteByPrimaryKey(id);
        mapper.deleteUserByProject((String)id);
        stageMapper.deleteByProjectId((String)id);
        Task task = new Task();
        task.setProjectId((String)id);
        List<Task> taskList = taskMapper.select(task);
        for(Task t : taskList){
            taskMapper.deleteTaskUserById(t.getId());
        }
        taskMapper.deleteByProjectId((String)id);
    }

    /**
     * 添加用户项目中间表
     * @param projectVo
     */
    @Transactional
    public void addProUser(ProjectVo projectVo){
        List<Map> list = new ArrayList<>();
        if(CollectionUtils.isEmpty(projectVo.getList())){
            return;
        }
        List<User> users = new ArrayList<>();
        for(User u : projectVo.getList()){
            User user = userMapper.selectUserByUProId(projectVo.getId(),u.getId());
            if(user!=null){
                users.add(u);
                mapper.updateProUser(projectVo.getId(),user.getId());
            }
        }
        projectVo.getList().removeAll(users);
        for(User user : projectVo.getList()){
            Map map = new HashMap();
            map.put("projectId",projectVo.getId());
            map.put("createUser",projectVo.getCreateUser());
            map.put("createTime",new Date());
            map.put("deleted","N");
            map.put("userId",user.getId());
            map.put("id",UUID.randomUUID().toString().replaceAll("-",""));
            list.add(map);
        }
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        mapper.insertProUser(list);
    }

    /**
     * 删除用户项目中间表
     * @param projectId
     * @param userId
     */
    public void delProUser(String projectId,String userId){
        mapper.delProUser(projectId,userId);
    }

    /**
     * 通过用户ID找到用户参与的项目
     * @param userId
     * @return
     */
    public List<Map> getProjectsByUserId(String userId){ return mapper.getProjectsByUserId(userId); }

    public Project updateProjectById(Project entity) {
        EntityUtil.setUpdatedInfo(entity);
        mapper.updateByPrimaryKeySelective(entity);
        return  mapper.selectByPrimaryKey(entity.getId());
    }

    /**
     * 根据userId和projectId查询是否是此项目的创建人
     * @param userId
     * @param projectId
     * @return
     */
    public Boolean getCreateProjectUser(String userId,String projectId){
        Project project = mapper.selectByPrimaryKey(projectId);
        return userId.equals(project.getCreateUser());
    }

}
