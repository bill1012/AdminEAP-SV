package com.eap.admin.mapper;

import com.eap.admin.entity.Org;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface OrgMapper extends Mapper<Org> {


    List<Map> getOrgByPIDToMap(String pid);

    String getLevelCodeByParentId(String parentId);

    List<Map> checkCode(Org org);
}