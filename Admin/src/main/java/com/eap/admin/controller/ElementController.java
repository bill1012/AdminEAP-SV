package com.eap.admin.controller;

import com.eap.admin.entity.Element;
import com.eap.admin.service.ElementService;
import com.eap.common.controller.BaseController;
import com.eap.common.response.TableResultResponse;
import com.eap.common.util.Query;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-10-16
 * @description 菜单关联资源
 */
@RestController
@RequestMapping(value = "/element")
public class ElementController extends BaseController<ElementService,Element> {

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<Element> list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);
        return baseService.selectByQueryAll(query);
    }
}
