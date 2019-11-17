package com.eap.admin.service;

import com.eap.admin.entity.Element;
import com.eap.admin.entity.Menu;
import com.eap.admin.entity.User;
import com.eap.admin.mapper.UserMapper;
import com.eap.common.constant.UserConstant;
import com.eap.common.response.TableResultResponse;
import com.eap.common.service.BaseService;
import com.eap.common.util.EntityUtil;
import com.eap.common.util.ImageUtil;
import com.eap.common.util.Query;
import com.eap.common.vo.PermissionInfo;
import com.eap.common.vo.UserInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-9-26
 */
@Service
public class UserService extends BaseService<UserMapper, User> {

    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */

    @Autowired
    private HttpSession session;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ElementService elementService;

    @Autowired
    private RoleService roleService;

    public User getUserByUsername(String username) {
        if (session != null && session.getAttribute(username) != null) {
            return (User) session.getAttribute(username);
        } else {
            User user = new User();
            user.setUsername(username);
            user = this.selectOne(user);
            session.setAttribute(username, user);
            return user;
        }
    }

    public UserInfo getUserInfoByUsername(String username) {
        UserInfo info = new UserInfo();
        User user = this.getUserByUsername(username);
        if (user == null) {
            info.setId(UserConstant.USER_NOT_EXIST);
            return info;
        }
        BeanUtils.copyProperties(user, info);
        info.setId(user.getId().toString());
        return info;
    }


    public void insertSelective(User entity) {
        String password = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(entity.getPassword());
        entity.setPassword(password);
        if (entity.getHeader() == null || "".equals(entity.getHeader())) {
            String name = entity.getName();
            ByteArrayOutputStream out = null;
            try {
                out = ImageUtil.generateImgOutputStream(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BASE64Encoder base64Encoder = new BASE64Encoder();
            out.toByteArray();
            String encode = base64Encoder.encode(out.toByteArray()).trim();
            encode = encode.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n

            entity.setHeader("data:image/png;base64," + encode);
        }
        super.insertSelective(entity);
        this.insertOrgUser(entity);
    }

    public TableResultResponse<User> selectByName(Query query) {
        Example example = new Example(User.class);
        Example.Criteria criteria1 = example.createCriteria();
        Example.Criteria criteria2 = example.createCriteria();
        if (query.get("name") != null) {
            criteria1.andLike("name", "%" + query.get("name").toString() + "%");
            criteria2.andLike("username", "%" + query.get("name").toString() + "%");
        }
        example.or(criteria2);
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<User> list = mapper.selectByExample(example);
        return new TableResultResponse<>(result.getTotal(), list);
    }

    public TableResultResponse<User> selectUsersWithRoleId(Query query) {
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<User> list = mapper.selectUsersWithRoleId(query.get("roleId").toString(),
                query.get("name") != null ? query.get("name").toString() : null);
        return new TableResultResponse<>(result.getTotal(), list);
    }

    public TableResultResponse<User> selectUsersWithoutRoleId(Query query) {
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<User> list = mapper.selectUsersWithoutRoleId(query.get("roleId").toString(),
                query.get("name") != null ? query.get("name").toString() : null);
        return new TableResultResponse<>(result.getTotal(), list);
    }

    public User selectById(Object id) {
        return mapper.selectUserById(id);
    }

    /**
     * 覆写更新方法
     *
     * @param entity
     */
    public void updateSelectiveById(User entity) {
        EntityUtil.setUpdatedInfo(entity);
        mapper.updateByPrimaryKeySelective(entity);
        //修改组织机构
        mapper.deleteRlOrgByUid(entity.getId());
        this.insertOrgUser(entity);

    }


    public void insertOrgUser(User entity) {
        // 插入中间表数据
        Map<String, String> map = new HashMap<>();
        String[] split = entity.getCorgId().split(",");
        if (split.length > 0) {
            map.put("userId", entity.getId());
            for (int i = 0; i < split.length; i++) {
                map.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
                map.put("orgId", split[i].trim());
                mapper.insertOrgUser(map);
            }
        }
    }


    public List<PermissionInfo> getPermissionByUsername(String username) {
        User user = this.getUserByUsername(username);
        List<Menu> menus = menuService.getMapper().selectMenusByUserId(user.getId());
        List<PermissionInfo> permissionInfos = new ArrayList<>();
        menuService.menuToPermissionInfo(menus, permissionInfos);
        List<Element> elements = elementService.getMapper().selectElementsByUserId(user.getId());
        elementService.elementToPermissionInfo(elements, permissionInfos);
        return permissionInfos;
    }

    public List<String> getRoleCodesByUsername(String username) {
        User user = this.getUserByUsername(username);
        List<String> roleCodes = new ArrayList<>();
        if (user != null) {
            roleCodes = roleService.getMapper().selectRoleCodesByUserId(user.getId());
        }
        return roleCodes;
    }

    public List<PermissionInfo> getAllPermissionInfo() {
        List<PermissionInfo> permissionInfos = new ArrayList<>();
        Example example = new Example(Menu.class);
        example.createCriteria().andEqualTo("enabled", "1");
        List<Menu> menus = menuService.selectByExample(example);
        menuService.menuToPermissionInfo(menus, permissionInfos);

        List<Element> elements = elementService.selectListAll();
        elementService.elementToPermissionInfo(elements, permissionInfos);
        return permissionInfos;
    }

    /**
     * 通过projectId查询此项目成员
     *
     * @param query
     * @return
     */
    public TableResultResponse<User> selectByProId(Query query) {
        String projectId = (String) query.get("projectId");
        String name = (String) query.get("name");
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<User> list = mapper.selectByProId(projectId, name);
        return new TableResultResponse<>(result.getTotal(), list);
    }

    /**
     * 通过projectId查询非此项目成员
     *
     * @param query
     * @return
     */
    public TableResultResponse<User> selectByNoPro(Query query) {
        String name = (String) query.get("name");
        String projectId = (String) query.get("projectId");
        List<User> list = mapper.selectUserNoPro(projectId, name);
        List<User> list1 = mapper.selectUsersByProjectIdN(projectId, name);
        list.addAll(list1);
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        return new TableResultResponse<>(result.getTotal(), list);
    }

    /**
     * 通过projectId查询此项目成员 --- 不分页
     */
    public Integer selectByProjectId(String projectId, String userId) {
        return mapper.selectByProjectId(projectId, userId);
    }

    public Integer updatePassword(String userid,String pass){
        User user = new User();
        user.setId(userid);
        user.setPassword(new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(pass));
        return mapper.updateByPrimaryKeySelective(user);
    }
}
