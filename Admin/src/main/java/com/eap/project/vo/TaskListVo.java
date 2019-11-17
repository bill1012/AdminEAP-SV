package com.eap.project.vo;

import com.eap.project.entity.Task;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Auther: sharps
 * @Date: 19-6-12 21:39
 * @Description:
 */
public class TaskListVo extends Task {

    private String userName;

    private String stageName;

    private String header;

    private String userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String endTimeStr;

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
