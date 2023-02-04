package com.example.demo1.service;

import com.example.demo1.mapper.GoodMapper;
import com.example.demo1.vo.Good;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodService {
    @Autowired
    private GoodMapper goodMapper;

    /**
     *
     * @return
     */
    public List<Good> getAllGoods() {
        return goodMapper.selectAllGoods();
    }

    public int addGood(Good good) {
        return goodMapper.addGood(good);
    }
}
