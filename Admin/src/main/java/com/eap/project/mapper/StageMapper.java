package com.eap.project.mapper;

import com.eap.project.entity.Stage;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface StageMapper extends Mapper<Stage> {

    int deleteByProjectId(String projectId);

    String getMaxStageSortByProjectId(String projectId);

    List<Stage> getStagesOrderBySort(Stage stage);


}
