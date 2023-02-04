package com.example.demo1.service.impl;

import com.example.demo1.service.ICheckService;
import com.example.demo1.util.WeChatUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class CheckServiceImpl implements ICheckService {

    private static final String TOKEN_DEFAULT = "get4";
    @Override
    public boolean checkTheSignature(HttpServletRequest request) {
        String signature = request.getParameter("signature");
        System.out.println("signature is :"  + signature);
        String timestamp = request.getParameter("timestamp");
        System.out.println("timestamp is :"  + timestamp);
        String nonce = request.getParameter("nonce");
        System.out.println("nonce is :"  + nonce);
        String sha1Str = WeChatUtil.getSha1Str(new String[]{TOKEN_DEFAULT, timestamp, nonce});
        return signature != null && signature.equals(sha1Str);
    }
}
