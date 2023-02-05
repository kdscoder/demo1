package com.example.demo1.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo1.service.impl.IMessagePushService;
import com.example.demo1.service.impl.TokenService;
import com.example.demo1.vo.Good;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessagePushService implements IMessagePushService {
    @Autowired
    GoodService goodService;
    @Autowired
    TokenService tokenService;
    private static final String template_id = "ZAATqG2rOyi7WiScFtfHhm2f08xCYc6CdtI-S_LiBVE";
    private static final String template_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={ACCESS_TOKEN}";
    @Override
    public boolean pushMessage() {
        List<Good> goods = this.goodService.getAllGoods();
        // todo 后续需要把模板生成的动作给封装起来
        StringBuilder good_names = new StringBuilder();
        StringBuilder numbers = new StringBuilder();
        StringBuilder deadlines = new StringBuilder();
        for (int i = 0; i < goods.size(); i++) {
            Good good = goods.get(i);
            good_names.append(good.getGood_name()).append(",");
            numbers.append(good.getNumbers()).append(",");
            deadlines.append(good.getDeadline()).append(",");
        }
        String good_names_str = good_names.toString();
        String numbers_str = numbers.toString();
        String deadlines_str = deadlines.toString();
        // 2.拼装数据组成Map,再转化成json
        Map map = new HashMap<>(16);
        // 此处先将touser写死，后续再调用用户列表接口获取所有关注的用户
//        map.put("touser", requestMsgMap.get("FromUserName"));
        map.put("touser", "okgxh56uuzT2EpixiQYAvrkUyoSM");
        map.put("template_id", template_id);
        map.put("topcolor", "#FF0000");
        Map dataMap = new HashMap<>(16);
        Map good_names_map = new HashMap();
        good_names_map.put("value",good_names_str);
        good_names_map.put("color","#173177");
        Map numbers_map = new HashMap();
        numbers_map.put("value",numbers_str);
        numbers_map.put("color","#173177");
        Map deadlines_map = new HashMap();
        deadlines_map.put("value",deadlines_str);
        deadlines_map.put("color","#173177");
        dataMap.put("good_names",good_names_map);
        dataMap.put("numbers",numbers_map);
        dataMap.put("deadlines",deadlines_map);
        map.put("data", dataMap);
        JSONObject jsonObject = new JSONObject(map);
        System.out.println("the json is :" + jsonObject.toJSONString());
        String true_url = template_url.replace("{ACCESS_TOKEN}", this.tokenService.getToken());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        String resultStr = new RestTemplate().postForObject(true_url,
                new HttpEntity<>(jsonObject, headers), String.class);
        assert resultStr != null;
        return resultStr.toLowerCase().contains("success");
    }
}
