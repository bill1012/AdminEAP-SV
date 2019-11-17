package com.eap.admin.controller;

import com.eap.admin.entity.Role;
import com.eap.admin.service.RoleService;
import com.eap.common.controller.BaseController;
import com.eap.common.response.ObjectRestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-10-19
 * 角色资源api
 */
@RestController
@RequestMapping(value="/role/resource")
public class RoleResourceController extends BaseController<RoleService,Role> {

    @GetMapping(value = "/{roleId}/element")
    @ResponseBody
    public ObjectRestResponse<List<String>> getElementsByRoleId(@PathVariable String roleId){
        return new ObjectRestResponse().data(baseService.getElementsByRoleId(roleId)).rel(true);
    }

    @RequestMapping(value = "/{roleId}/element/add", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse addResourceElement(@PathVariable  String roleId, String menuId,String elementId){
        baseService.addResourceElement(roleId, menuId,elementId);
        return new ObjectRestResponse().rel(true);
    }



    @RequestMapping(value = "/{roleId}/element/delete", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse deleteResourceElement(@PathVariable String roleId,String elementId){
        baseService.deleteResourceElement(roleId,elementId);
        return new ObjectRestResponse().rel(true);
    }


    @RequestMapping(value = "/{roleId}/element/modify", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse modifyResourceElement(@PathVariable  String roleId, String menuId,String elementIds){
        baseService.modifyResourceElement(roleId, menuId,elementIds);
        return new ObjectRestResponse().rel(true);
    }

    @GetMapping(value="/{roleId}/menu")
    @ResponseBody
    public ObjectRestResponse<List<String>> getResourceMenu(@PathVariable String roleId ){
        return new ObjectRestResponse().data(baseService.getMenusByRoleId(roleId)).rel(true);
    }


    @PostMapping(value = "/{roleId}/menu")
    @ResponseBody
    public ObjectRestResponse modifyResourceMenu(@PathVariable  String roleId, String menuIds){
        String[] menus=null;
        if(StringUtils.isNotBlank(menuIds)) {
             menus = menuIds.split(",");
        }
        baseService.modifyResourceMenu(roleId, menus);
        return new ObjectRestResponse().rel(true);
    }

}
