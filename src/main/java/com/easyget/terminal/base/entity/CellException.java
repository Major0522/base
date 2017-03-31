package com.easyget.terminal.base.entity;

import com.easyget.terminal.base.model.AlarmState;

/**
 * 格口异常运行日志
 */
public class CellException {

    private Long serialId;/*这个变量好像没有啥用*/

    private String content;/*告警的具体内容*/

    private String operatorPhone;/*操作者手机号*/

    private int slaveId;/*副柜ID*/

    private int cellId;/*格口ID*/

    private String trackingNo;/*快递单号*/

    private String dynamicPassword;/*取件码*/

    private String occureTime;/*发生时间*/

    public AlarmState alarmState;

    public CellException() {

    }

    public CellException(Long serialId, String content) {
        this.serialId = serialId;
        this.content = content;
    }

    public CellException(String content) {

        this.content = content;
    }

    public CellException(String systemOperateType,String operatorPhone,String trackingNo,String dynamicPassword,int slaveId, int cellId, String content, String occureTime
            ) {
        this.operatorPhone = operatorPhone;
        this.trackingNo =trackingNo;
        this.dynamicPassword =dynamicPassword;
        this.slaveId = slaveId;
        this.cellId = cellId;
        this.content = content;
        this.occureTime = occureTime;
    }
   

    ////*************************获取参数值***********************************/////////////
    /*获取格口ID*/
    public int getcellId() {
        return cellId;
    }

    /*获取异常详情*/
    public String getContent() {
        return content;
    }

    /*获取取件码*/
    public String getdynamicPassword() {
        return dynamicPassword;
    }

    /*获取发生时间*/
    public String getoccureTime() {
        return occureTime;
    }

    /*获取操作者手机号码*/
    public String getoperatorPhone() {
        return operatorPhone;
    }

    public Long getSerialId() {
        return serialId;
    }

    /*获取副柜ID*/
    public int getslaveId() {
        return slaveId;
    }

    /**获取系统操作类型*/
    public String getsystemOperateType() {

        return alarmState.getDescription();
    }

    /*获取快递单号*/
    public String gettrackingNo() {
        return trackingNo;
    }

    /*获取系统操作类型*/

    ////*************************获取参数值***********************************/////////////
    ////*************************设置参数值***********************************/////////////
    /*设置格口ID*/
    public void setcellId(int cellId) {
        this.cellId = cellId;
    }

    /*设置详情*/
    public void setContent(String content) {
        this.content = content;
    }

    /*设置取件码*/
    public void setdynamicPassword(String dynamicPassword) {

        this.dynamicPassword = dynamicPassword;
    }

    /*设置发生时间*/
    public void setoccureTime(String occureTime) {
        this.occureTime = occureTime;
    }

    /*设置副柜号*/
    public void setoperatorPhone(String operatorPhone) {

        this.operatorPhone = operatorPhone;
    }

    public void setSerialId(Long serialId) {
        this.serialId = serialId;
    }

 
    public void setSlaveId(int slaveId) {

        this.slaveId = slaveId;
    }

    /*设置系统操作类型*/
    public void setsystemOperateType(String systemOperateType) {
    }

    /*获取快递单号*/
    public void settrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    ////*************************设置参数值***********************************/////////////
}
