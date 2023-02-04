package com.example.demo1.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo1.service.GoodService;
import com.example.demo1.service.IProcessMessageService;
import com.example.demo1.vo.*;
import com.example.demo1.util.WeChatUtil;
import com.thoughtworks.xstream.XStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class ProcessMessageService implements IProcessMessageService, Closeable {
    @Autowired
    private GoodService goodService;

    @Autowired
    private TokenService tokenService;

    // todo 后续需要对这些变量进行统一管理
    private static final String template_id = "ZAATqG2rOyi7WiScFtfHhm2f08xCYc6CdtI-S_LiBVE";
    private static final String template_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={ACCESS_TOKEN}";

    @Override
    public String returnRspXml(HttpServletRequest request) throws IOException {
        Map<String, String> requestMsgMap = WeChatUtil.xml2Map(request.getInputStream());
        System.out.println("************************************");
        System.out.println("the request msg is :" + requestMsgMap);
        System.out.println("************************************");
        BaseResponseMsg responseMsg = null;
        // 1.获取MsgType
        String msgType = requestMsgMap.get("MsgType");
        switch (msgType) {
            case "text":
                // 2.根据MsgType生成对应的ResponseMsg vo对象
                responseMsg = new TextResponseMsg(requestMsgMap);
                // 3.调用一定的方法获取自动回复的话术，填充至vo中
                // TODO: 2023/1/27 核心方法
                dealTxtMessage(requestMsgMap, responseMsg);
                break;
            case "voice":
                break;
            case "image":
                responseMsg = new ArticleResponseMsg(requestMsgMap);
                List<Article> articles = new ArrayList<>();
                articles.add(new Article("标题1", "这是一个简单的图文消息！", requestMsgMap.get("PicUrl"), "http://www.baidu.com"));
                ((ArticleResponseMsg)responseMsg).setArticles(articles);
                ((ArticleResponseMsg)responseMsg).setArticleCount(articles.size());
                break;
            default:
                break;
        }
        // 4.利用Xstream第三方jar包将vo转换成xmlStr返回
        XStream xStream = new XStream();
        // 增加断言控制
        assert responseMsg != null;
        xStream.processAnnotations(responseMsg.getClass());
        System.out.println("this is : \n" + xStream.toXML(responseMsg));

        return xStream.toXML(responseMsg);
    }

    private void dealTxtMessage(Map<String, String> requestMsgMap, BaseResponseMsg responseMsg) {
        String contentFromUser = (String) ((Map<?, ?>) requestMsgMap).get("Content");
        if ("查询全部".equals(contentFromUser.trim())) {
            List<Good> goods = goodService.getAllGoods();
//                    StringBuilder allGoodsString = new StringBuilder("<![CDATA[");
            StringBuilder allGoodsString = new StringBuilder();
            for (int i = 0; i < goods.size(); i++) {
                Good good = goods.get(i);
                allGoodsString.append(good.getGood_name());
                if (i != goods.size() - 1) {
                    allGoodsString.append(",\n");
                }
            }
//                    allGoodsString.append("]]>");
            ((TextResponseMsg) responseMsg).setContent(allGoodsString.toString());
        } else if (contentFromUser.startsWith("添加：")) {
            // 添加：苹果|20230301|1
            String[] addStrArray = contentFromUser.split("：");
            String addStr = addStrArray[addStrArray.length - 1];
            String[] goodStrArray = addStr.split("\\|");
            Good good = new Good();
            good.setGood_name(goodStrArray[0]);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                good.setDeadline(sdf.parse(goodStrArray[1]));
            } catch (ParseException e) {
            }
            good.setNumbers(Integer.parseInt(goodStrArray[2]));
            if (this.goodService.addGood(good) == 1) {
                ((TextResponseMsg) responseMsg).setContent("添加成功哟！");
            } else {
                ((TextResponseMsg) responseMsg).setContent("添加失败了！");
            }
        } else if ("查询清单".equals(contentFromUser.trim())) {
            // 如果是查询清单，则调用接口,推送模板
            // 1.准备数据以及Token
            List<Good> goods = goodService.getAllGoods();
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
            map.put("touser", requestMsgMap.get("FromUserName"));
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
            System.out.println("the json is " + jsonObject.toJSONString());
            String true_url = template_url.replace("{ACCESS_TOKEN}", this.tokenService.getToken());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            String resultStr = new RestTemplate().postForObject(true_url,
                    new HttpEntity<>(jsonObject, headers), String.class);
            System.out.println("the return string is " + resultStr);
            ((TextResponseMsg) responseMsg).setContent("loading...");
        } else {
            ((TextResponseMsg) responseMsg).setContent("hello World!");
        }
    }

    @Override
    public void processMessage(HttpServletRequest request, HttpServletResponse response) {
        // 1.调用自身方法返回xmlStr
        response.setCharacterEncoding("utf-8");
        String responseXmlStr = null;
        try {
            responseXmlStr = this.returnRspXml(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 2.将responseXmlStr写入response中
        try (PrintWriter out = response.getWriter();) {
            out.print(responseXmlStr);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
    }
}
