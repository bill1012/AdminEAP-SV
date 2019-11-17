package com.eap.project.controller;

import com.eap.common.controller.BaseController;
import com.eap.common.response.ObjectRestResponse;
import com.eap.project.entity.Record;
import com.eap.project.mapper.RecordMapper;
import com.eap.project.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController extends BaseController<RecordService, Record> {
    @Autowired
    private RecordMapper recordMapper;
    @RequestMapping(value = "/getRecordsByTaskId",method = RequestMethod.GET)
    public ObjectRestResponse getRecordsByTaskId(String taskId){
        Example example=new Example(Record.class);
        example.createCriteria().andEqualTo("taskId", taskId);
        example.setOrderByClause("create_time");
        List<Record> records = recordMapper.selectByExample(example);
        return new ObjectRestResponse().rel(true).data(records);
    }
}
