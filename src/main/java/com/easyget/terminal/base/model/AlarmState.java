package com.easyget.terminal.base.model;

/**
 * 告警状态常量定义。
 * 
 * @author wutianbin
 *
 */
public enum AlarmState {

    /*格口告警状态：正常(0) */
    Nomal(0, "格口正常"),

    /*格口告警状态：取件关闭失败(1) */
    ErrorForTakeClose(1, "取件时关闭格口失败"),

    /*格口告警状态：取件打开失败(2) */
    ErrorForTakeOpen(2, "取件时打开格口失败"),

    /*格口告警状态：放件关闭失败(3) */
    ErrorForPutClose(3, "放件时关闭格口失败"),

    /*格口告警状态：放件打开失败(4) */
    ErrorForPutOpen(4, "放件时打开格口失败"),

    /*格口告警状态：退件时打开格口失败(5) */
    ErrorForCancelOpen(5, "退件时打开格口失败"),

    /*格口告警状态：退件时关闭格口失败(6) */
    ErrorForCancelClose(6, "退件时关闭格口失败"),

    /*格口告警状态：上架时打开格口失败(7) */
    ErrorForAddOpen(7, "上架时打开格口失败"),

    /*格口告警状态：上架时关闭格口失败(8) */
    ErrorForAddClose(8, "上架时关闭格口失败"),

    /*格口告警状态：上架时检测货物故障(9) */
    ErrorForAddDetect(9, "上架时检测货物故障"),

    /*格口告警状态：上架时没有放置货物(10) */
    ErrorForAddNoGoods(10, "上架时没有放置货物"),

    /*格口告警状态：购买时打开格口失败(11) */
    ErrorForBuyOpen(11, "购买时打开格口失败"),

    /*格口告警状态：购买时关闭格口失败(12) */
    ErrorForBuyClose(12, "购买时关闭格口失败"),

    /*格口告警状态：购买时检测货物故障(13) */
    ErrorForBuyDetect(13, "购买时检测货物故障"),

    /*格口告警状态：购买时没有取走货物(14) */
    ErrorForBuyKeepGoods(14, "购买时没有取走货物"),

    /*格口告警状态：下架时打开格口失败(15) */
    ErrorForRemoveOpen(15, "下架时打开格口失败"),

    /*格口告警状态：下架时关闭格口失败(16) */
    ErrorForRemoveClose(16, "下架时关闭格口失败"),

    /*格口告警状态：下架时检测货物故障(17) */
    ErrorForRemoveDetect(17, "下架时检测货物故障"),

    /*格口告警状态：下架时没有取走货物(18) */
    ErrorForRemoveKeepGoods(18, "下架时没有取走货物"),

    /*格口告警状态：格口被异常打开(19) */
    ErrorForOpenException(19, "格口被异常打开"),
    /*领取样品格口打开成功*/
    TakeSubstanceSuccess(20, "领取样品格口打开成功"),
    /*领取快件格口打开成功*/
    TakeExpressSuccess(21, "领取快件格口打开成功"),

    /*系统开机启动*/
    SystemStart(22, "系统开机启动"),
    /*(23)放件时点击否 */
    PutExpressClickNo(23, "放件时点击否"),

    /*上架时格口打开成功*/
    AddSubstanceSuccess(24, "上架时格口打开成功"),

    /*下格时口打开成功*/
    RecycleSubstanceSuccess(25, "下架时格口打开成功"),

    /*快件投递格口打开成功*/
    PutExpressSuccess(26, "快件投递格口打开成功"),

    /*快件退件格口打开成功*/
    CancelExpressSuccess(27, "快件退件格口打开成功"),

    /*人工重启程序*/
    ManMadeRestartSystem(28, "人工重启系统"),

    /*人工重启业务程序*/
    ManMadeRestartBizProgram(29, "人工重启业务程序"),

    /*升级业务程序失败*/
    UpdateProgramFailed(30, "升级程序失败"),

    /*有副柜失联*/
    SlaveOffLine(31, "有副柜失联"),
    /*程序崩溃*/
    ProgramCrashed(32, "程序崩溃"),
    /*远程格口操作*/
    RomoteCellOperation(33, "远程格口操作"),

    /*升级程序成功，待用*/
    UpdateProgramSuccess(34, "升级程序成功"),

    /** 格口告警状态：未知的异常(-1) */
    ErrorForUnknown(-1, "未知的异常");

    public static AlarmState getByStrValue(String value) {
        for (final AlarmState v : values()) {
            if (v.strValue().equals(value)) {
                return v;
            }
        }

        return ErrorForUnknown;
    }

    public static AlarmState getByValue(int value) {
        for (final AlarmState v : values()) {
            if (v.value == value) {
                return v;
            }
        }

        return ErrorForUnknown;
    }

    public static String stringAllCloseError() {
        final StringBuilder alarms = new StringBuilder();

        alarms.append(ErrorForTakeClose.strValue() + ",");
        alarms.append(ErrorForCancelClose.strValue() + ",");
        alarms.append(ErrorForRemoveClose.strValue() + ",");
        alarms.append(ErrorForBuyClose.strValue() + ",");
        alarms.append(ErrorForPutClose.strValue() + ",");
        alarms.append(ErrorForAddClose.strValue());

        return alarms.toString();
    }

    public static String stringAllOpenError() {
        final StringBuilder alarms = new StringBuilder();

        alarms.append(ErrorForTakeOpen.strValue() + ",");
        alarms.append(ErrorForCancelOpen.strValue() + ",");
        alarms.append(ErrorForRemoveOpen.strValue() + ",");
        alarms.append(ErrorForBuyOpen.strValue() + ",");
        alarms.append(ErrorForPutOpen.strValue() + ",");
        alarms.append(ErrorForAddOpen.strValue());

        return alarms.toString();
    }

    private int value;

    private String description;

    private AlarmState(int value, String desc) {
        this.value = value;
        this.description = desc;
    }

    public String getDescription() {
        return this.description;
    }

    public int getValue() {
        return this.value;
    }

    public boolean isCloseError() {
        return this.equals(ErrorForTakeClose) || this.equals(ErrorForCancelClose) || this.equals(ErrorForRemoveClose)
                || this.equals(ErrorForBuyClose) || this.equals(ErrorForPutClose) || this.equals(ErrorForAddClose);
    }

    public boolean isOpenError() {
        return this.equals(ErrorForTakeOpen) || this.equals(ErrorForCancelOpen) || this.equals(ErrorForRemoveOpen)
                || this.equals(ErrorForBuyOpen) || this.equals(ErrorForPutOpen) || this.equals(ErrorForAddOpen);
    }

    public String strValue() {
        return String.valueOf(this.value);
    }

    @Override
    public String toString() {
        return strValue();
    }

}
