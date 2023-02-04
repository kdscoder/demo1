package com.example.demo1.mapper;

import com.example.demo1.vo.Good;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodMapper {
    List<Good> selectAllGoods();
    int addGood(Good good);
}
