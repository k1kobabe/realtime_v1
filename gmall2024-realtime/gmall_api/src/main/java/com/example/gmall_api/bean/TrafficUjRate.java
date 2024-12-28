package com.example.gmall_api.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrafficUjRate {
    // 渠道
    String ch;
    // 跳出率
    Double ujRate;
}
