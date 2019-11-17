package com.eap.admin.controller;

import com.eap.admin.entity.User;
import com.eap.admin.service.RoleService;
import com.eap.admin.service.UserService;
import com.eap.common.controller.BaseController;
import com.eap.common.response.ObjectRestResponse;
import com.eap.common.response.TableResultResponse;
import com.eap.common.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-10-17
 */
@RestController
@RequestMapping(value = "/role/user")
public class RoleUserController extends BaseController<UserService, User> {

    @Autowired
    private RoleService roleService;

    /**
     * 关联角色的用户列表
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<User> list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        return baseService.selectUsersWithRoleId(query);
    }

    /**
     * 待选择的用户列表（未关联角色)
     */
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<User> select(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        return baseService.selectUsersWithoutRoleId(query);
    }


    @RequestMapping(value = "/{roleId}/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public ObjectRestResponse<Object> deleteRoleUser(@PathVariable String roleId, String userIds) {
        roleService.deleteRoleUser(roleId,userIds);
        return new ObjectRestResponse<>().rel(true);
    }


    @RequestMapping(value = "/{roleId}/add", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Object> addRoleUsers(@PathVariable String roleId, String userIds) {
        roleService.insertRoleUsers(roleId, userIds);
        return new ObjectRestResponse<>().rel(true);
    }
}
