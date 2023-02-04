package com.example.demo1.util;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import javax.servlet.ServletInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeChatUtil {
    private static final String DIGEST_SHA1 = "sha1";
    private static final char[] CHAR_MAPPING =  {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 根据params生成sha1字符串
     * @param params
     * @return
     */
    public static String getSha1Str(String[] params) {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.sort(params);
        for (String str :params) {
            stringBuilder.append(str);
        }
        String orgStr = stringBuilder.toString();
        // 清空这个字符串
        stringBuilder.delete(0, stringBuilder.length());
        try {
            MessageDigest sha1 = MessageDigest.getInstance("sha1");
            byte[] digest = sha1.digest(orgStr.getBytes());
            // 对其进行处理,遍历byte数组，对每一个数组的高4位向右移动4bit,然后与0x00001111进行与运算
            for (byte b : digest) {
                stringBuilder.append(CHAR_MAPPING[(b>>4)&15]);
                stringBuilder.append(CHAR_MAPPING[b&15]);
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 利用SAXReader解析xml字符串，并将其转为map返回
     * @param is
     * @return
     */
    public static Map<String, String> xml2Map(ServletInputStream is) {
        SAXReader saxReader = new SAXReader();
        Map<String, String> requestMap = new HashMap<>(16);
        try {
            Document document = saxReader.read(is);
            Element root = document.getRootElement();
            List elements =  root.elements();
            for (Object o: elements) {
                Element e = (Element)o;
                requestMap.put(e.getName(), e.getStringValue());
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return requestMap;
    }
}
