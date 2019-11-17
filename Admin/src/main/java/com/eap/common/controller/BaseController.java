package com.eap.common.controller;

import com.eap.common.constant.BaseConstants;
import com.eap.common.response.ObjectRestResponse;
import com.eap.common.response.TableResultResponse;
import com.eap.common.service.BaseService;
import com.eap.common.util.Query;
import com.eap.common.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class BaseController<Service extends BaseService, Entity> {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected Service baseService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Entity> add(@RequestBody Entity entity) {
        baseService.insertSelective(entity);
        return new ObjectRestResponse<Entity>().rel(true);
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Entity> get(@PathVariable String id) {
        return new ObjectRestResponse<Entity>().rel(true).data(baseService.selectById(id));
    }

    @RequestMapping(value = "/put/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<Entity> update(@RequestBody Entity entity) {
        baseService.updateSelectiveById(entity);
        return new ObjectRestResponse<Entity>().rel(true);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ObjectRestResponse<Entity> remove(@PathVariable String id) {
        baseService.deleteById(id);
        return new ObjectRestResponse<Entity>().rel(true);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public List<Entity> all() {
        return baseService.selectListAll();
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<Entity> page(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        return baseService.selectByQuery(query);
    }


    protected String getCurrentUserName() {
        Object userName=request.getSession().getAttribute(BaseConstants.REQUEST_HEADER_UNAME);
        return userName==null?null:userName.toString();

    }

    protected String getCurrentUserId() {
        Object userId=request.getSession().getAttribute(BaseConstants.REQUEST_HEADER_UID);
        return userId==null?null:userId.toString();
    }

    protected UserInfo getUserInfo(){
        return (UserInfo)request.getSession().getAttribute(BaseConstants.REQUEST_HEADER_USER);
    }
}
