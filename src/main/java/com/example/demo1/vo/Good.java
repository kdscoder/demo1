package com.example.demo1.vo;

import lombok.Data;

import java.util.Date;

@Data
public class Good {
    private int id;
    private Date deadline;
    private int numbers;
    private String good_name;

}
