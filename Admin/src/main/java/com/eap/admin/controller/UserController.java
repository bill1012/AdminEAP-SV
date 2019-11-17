package com.eap.admin.controller;

import com.eap.admin.entity.User;
import com.eap.admin.mapper.UserMapper;
import com.eap.admin.service.ElementService;
import com.eap.admin.service.MenuService;
import com.eap.admin.service.RoleService;
import com.eap.admin.service.UserService;
import com.eap.common.constant.UserConstant;
import com.eap.common.controller.BaseController;
import com.eap.common.jwt.SecurityContextHolder;
import com.eap.common.response.ObjectRestResponse;
import com.eap.common.response.TableResultResponse;
import com.eap.common.util.Query;
import com.eap.common.vo.PermissionInfo;
import com.eap.common.vo.UserInfo;
import com.eap.project.entity.Task;
import com.eap.project.mapper.TaskMapper;
import io.undertow.util.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-9-26
 */
@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController<UserService, User> {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ElementService elementService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskMapper taskMapper;

    @RequestMapping(value = "/username/{username}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public UserInfo getUserByUsername(@PathVariable("username") String username) {
        return baseService.getUserInfoByUsername(username);
    }

    @GetMapping(value = "/{username}/roles")
    @ResponseBody
    public List<String> getRoleCodesByUsername(@PathVariable("username") String username) {
        User user = baseService.getUserByUsername(username);
        List<String> roleCodes = new ArrayList<>();
        if (user != null) {
            roleCodes = roleService.getMapper().selectRoleCodesByUserId(user.getId());
        }
        return roleCodes;
    }

    @GetMapping(value = "/{username}/permissions")
    @ResponseBody
    List<PermissionInfo> getPermissionByUsername(@PathVariable("username") String username) {
        return baseService.getPermissionByUsername(username);
    }

    @GetMapping(value = "/permissions")
    @ResponseBody
    List<PermissionInfo> getAllPermissionInfo() {
        return baseService.getAllPermissionInfo();

    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<User> list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        return baseService.selectByName(query);
    }

    /**
     * 当前项目下所有的人员
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/getUsersByProjectId", method = RequestMethod.GET)
    public ObjectRestResponse getUsersByProjectId(String projectId) {
        List<User> list = userMapper.getUsersByProjectId(projectId);
        return new ObjectRestResponse<List<User>>().rel(true).data(list);
    }

    @RequestMapping(value = "/getUsersMapByProjectId", method = RequestMethod.GET)
    public ObjectRestResponse getUsersMapByProjectId(String projectId, String type) {
        // type为1，map存id，name
        // type为2，map存id，header
        List<User> list = userMapper.getUsersByProjectId(projectId);
        Map<String, String> map = new HashMap<>();
        if ("1".equals(type)) {
            for (User user : list) {
                map.put(user.getId(), user.getName());
            }
        }
        if ("2".equals(type)) {
            for (User user : list) {
                map.put(user.getId(), user.getHeader());
            }
        }
        return new ObjectRestResponse<>().rel(true).data(map);
    }

    /**
     * 一个项目下所有的任务id对应的执行人id
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/getTaskIdExecutorId", method = RequestMethod.GET)
    public ObjectRestResponse getTaskIdExecutorId(String projectId) {
        Task task = new Task();
        task.setProjectId(projectId);
        List<Task> tasks = taskMapper.select(task);
        Map<String, String> map = new HashMap<>();
        for (Task task1 : tasks) {
            String taskExecutorIdById = taskMapper.getTaskExecutorIdById(task1);
            map.put(task1.getId(), taskExecutorIdById);
        }
        return new ObjectRestResponse().rel(true).data(map);
    }

    /**
     * 分页查询不存在的项目成员关系
     *
     * @param params
     * @return
     */
    @GetMapping(value = "/userPageNo")
    public TableResultResponse<User> getNoProUser(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        return baseService.selectByNoPro(query);
    }

    /**
     * 分页查询已存在的项目成员关系
     *
     * @param params
     * @return
     */
    @GetMapping(value = "/userPage")
    public TableResultResponse<User> getProUser(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        return baseService.selectByProId(query);
    }

    /**
     * 不分页查询已存在的项目成员关系
     * @return
     */
    @GetMapping(value = "/getUserBoolean")
    public ObjectRestResponse<Boolean> getUserNoPage(String projectId,String userId) {
        Integer user = baseService.selectByProjectId(projectId,userId);
        return new ObjectRestResponse<List<User>>().rel(true).data(user == 0?false:true);
    }
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    /**
     * @description: 修改用户密码
     * @param: userid,pass,oldpass
     * @return: ResponseEntity
     */
    @RequestMapping(value = "/updatepwd",method = RequestMethod.GET)
    public ObjectRestResponse updatePassword(String userid, String pass,  String oldpass){
        User user = baseService.selectById(userid);
        if (!encoder.matches(oldpass, user.getPassword())) {
            return new ObjectRestResponse().rel(true).data(false);
        }
        int result = baseService.updatePassword(userid,pass);
        return new ObjectRestResponse().rel(true).data(result > 0?true:false);
    }

}
