package com.eap.project.service;

import com.eap.common.service.BaseService;
import com.eap.common.util.CodeUtil;
import com.eap.project.entity.Stage;
import com.eap.project.entity.Task;
import com.eap.project.mapper.TaskMapper;
import com.eap.project.vo.TaskListVo;
import com.eap.project.vo.TaskVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskService extends BaseService<TaskMapper, Task> {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private StageService stageService;

    public void updateTaskUser(TaskVo taskVo) {
        Map<String, String> taskUser = new HashMap<>();
        taskUser.put("task_id", taskVo.getId());
        taskUser.put("user_id", taskVo.getExecutorId());
        //更新执行者
        taskMapper.updateExecutorTaskUser(taskUser);
        //更新参与者
        taskMapper.deleteTaskUser(taskVo.getId());
        List<Map<String, String>> taskUsers = new ArrayList<>();
        if (null != taskVo.getParticipatorIds() && taskVo.getParticipatorIds().size() != 0) {
            for (String participatorId : taskVo.getParticipatorIds()) {
                Map<String, String> taskUserMap = new HashMap<>();
                taskUserMap.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
                taskUserMap.put("taskId", taskVo.getId());
                taskUserMap.put("userId", participatorId);
                //参与者
                taskUserMap.put("dictType", "2");
                taskUsers.add(taskUserMap);
            }
        }
        if (taskUsers.size() != 0) {
            taskMapper.insertTaskUser(taskUsers);
        }
    }

    /**
     * by llx
     * 通过项目ID找到每个用户参与的任务
     *
     * @param projectId
     * @return
     */
    public List<Map> getCountTaskUserByProjectId(String projectId) {
        return mapper.getCountTaskUserByProjectId(projectId);
    }


    /**
     * by llx
     *
     * @param taskListVo
     * @return
     */
    public List<TaskListVo> getTaskListVoByExample(TaskListVo taskListVo) {
        return mapper.getTaskListVoByExample(taskListVo);
    }

    /**
     * 表示存在这个用户的任务
     *
     * @param userId
     * @return
     */
    public Boolean getTaskByUserId(String userId, String projectId) {
        int count1 = mapper.getTaskByUserId(userId, projectId);
        int count2 = mapper.getTaskByUserId2(userId, projectId);
        return count1 > 0 || count2 > 0;
    }

    public void updateDragTasks(Map<String, String> data) {
        String flag = data.get("flag");
        String stageId = data.get("stageId");
        Stage stage = new Stage();
        stage.setId(stageId);
        Stage stage1 = stageService.selectOne(stage);
        String taskId = data.get("taskId");
        Task task = new Task();
        task.setId(taskId);
        task.setStageId(stageId);
        if ("0".equals(flag)) {
            task.setSort(stage1.getSort() + "000001");
        } else if ("1".equals(flag)) {
            String lastSort = data.get("lastSort");
            task.setSort(stage1.getSort() + CodeUtil.nextCode("", lastSort, 12).substring(6, 12));
        } else if ("2".equals(flag)) {
            String nextSort = data.get("nextSort");
            taskMapper.updateTaskGeSort(nextSort);
            task.setSort(stage1.getSort() + nextSort.substring(6, 12));
        }
        taskService.updateSelectiveById(task);
    }
    /**
     * by llx
     * 根据项目ID查询 每天累计创建任务的数 List
     * @param projectId
     * @return
     */
    public List<Map> getCountDayTaskByProjectId(String projectId){ return mapper.getCountDayTaskByProjectId(projectId); }

    /**
     * by llx
     * 截止times当天累计创建完成的任务数
     * @param projectId
     * @param times
     * @return
     */
    public Integer getCountDayTaskByProjectIdTimes(String projectId,String times){ return mapper.getCountDayTaskByProjectIdTimes(projectId,times); }

    /**
     * by llx
     * 根据项目ID查询 每天累计已完成任务的数 List
     * @param projectId
     * @return
     */
    public List<Map> getCountDayCompTaskByProjectId(String projectId){ return mapper.getCountDayCompTaskByProjectId(projectId); }

    /**
     * by llx
     * 截止times当天累计已完成的任务数
     * @param projectId
     * @param times
     * @return
     */
    public Integer getCountDayCompTaskByProjectIdTimes(String projectId,String times){ return mapper.getCountDayCompTaskByProjectIdTimes(projectId,times); }


    /**
     * by llx
     * 获取项目待认领的任务数
     * @param projectId
     * @param times
     * @return
     */
    public Integer  getCountDairenlingByProjectId(String projectId){return mapper.getCountDairenlingByProjectId(projectId);}

    /**
     * by llx
     * 获取项目已完成的任务数
     * @param projectId
     * @param times
     * @return
     */
    public Integer  getCountFinishedByProjectId(String projectId){return mapper.getCountFinishedByProjectId(projectId);}

    /**
     * by llx
     * 获取项目未完成的任务数
     * @param projectId
     * @param times
     * @return
     */
    public Integer  getCountUnFinishedByProjectId(String projectId){return mapper.getCountUnFinishedByProjectId(projectId);}
}
