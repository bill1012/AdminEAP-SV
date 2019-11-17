package com.eap.admin.controller;

import com.eap.admin.entity.Org;
import com.eap.admin.service.OrgService;
import com.eap.common.controller.BaseController;
import com.eap.common.response.ObjectRestResponse;
import com.eap.common.util.JsonUtil;
import com.eap.common.util.PinYingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/org")
public class OrgController extends BaseController<OrgService, Org> {

    @Autowired
    private OrgService orgService;

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Org> add(@RequestBody Org org){
        String spell = PinYingUtil.convertHanzi2Pinyin(org.getName(), true);
        String inital = PinYingUtil.convertHanzi2Pinyin(org.getName(),false);
        org.setLspell(spell);
        org.setUspell(spell.toUpperCase());
        org.setInitials(inital+"_"+inital.toUpperCase());
        baseService.insertSelective(org);
        return new ObjectRestResponse<Org>().rel(true);
    }
    @RequestMapping(value = "/put/{id}",method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<Org> update(@RequestBody Org org){
        String spell = PinYingUtil.convertHanzi2Pinyin(org.getName(), true);
        String inital = PinYingUtil.convertHanzi2Pinyin(org.getName(),false);
        org.setLspell(spell);
        org.setUspell(spell.toUpperCase());
        org.setInitials(inital+"_"+inital.toUpperCase());
        baseService.updateSelectiveById(org);
        return new ObjectRestResponse<Org>().rel(true);
    }

    @GetMapping("orgTree")
    public ObjectRestResponse orgTree(Org org){
        List<Map> orgs = baseService.getTree(org);
        String str = JsonUtil.ObjectToJson(orgs).replaceAll("name","label");
        return new ObjectRestResponse<String>().rel(true).data(str);
    }

    @GetMapping(value = "/getNextCode")
    public ObjectRestResponse getNextCode(String parentId,String levelCode){
        String res = orgService.getNextLevelCode(parentId,levelCode);
        return new ObjectRestResponse<String>().rel(true).data(res);
    }

    @GetMapping(value = "/checkCode")
    public Boolean checkCode(String id , String code){
        Boolean res = orgService.checkCode(id,code);
        return res;
    }

    @RequestMapping(value = "/deleted",method = RequestMethod.DELETE)
    public Boolean deleted(String id){
        Boolean res = orgService.deleteById(id);
        return res;
    }


}
