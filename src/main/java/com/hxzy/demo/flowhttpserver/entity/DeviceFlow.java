package com.hxzy.demo.flowhttpserver.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeviceFlow implements Serializable {

    // 设备序列号
    private String sn;

    // 进客流
    private Long in;

    // 出客流
    private Long out;

    /** 经过人数*/
    private Long passby;

    /** 折返人数*/
    private Long turnback;

    /** 当前上传时间段内的进入成人人数*/
    private Long inAdult;

    /** 当前上传时间段内的离开成人人数*/
    private Long outAdult;

    /** 当前上传时间段内的经过成人人数*/
    private Long passbyAdult;

    /** 当前上传时间段内的折返成人人数*/
    private Long turnbackAdult;

    /** 当前上传时间段内的进入儿童人数*/
    private Long inChild;

    /** 当前上传时间段内的离开儿童人数*/
    private Long outChild;

    /** 当前上传时间段内的经过儿童人数*/
    private Long passbyChild;

    /** 当前上传时间段内的折返儿童人数*/
    private Long turnbackChild;

    /** 统计开始时间，Unix时间戳,秒 */
    private String startFlow;

    /** 统计结束时间，Unix时间戳,秒 */
    private String endFlow;

    /** 平均逗留时间*/
    private String avgStayTime;

    /**  检测线ID */
    private Long regionId;

    /**  员工进人数 */
    private Long inStaff;

    /**  员工出人数 */
    private Long outStaff;

    /**  经过的员工人数 */
    private Long passbyStaff;

    /**  折返的员工人数 */
    private Long turnbackStaff;

    /** 最大停留时间  */
    private Long maxStayTime;

    /** 儿童平均停留时间  */
    private Long childAvgStayTime;

    /** 儿童最大停留时间  */
    private Long childMaxStayTime;

    /**  员工平均停留时间 */
    private Long staffAvgStayTime;

    /**  员工最大停留时间 */
    private Long staffMaxStayTime;

}
