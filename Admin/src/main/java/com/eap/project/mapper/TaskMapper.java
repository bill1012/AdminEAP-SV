package com.eap.project.mapper;

import com.eap.project.entity.Task;
import com.eap.project.vo.TaskListVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface TaskMapper extends Mapper<Task> {
     void insertTaskUser(List<Map<String,String>> taskUsers);

     String getTaskExecutorIdById(Task task);

     List<String> getTaskParticipatorIds(Task task);

     int updateExecutorTaskUser(Map<String, String> taskUser);

     int deleteTaskUser(String taskId);

     int deleteByProjectId(String projectId);

     int deleteTaskUserById(String taskId);

     List<Map> getCountTaskUserByProjectId(String projectId);

     List<TaskListVo> getTaskListVoByExample(TaskListVo taskListVo);

     /**
      * by llx
      * 根据项目ID查询 每天累计创建任务的数 List
      * @param projectId
      * @return
      */
     List<Map> getCountDayTaskByProjectId(String projectId);

     /**
      * by llx
      * 截止times当天累计创建完成的任务数
      * @param projectId
      * @param times
      * @return
      */
     int getCountDayTaskByProjectIdTimes(@Param("projectId") String projectId, @Param("times") String times);

     /**
      * by llx
      * 根据项目ID查询 每天累计已完成任务的数 List
      * @param projectId
      * @return
      */
     List<Map> getCountDayCompTaskByProjectId(String projectId);

     /**
      * by llx
      * 截止times当天累计已完成的任务数
      * @param projectId
      * @param times
      * @return
      */
     int getCountDayCompTaskByProjectIdTimes(@Param("projectId") String projectId, @Param("times") String times);

     int getTaskByUserId(@Param("userId") String userId,@Param("projectId")String projectId);

     String getSortByStageId(String stageId);

     int getTaskByUserId2(@Param("userId") String userId,@Param("projectId")String projectId);

     void updateTaskGeSort(String sort);


     /**
      * by llx
      * 获取项目待认领的任务数
      * @param projectId
      * @param times
      * @return
      */
     int getCountDairenlingByProjectId(@Param("projectId") String projectId);



     /**
      * by llx
      * 获取项目已完成的任务数
      * @param projectId
      * @param times
      * @return
      */
     int getCountFinishedByProjectId(@Param("projectId") String projectId);

     /**
      * by llx
      * 获取项目未完成的任务数
      * @param projectId
      * @param times
      * @return
      */
     int getCountUnFinishedByProjectId(@Param("projectId") String projectId);

}
