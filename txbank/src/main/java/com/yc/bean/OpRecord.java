package com.yc.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OpRecord {
    private int id;
    private int accountid;
    private double opmoney;
    private String optime;   //数据库是datetime ,在java中转为String
    private OpType opType;   //OpType类型
    private Integer transferid;
}
