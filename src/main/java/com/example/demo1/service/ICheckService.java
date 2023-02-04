package com.example.demo1.service;

import javax.servlet.http.HttpServletRequest;

public interface ICheckService {
    boolean checkTheSignature(HttpServletRequest request);
}
