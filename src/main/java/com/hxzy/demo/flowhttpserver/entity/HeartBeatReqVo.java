package com.hxzy.demo.flowhttpserver.entity;


import lombok.Data;


@Data
public class HeartBeatReqVo {

    // 设备序列号
    private String sn;

    // 上传时间，Unix时间戳,秒
    private Long time;

}
