package com.hxzy.demo.flowhttpserver.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hxzy.demo.flowhttpserver.entity.DeviceFlow;
import com.hxzy.demo.flowhttpserver.entity.HeartBeatReqVo;
import com.hxzy.demo.flowhttpserver.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 *
 * 心跳上报地址：http://ip:port/api/HeartBeat
 * 数据上报地址：http://ip:port/api/DataUpload
 */
@Slf4j
@RestController
@RequestMapping("api")
public class FlowController {


    @PostMapping("/HeartBeat")
    public Map<String, Object> heartbeat(@RequestBody HeartBeatReqVo params) throws Exception {

        String sn = params.getSn();

        Map<String,Object> heatMap = new HashMap();
        heatMap.put("sn",sn);
        Map<String,Object>  map = new HashMap();

        log.info("【客流】心跳->接收: {}", params);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = new Date();
        String nowDateStr = sdf.format(newDate);
        String dayStr = nowDateStr.substring(0, 11);

        //营业开始时间
        String startTime = dayStr + "01:30:00";
        //营业结束时间
        String endTime   = dayStr + "20:25:00";
        //上传间隔 数据上报周期，0-实时上报  单位：分钟
        int uploadInterval = 0;
        //数据上报方式，默认：Add   Add-增量；Total-总量
        String dataMode = "Add";
        //GMT时区
        String timezone = "+8";

        heatMap.put("dataStartTime", DateUtil.dateTimeToLong(startTime));
        heatMap.put("dataEndTime",  DateUtil.dateTimeToLong(endTime));
        heatMap.put("uploadInterval", uploadInterval);
        heatMap.put("timezone", timezone);
        heatMap.put("time", DateUtil.dateTimeToLong(nowDateStr));
        heatMap.put("dataMode","Add");
        map.put("data",heatMap);
        map.put("code", 0);
        map.put("msg","success");
        log.info("【客流】心跳回复：----" +map.toString());

        return map;
    }


    @PostMapping("/DataUpload")
    public Map<String, Object> dataUpload(@RequestBody JSONObject payload) {

        String sn = payload.getString("sn");
        Map<String, Object> result = new LinkedHashMap();
        Map<String, Object> data = new LinkedHashMap();
        Date newDate = new Date();
        // 将失败的结果存入集合
        result.put("code", 2);
        result.put("msg", "上传失败,请联系负责人");

        try {
            JSONArray dataArray = payload.getJSONArray("data");
            if(dataArray.size() > 0){
                DeviceFlow flow = new DeviceFlow();
                flow.setSn(sn);
                //in
                flow.setIn(0L);
                flow.setInAdult(0L);
                flow.setInChild(0L);
                flow.setInStaff(0L);
                //out
                flow.setOut(0L);
                flow.setOutAdult(0L);
                flow.setOutChild(0L);
                flow.setOutStaff(0L);
                //passby
                flow.setPassby(0L);
                flow.setPassbyAdult(0L);
                flow.setPassbyChild(0L);
                flow.setPassbyStaff(0L);
                //turnback
                flow.setTurnback(0L);
                flow.setTurnbackAdult(0L);
                flow.setTurnbackChild(0L);
                flow.setTurnbackStaff(0L);
                //stay
                Long avgStayTime = 0L;
                Long maxStayTime = 0L;
                Long chilAvgTime = 0L;
                Long chilMaxTime = 0L;
                Long staffAvgTime = 0L;
                Long staffMaxTime = 0L;

                for(int i=0; i<dataArray.size(); i++){
                    JSONObject dataObj = dataArray.getJSONObject(i);

                    JSONObject inObj = dataObj.getJSONObject("in");
                    flow.setIn(flow.getIn() + inObj.getLong("count"));
                    flow.setInAdult(flow.getInAdult() + inObj.getLong("count") - inObj.getLong("child") - inObj.getLong("staff"));
                    flow.setInChild(flow.getInChild() + inObj.getLong("child"));
                    flow.setInStaff(flow.getInStaff() + inObj.getLong("staff"));

                    JSONObject outObj = dataObj.getJSONObject("out");
                    flow.setOut(flow.getOut() + outObj.getLong("count"));
                    flow.setOutAdult(flow.getOutAdult() + outObj.getLong("count") - outObj.getLong("child") - outObj.getLong("staff"));
                    flow.setOutChild(flow.getOutChild() + outObj.getLong("child"));
                    flow.setOutStaff(flow.getOutStaff() + outObj.getLong("staff"));

                    JSONObject passbyObj = dataObj.getJSONObject("pass");
                    flow.setPassby(flow.getPassby() + passbyObj.getLong("count"));
                    flow.setPassbyAdult(flow.getPassbyAdult() + passbyObj.getLong("count") - passbyObj.getLong("child") - passbyObj.getLong("staff"));
                    flow.setPassbyChild(flow.getPassbyChild() + passbyObj.getLong("child"));
                    flow.setPassbyStaff(flow.getPassbyStaff() + passbyObj.getLong("staff"));

                    JSONObject turnbackObj = dataObj.getJSONObject("turnback");
                    flow.setTurnback(flow.getTurnback() + turnbackObj.getLong("count"));
                    flow.setTurnbackAdult(flow.getTurnbackAdult() + turnbackObj.getLong("count") - turnbackObj.getLong("child") - turnbackObj.getLong("staff"));
                    flow.setTurnbackChild(flow.getTurnbackChild() + turnbackObj.getLong("child"));
                    flow.setTurnbackStaff(flow.getTurnbackStaff() + turnbackObj.getLong("staff"));

                    JSONObject stayTimekObj = dataObj.getJSONObject("stayTime");
                    avgStayTime = avgStayTime + stayTimekObj.getLong("avgTime");
                    if(stayTimekObj.getLong("maxTime") > maxStayTime){
                        maxStayTime = stayTimekObj.getLong("maxTime");
                    }
                    chilAvgTime = chilAvgTime + stayTimekObj.getLong("childAvgTime");
                    if(stayTimekObj.getLong("childMaxTime") > chilMaxTime){
                        chilMaxTime = stayTimekObj.getLong("childMaxTime");
                    }
                    staffAvgTime = staffAvgTime + stayTimekObj.getLong("staffAvgTime");
                    if(stayTimekObj.getLong("staffMaxTime") > staffMaxTime){
                        staffMaxTime = stayTimekObj.getLong("staffMaxTime");
                    }
                }

                flow.setAvgStayTime(String.valueOf(avgStayTime/dataArray.size()));
                flow.setMaxStayTime(maxStayTime);
                flow.setChildAvgStayTime(chilAvgTime/dataArray.size());
                flow.setChildMaxStayTime(chilMaxTime);
                flow.setStaffAvgStayTime(staffAvgTime/dataArray.size());
                flow.setStaffMaxStayTime(staffMaxTime);

                result.put("code", 0);
                result.put("msg", "success");
                data.put("sn", sn);
                data.put("time", newDate.getTime() / 1000);
                result.put("data", data);
                log.info("【客流】数据上传->成功: result={}", result);
            }
        } catch (Exception e) {
            result.put("code", 2);
            result.put("msg", "上传数据错误");
            log.error("【客流】数据上传->异常: result={}", result, e);
        }

        return result;
    }

}
