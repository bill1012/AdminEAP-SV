package com.eap.project.controller;

import com.eap.common.controller.BaseController;
import com.eap.common.response.ObjectRestResponse;
import com.eap.common.util.CodeUtil;
import com.eap.common.util.JsonUtil;
import com.eap.common.util.Query;
import com.eap.project.entity.Record;
import com.eap.project.entity.Stage;
import com.eap.project.entity.Task;
import com.eap.project.mapper.RecordMapper;
import com.eap.project.mapper.StageMapper;
import com.eap.project.mapper.TaskMapper;
import com.eap.project.service.TaskService;
import com.eap.project.vo.TaskListVo;
import com.eap.project.vo.TaskVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@RestController
@RequestMapping("/task")
public class TaskController extends BaseController<TaskService, Task> {
    @Autowired
    private StageMapper stageMapper;
    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RecordMapper recordMapper;

    @RequestMapping(value = "/addTask", method = RequestMethod.POST)
    public ObjectRestResponse<Task> addTask(@RequestBody TaskVo entity) {
        Task task = new Task();
        String sort = taskMapper.getSortByStageId(entity.getStageId());
        String nextCode = CodeUtil.nextCode("", sort, 12);
        entity.setSort(nextCode);
        BeanUtils.copyProperties(entity, task);
        baseService.insert(task);
        String executorId = entity.getExecutorId();
        List<String> participatorIds = entity.getParticipatorIds();
        List<Map<String, String>> taskUsers = new ArrayList<>();
        Map<String, String> taskUser = new HashMap<>();
        taskUser.put("id",UUID.randomUUID().toString().replaceAll("-", ""));
        taskUser.put("taskId", task.getId());
        taskUser.put("userId", executorId);
        //执行者
        taskUser.put("dictType", "1");
        taskUsers.add(taskUser);
        if (null != participatorIds && participatorIds.size() != 0) {
            for (String participatorId: participatorIds) {
                Map<String, String> taskUserMap = new HashMap<>();
                taskUserMap.put("id",UUID.randomUUID().toString().replaceAll("-", ""));
                taskUserMap.put("taskId", task.getId());
                taskUserMap.put("userId", participatorId);
                //参与者
                taskUserMap.put("dictType", "2");
                taskUsers.add(taskUserMap);
            }
        }
        taskMapper.insertTaskUser(taskUsers);
        Record record = new Record();
        record.setTaskId(task.getId());
        record.setContent("创建了任务:" + task.getName());
        record.setUserId(task.getCreateUser());
        record.setCreateTime(new Date());
        recordMapper.insertSelective(record);
        return new ObjectRestResponse<Task>().rel(true).data(task);
    }

