package com.eap.admin.controller;

import com.eap.admin.entity.Dict;
import com.eap.admin.entity.Org;
import com.eap.admin.service.DictService;
import com.eap.common.controller.BaseController;
import com.eap.common.response.ObjectRestResponse;
import com.eap.common.util.JsonUtil;
import com.eap.common.util.PinYingUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/dict")
public class DictController extends BaseController<DictService, Dict> {


    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<Dict> add(@RequestBody Dict dict){
        baseService.insertSelective(setSpellAndInital(dict));
        return new ObjectRestResponse<Dict>().rel(true).data(dict);
    }
    @RequestMapping(value = "/put/{id}",method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<Dict> update(@RequestBody Dict dict){
        baseService.updateSelectiveById(this.setSpellAndInital(dict));
        return new ObjectRestResponse<Dict>().rel(true).data(dict);
    }

    @GetMapping("dictTree")
    public ObjectRestResponse orgTree(Dict org){
        List<Map> orgs = baseService.getTree(org);
        String str = JsonUtil.ObjectToJson(orgs).replaceAll("name","label");
        return new ObjectRestResponse<String>().rel(true).data(str);
    }

    @GetMapping("dictSon")
    public ObjectRestResponse orgSon(String code){
        List<Map> orgs = baseService.getDictByCodeToMap(code);
        //String str = JsonUtil.ObjectToJson(orgs).replaceAll("name","label");
        return new ObjectRestResponse<List<Map>>().rel(true).data(orgs);
    }

    @GetMapping(value = "/getNextCode")
    public ObjectRestResponse getNextCode(String parentId,String levelCode){
        String res = baseService.getNextLevelCode(parentId,levelCode);
        return new ObjectRestResponse<String>().rel(true).data(res);
    }

    @GetMapping(value = "/checkCode")
    public Boolean checkCode(String id , String code){
        Boolean res = baseService.checkCode(id,code);
        return res;
    }

    @RequestMapping(value = "/deleted",method = RequestMethod.DELETE)
    public Boolean deleted(String id){
        Boolean res = baseService.deleteById(id);
        return res;
    }

    public  Dict setSpellAndInital(Dict dict){
        String spell = PinYingUtil.convertHanzi2Pinyin(dict.getName(), true);
        String inital = PinYingUtil.convertHanzi2Pinyin(dict.getName(),false);
        dict.setLspell(spell);
        dict.setUspell(spell.toUpperCase());
        dict.setInitials(inital+"_"+inital.toUpperCase());
        return dict;
    }
}
