package com.eap.project.controller;

import com.eap.common.controller.BaseController;
import com.eap.common.response.ObjectRestResponse;
import com.eap.common.util.CodeUtil;
import com.eap.project.entity.Stage;
import com.eap.project.mapper.StageMapper;
import com.eap.project.service.StageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stage")
public class StageController extends BaseController<StageService, Stage> {
    @Autowired
    private StageMapper stageMapper;

    @Autowired
    private StageService stageService;

    @RequestMapping(value = "/getStagesByProjectId", method = RequestMethod.GET)
    public ObjectRestResponse getStagesByProjectId(Stage stage) {
        List<Stage> stages = stageMapper.getStagesOrderBySort(stage);
        return new ObjectRestResponse<List<Stage>>().rel(true).data(stages);
    }

    @RequestMapping(value = "/addStage", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Stage> add(@RequestBody Stage entity) {
        String  sort = stageMapper.getMaxStageSortByProjectId(entity.getProjectId());
        String nextSort = CodeUtil.nextCode("", sort, 6);
        entity.setSort(nextSort);
        stageService.insertSelective(entity);
        return new ObjectRestResponse<Stage>().rel(true).data(entity);
    }
}
