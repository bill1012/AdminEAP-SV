package com.eap.admin.service;

import com.eap.admin.entity.Menu;
import com.eap.admin.entity.Role;
import com.eap.admin.mapper.MenuMapper;
import com.eap.admin.mapper.RoleMapper;
import com.eap.common.constant.CommonConstant;
import com.eap.common.service.BaseService;
import com.eap.common.util.CodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-10-17
 */
@Service
public class RoleService extends BaseService<RoleMapper, Role> {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public Role selectById(Object id) {
        Role role = super.selectById(id);
        if (!role.getParentId().equals(CommonConstant.ROOT)) {
            Role parentRole = super.selectById(role.getParentId());
            role.setParentName(parentRole.getName());
        } else {
            role.setParentName("根节点");
        }
        return role;
    }


    public String getNextCode(String parentId) {
        String maxCode = this.mapper.selectMaxLevelCodeByParentId(parentId);
        String prefix = "";
        if (!parentId.equals(CommonConstant.ROOT)) {
            Role role = super.selectById(parentId);
            prefix = role.getLevelcode();
        }
        return CodeUtil.nextCode(prefix, maxCode, 6);
    }


    public void deleteRoleUser(String roleId, String userIds) {
        if (StringUtils.isEmpty(userIds))
            return;
        String[] idArr = userIds.split(",");
        for (String userId : idArr) {
            this.mapper.deleteRoleUser(roleId, userId);
        }
    }

    public void insertRoleUsers(String roleId, String userIds) {
        if (StringUtils.isEmpty(userIds))
            return;
        String[] idArr = userIds.split(",");
        for (String userId : idArr) {
            this.mapper.insertRoleUser(roleId, userId);
        }
    }

    public List<String> getElementsByRoleId(String roleId) {
        return this.mapper.selectResourceByRoleIdAndType(roleId, CommonConstant.RESOURCE_TYPE_ELEMENT);
    }

    public void deleteResourceElement(String roleId, String elementId) {
        this.mapper.deleteByRoleIdAndResourceId(roleId, elementId);
    }

    public void addResourceElement(String roleId, String menuId, String elementId) {
        this.mapper.insertResourceElement(roleId, elementId, menuId, CommonConstant.RESOURCE_TYPE_ELEMENT);
    }

    public void modifyResourceElement(String roleId, String menuId,String elementIds){
        this.mapper.deleteByRoleIdAndParentId(roleId,menuId);
        if(StringUtils.isNotEmpty(elementIds)){
            this.mapper.insertResourceElements(roleId,elementIds.split(","),menuId,CommonConstant.RESOURCE_TYPE_ELEMENT);
        }
    }

    public Object getMenusByRoleId(String roleId) {
        return this.mapper.selectResourceByRoleIdAndType(roleId,CommonConstant.RESOURCE_TYPE_MENU);
    }

    public void modifyResourceMenu(String roleId, String[] menuIds) {
        this.mapper.deleteByRoleIdAndResourceType(roleId,CommonConstant.RESOURCE_TYPE_MENU);

        if(menuIds==null)
            return;

        //关联上父菜单
        List<Menu> menuList = menuMapper.selectAll();
        Map<String,String> map = new HashMap<>();
        for(Menu menu:menuList){
            map.put(menu.getId().toString(),menu.getParentId().toString());
        }
        Set<String> relationMenus = new HashSet<String>();
        relationMenus.addAll(Arrays.asList(menuIds));
        for(String menuId:menuIds){
            findParentID(map,relationMenus,menuId);
        }

        String[] menuArr=new String[relationMenus.size()];
        int i=0;
        for (String relationMenu : relationMenus) {
            menuArr[i++]=relationMenu;
        }

        this.mapper.insertResourceMenus(roleId,menuArr,CommonConstant.RESOURCE_TYPE_MENU);
    }


    private void findParentID(Map<String,String> map, Set<String> relationMenus, String id){
        String parentId = map.get(id);
        if(String.valueOf(CommonConstant.ROOT).equals(parentId)){
            return ;
        }
        relationMenus.add(parentId);
        findParentID(map,relationMenus,parentId);
    }
}