    @RequestMapping(value = "getTasksByProjectId", method = RequestMethod.GET)
    public ObjectRestResponse<Map<String, List<Task>>> getTasksByProjectId(String projectId) {
        Stage stage = new Stage();
        stage.setProjectId(projectId);
        List<Stage> stages = stageMapper.select(stage);
        //List<Map<String, List>> maps = new List<Map<String, List>>;
        HashMap map = new HashMap<String, List>();
        for (Stage s : stages) {
            Example example = new Example(Task.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("projectId", projectId);
            criteria.andEqualTo("stageId", s.getId());
            example.setOrderByClause("sort");
            List<Task> tasks = taskMapper.selectByExample(example);
            map.put(s.getId(), tasks);
        }
        return new ObjectRestResponse<Map>().rel(true).data(map);
    }

    @RequestMapping(value = "getTaskVoById",method = RequestMethod.GET)
    public ObjectRestResponse<TaskVo> getTaskById(Task task) {
        Task getTask = taskMapper.selectOne(task);
        TaskVo taskVo = new TaskVo();
        BeanUtils.copyProperties(getTask,taskVo);
        String executorIdById = taskMapper.getTaskExecutorIdById(task);
        List<String> taskParticipatorIds = taskMapper.getTaskParticipatorIds(task);
        taskVo.setExecutorId(executorIdById);
        taskVo.setParticipatorIds(taskParticipatorIds);
        return new ObjectRestResponse<>().rel(true).data(taskVo);
    }

    @RequestMapping(value = "updateTask",method = RequestMethod.PUT)
    public ObjectRestResponse updateTask(@RequestBody TaskVo taskVo){
        Task task = new Task();
        BeanUtils.copyProperties(taskVo,task);
        //更新task
        taskMapper.updateByPrimaryKey(task);
        //更新执行人和参与者
        taskService.updateTaskUser(taskVo);
        return new ObjectRestResponse().rel(true);
    }


    @GetMapping("/getCountTaskUserByProjectId")
    public ObjectRestResponse<List<Map>> getCountTaskUserByProjectId(@RequestParam String projectId){
        List<Map> orgs = baseService.getCountTaskUserByProjectId(projectId);
        return new ObjectRestResponse<List<Map>>().rel(true).data(orgs);
    }

    @GetMapping("/getTaskListVoByExample")
    public ObjectRestResponse getTaskListVoByExample(TaskListVo taskListVo){
        List<TaskListVo> taskListVos = baseService.getTaskListVoByExample(taskListVo);
        String str = JsonUtil.ObjectToJson(taskListVos);
        return new ObjectRestResponse<String>().rel(true).data(str);
    }

    @GetMapping("/getCountDayTaskByProjectId")
    public ObjectRestResponse<List<Map>> getCountDayTaskByProjectId(@RequestParam String projectId){
        List<Map> orgs = baseService.getCountDayTaskByProjectId(projectId);
        return new ObjectRestResponse<List<Map>>().rel(true).data(orgs);
    }

    @GetMapping("/getCountDayTaskByProjectIdTimes")
    public ObjectRestResponse getCountDayTaskByProjectIdTimes(@RequestParam Map<String,Object> params){
        Query query = new Query(params);
        String projectId = (String)query.get("projectId");
        String times = (String)query.get("times");
        Integer res = baseService.getCountDayTaskByProjectIdTimes(projectId,times);
        return new ObjectRestResponse<String>().rel(true).data(res);
    }

    @GetMapping("/getCountDayCompTaskByProjectId")
    public ObjectRestResponse<List<Map>> getCountDayCompTaskByProjectId(@RequestParam String projectId){
        List<Map> orgs = baseService.getCountDayCompTaskByProjectId(projectId);
        return new ObjectRestResponse<List<Map>>().rel(true).data(orgs);
    }

    @GetMapping("/getCountDayCompTaskByProjectIdTimes")
    public ObjectRestResponse getCountDayCompTaskByProjectIdTimes(@RequestParam Map<String,Object> params){
        Query query = new Query(params);
        String projectId = (String)query.get("projectId");
        String times = (String)query.get("times");
        Integer res = baseService.getCountDayCompTaskByProjectIdTimes(projectId,times);
        return new ObjectRestResponse<String>().rel(true).data(res);
    }



    @GetMapping("/getTaskUserId")
    public ObjectRestResponse getTaskUserId(String userId,String projectId){
        Boolean flag = baseService.getTaskByUserId(userId,projectId);
        return new ObjectRestResponse<Integer>().rel(true).data(flag);
    }


    /**
     * 更新拖拽后stage和task的对应关系及顺序
     * @return
     */
    @PutMapping(value = "/updateDragTasks")
    public ObjectRestResponse updateDragTasks(@RequestParam Map<String, String> data) {
        taskService.updateDragTasks(data);
        return new ObjectRestResponse().rel(true);
    }


    @GetMapping("/getCountDairenlingByProjectId")
    public ObjectRestResponse getCountDairenlingByProjectId(String projectId){
        Integer flag = baseService.getCountDairenlingByProjectId(projectId);
        return new ObjectRestResponse<Integer>().rel(true).data(flag);
    }

    @GetMapping("/getCountFinishedByProjectId")
    public ObjectRestResponse getCountFinishedByProjectId(String projectId){
        Integer flag = baseService.getCountFinishedByProjectId(projectId);
        return new ObjectRestResponse<Integer>().rel(true).data(flag);
    }

    @GetMapping("/getCountUnFinishedByProjectId")
    public ObjectRestResponse getCountUnFinishedByProjectId(String projectId){
        Integer flag = baseService.getCountUnFinishedByProjectId(projectId);
        return new ObjectRestResponse<Integer>().rel(true).data(flag);
    }

}
