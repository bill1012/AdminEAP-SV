package com.eap.admin.controller;

import com.eap.admin.entity.Role;
import com.eap.admin.service.RoleService;
import com.eap.admin.vo.RoleTree;
import com.eap.common.constant.CommonConstant;
import com.eap.common.controller.BaseController;
import com.eap.common.util.TreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-10-17
 */
@RestController
@RequestMapping(value="/role")
public class RoleController extends BaseController<RoleService,Role> {

    @GetMapping(value = "/list")
    @ResponseBody
    public List<Role> list(String name){
        Example example=new Example(Role.class);
        if(StringUtils.isNotBlank(name)){
            example.createCriteria().andLike("name","%"+name+"%");
        }
        return baseService.selectByExample(example);
    }

    @GetMapping(value = "/tree")
    @ResponseBody
    public List<RoleTree> getTree(String name) {
        Example example = new Example(Role.class);
        if (StringUtils.isNotBlank(name))
            example.createCriteria().andLike("name", "%" + name + "%");
        example.setOrderByClause("levelcode asc");
        return getRoleTree(baseService.selectByExample(example), CommonConstant.ROOT);
    }

    @GetMapping(value = "/getNextCode")
    @ResponseBody
    public String getNextCode(String parentId){
        return baseService.getNextCode(parentId);
    }

    //判断编码的唯一性
    @GetMapping(value="/checkCode")
    @ResponseBody
    public Boolean checkCode(String code,String id){
        if(StringUtils.isBlank(code)){
            return true;
        }else if(StringUtils.isNotBlank(id)&&!id.equals(CommonConstant.ROOT)){
            //编辑时校验
            Example example =new Example(Role.class);
            example.createCriteria().andEqualTo("code",code).andNotEqualTo("id",id);
            List<Role> exists=baseService.selectByExample(example);
            if(exists.isEmpty())
                return true;
            else
                return false;
        }else{
            //新增时校验
            Example example =new Example(Role.class);
            example.createCriteria().andEqualTo("code",code);
            List<Role> exists=baseService.selectByExample(example);
            if(exists.isEmpty()){
                return true;
            }else{
                return false;
            }
        }
    }

    private List<RoleTree> getRoleTree(List<Role> roles,String rootId){
        List<RoleTree> roleTrees=new ArrayList<>();
        RoleTree node=null;
        for (Role role : roles) {
            node=new RoleTree();
            BeanUtils.copyProperties(role,node);
            roleTrees.add(node);
        }
        return TreeUtil.build(roleTrees,rootId);
    }

    @GetMapping(value="/system")
    @ResponseBody
    public List<Role> getSystem(){
        Role role=new Role();
        role.setParentId(CommonConstant.ROOT);
        return baseService.selectList(role);
    }

    @GetMapping(value="/roleTree")
    @ResponseBody
    public List<RoleTree> listRole(String parentId) {
        try {
            if (parentId == null) {
                parentId = this.getSystem().get(0).getId();
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
        Example example = new Example(Role.class);
        Role parent = baseService.selectById(parentId);
        example.createCriteria().andLike("levelcode", parent.getLevelcode() + "%").
                andNotEqualTo("id",parent.getId());
        return getRoleTree(baseService.selectByExample(example), parent.getId());
    }

}
