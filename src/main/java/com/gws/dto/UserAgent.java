package com.gws.dto;

import lombok.Data;

/**
 * 【UA解析】
 * 1、User-Agent，包括了一些版本信息和设备信息，格式 ： 操作系统/APP版本号 (deviceId,nt,dt,deviceVersion,areaName,cpu)
 * 1）demo 1 : Android/1.0.0(503030D5-2FDB-4138-A039-1DF1C4C74995,Wifi,Simulator,4.3,浙江杭州,Intel x86)
 * 2)  demo 2 : iOS/1.0.0(503030D5-2FDB-4138-A039-1DF1C4C74995,Wifi,iPhone7,10.3,浙江杭州,Intel x86)
 *
 * @author wangdong  3/24/17.
 */
@Data
public class UserAgent {

    private String appVersion;

    private String deviceId;

    private String nt;

    private String dt;

    private String deviceVersion;

    private String area;

    private String cpu;

    private String ip;
}
