package com.eap.admin.mapper;

import com.eap.admin.entity.Dict;
import com.eap.admin.entity.Org;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface DictMapper extends Mapper<Dict> {


    List<Map> getDictByPIDToMap(String pid);

    String getLevelCodeByParentId(String parentId);

    List<Map> checkCode(Dict org);

    List<Map> getDictByCodeToMap(String code);
}