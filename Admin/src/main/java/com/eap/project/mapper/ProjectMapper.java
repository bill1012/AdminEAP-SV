package com.eap.project.mapper;

import com.eap.project.entity.Project;
import com.eap.project.vo.ProjectVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface ProjectMapper extends Mapper<Project> {

    List<ProjectVo> selectVoByExample(ProjectVo project);

    int deleteUserByProject(String id);

    int insertProUser(@Param("list")List<Map> list);

    int delProUser(@Param("projectId")String projectId,@Param("userId")String userId);

    int updateProUser(@Param("projectId")String projectId,@Param("userId")String userId);

    int insertProjectUser(@Param("map")Map<String,Object> map);

    List<Map> getProjectsByUserId(String userId);
}
