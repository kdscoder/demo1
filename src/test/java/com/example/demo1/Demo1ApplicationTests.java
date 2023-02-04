package com.example.demo1;

import com.example.demo1.service.GoodService;
import com.example.demo1.service.impl.TokenService;
import com.example.demo1.vo.*;
import com.thoughtworks.xstream.XStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class Demo1ApplicationTests {
	@Autowired
	GoodService goodService;

	@Autowired
	TokenService tokenService;
	@Test
	void contextLoads() {
/*		Map a = new HashMap<>();
		a.put("ToUserName","123");
		a.put("FromUserName","123");
		a.put("MsgType","123");
		a.put("PicUrl","123");
		BaseResponseMsg msg = new ArticleResponseMsg(a);
		List<Article> articles = new ArrayList<>();
		articles.add(new Article("123","123","123123","asdd"));
		((ArticleResponseMsg)msg).setArticles(articles);
		((ArticleResponseMsg) msg).setArticleCount(1);
		XStream xStream = new XStream();
		xStream.processAnnotations(msg.getClass());
		System.out.println(xStream.toXML(msg));*/
//		String str3 = tokenService.getToken();
	}

}
