package com.eap.project.vo;

import com.eap.project.entity.Task;

import java.util.List;

public class TaskVo extends Task {
    //执行者
    private String executorId;

    private List<String> participatorIds;

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public List<String> getParticipatorIds() {
        return participatorIds;
    }

    public void setParticipatorIds(List<String> participatorIds) {
        this.participatorIds = participatorIds;
    }
}
