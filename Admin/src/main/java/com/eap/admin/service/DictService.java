package com.eap.admin.service;

import com.eap.admin.entity.Dict;
import com.eap.admin.mapper.DictMapper;
import com.eap.common.service.BaseService;
import com.eap.common.util.CodeUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class DictService extends BaseService<DictMapper,Dict> {

    public List<Map> getTree(Dict org) {
        List<Map> orgs = this.getDictByPIDToMap(org.getParentId());
        return this.getTrees(orgs);
    }

    /**
     * 删除组织机构
     * @param id
     */
    public Boolean deleteById(String id) {
        //是否有子集的机构
        List<Map> maps = this.getDictByPIDToMap(id);
        if (maps.size()>0){
            return false;
        }else {
            mapper.deleteByPrimaryKey(id);
            return true;
        }
    }

    /**
     * 根据一级节点获取树
     * @param orgs
     * @return
     */
    public List<Map> getTrees(List<Map> orgs){
        for (Map o: orgs ) {
            List<Map> orgByPID = this.getDictByPIDToMap(o.get("id").toString());
            if (orgByPID.size()>0){
                o.put("children",orgByPID);
                getTrees(orgByPID);
            }
        }
        return  orgs;
    }
    /**
     * 根据父id，获取列表数据 返回List<map>
     * @param pid
     * @return
     */
    public List<Map> getDictByPIDToMap(String pid){
        return mapper.getDictByPIDToMap(pid);
    }


    /**
     * 字典code获取该值ID作为父ID,根据父id，获取列表数据 返回List<map>
     * @param code
     * @return
     */
    public List<Map> getDictByCodeToMap(String code){
        return mapper.getDictByCodeToMap(code);
    }

    public String getNextLevelCode(String parentId,String levelCode) {
        String lCode = mapper.getLevelCodeByParentId(parentId);
        return CodeUtil.getNextLevelCode(levelCode,lCode);
    }

    public Boolean checkCode(String id, String code) {
        Dict org = new Dict();
        org.setCode(code);
        org.setId(id);
        List<Map> maps = mapper.checkCode(org);
        if (maps.size()>0){
            return false;
        }
        return true;
    }
}
