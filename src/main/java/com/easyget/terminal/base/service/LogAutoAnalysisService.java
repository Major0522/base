package com.easyget.terminal.base.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.easyget.terminal.base.dao.CellExceptionDao;
import com.easyget.terminal.base.dao.SubstanceDao;
import com.easyget.terminal.base.entity.CabinetCell;
import com.easyget.terminal.base.entity.CellException;
import com.easyget.terminal.base.entity.Member;
import com.easyget.terminal.base.entity.Substance;
import com.easyget.terminal.base.model.AlarmState;
import com.easyget.terminal.base.util.ContextUtil;
import com.seagen.ecc.utils.DateUtils;

/*日志自动分析文件*/
public class LogAutoAnalysisService {

    private static Logger logger = Logger.getLogger(LogAutoAnalysisService.class);

    private static LogAutoAnalysisService INSTANCE;

    public static LogAutoAnalysisService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LogAutoAnalysisService();
        }
        return INSTANCE;
    }

    public LogAutoAnalysisService() {

        // TODO Auto-generated constructor stub
    }

    /*增加样品成功过程记入到tb_log_cell_exception中 mark by pgl
     *@author pengguoli
     * @param substance 样品信息。
     *@param outPutTime  样品回收时间。*/
    public void RecordAddSubstanceSuccess(Substance substance, CabinetCell cell) {

        /*将上架成功记录记入到tb_log_cell_exception中 mark by  pgl**/
        final CellException cellException = new CellException();
        /*获取当前操作者的手机号*/
        final Member member = ContextUtil.getCurrentMember();
        final String mobile = member != null ? member.getMemberPhone() : null;

        String switchState = null;
        /*获取取件码*/
        cellException.setdynamicPassword(substance.getDynamicPwd());

        /*获取操作员的手机号*/
        cellException.setoperatorPhone(mobile);

        /*设置当前样品存放的副柜号SlaveId*/
        cellException.setSlaveId(cell.getSlaveId());

        /*设置当前样品存放的格口号cellId*/
        cellException.setcellId(cell.getCellId());

        cellException.alarmState = AlarmState.AddSubstanceSuccess;

        cellException.setsystemOperateType(cellException.getsystemOperateType());

        /*设置当前件存放的时间*/
        cellException.setoccureTime(substance.getInputTime());

        if (cell.getSwitchState() == 1) {
            switchState = "关闭";
        } else if (cell.getSwitchState() == 2) {
            switchState = "未关闭";
        } else {
            switchState = "未知";
        }

        /*设置当前发生详情*/
        cellException.setContent(
                "样品专员: " + cellException.getoperatorPhone() + " 在  " + cellException.getoccureTime() + "上架样品。" + "存放格口为"
                        + "(" + cellException.getslaveId() + "," + cellException.getcellId() + ")。 " + "取件码为: "
                        + cellException.getdynamicPassword() + " 格口状态为: " + switchState + ";");

        CellExceptionDao.getInstance().add(cellException);

        /*将上架成功记录记入到tb_log_cell_exception中 mark by  pgl**/
    }

    /*将快件退件失败过程记入到tb_log_cell_exception中 mark by pgl
     *@author pengguoli
     * @param trackingNo
     * @param substance
     * @param  cell
     */
    /*将退件失败记录记入到tb_log_cell_exception中 mark by pgl**/
    public void RecordCancelExpresFailed(String trackingNo, Substance substance) {
        final CellException cellException = new CellException();

        /*获取快递单号***************/

        cellException.settrackingNo(trackingNo);

        /*通过substanceid获取到当前取件的取件码，既然已经执行到这里来了，说明肯定已经输入取件码，并且验证通过了****/
        cellException.setdynamicPassword(substance.getDynamicPwd());

        /*获取收件人手机号*****************/
        final Member member = ContextUtil.getCurrentMember();

        final String mobile = member != null ? member.getMemberPhone() : null;
        cellException.setoperatorPhone(mobile);

        /*设置当前件存放的副柜号SlaveId**********/
        cellException.setSlaveId(substance.getSlaveId());

        /*设置当前件存放的格口号cellId*********/
        cellException.setcellId(substance.getCellId());

        /*设置当前件取件发生时间*********/
        cellException.setoccureTime(DateUtils.getDateTimeStr());

        cellException.alarmState = AlarmState.ErrorForCancelOpen;

        cellException.setsystemOperateType(cellException.alarmState.getDescription());

        /*设置当前发生详情*/
        cellException.setContent(
                "快递员 " + cellException.getoperatorPhone() + " 在  " + cellException.getoccureTime() + "退件。" + "该快件存放格口为"
                        + "(" + cellException.getslaveId() + "," + cellException.getcellId() + ")。 " + "取件码为: "
                        + cellException.getdynamicPassword() + " 快递单号为: " + trackingNo + " 操作结果为: 打开格口失败;");

        CellExceptionDao.getInstance().add(cellException);

        /*将取件成功记录记入到tb_log_cell_exception中 mark by  pgl**/
    }

    /*将快件成功退件过程记入到tb_log_cell_exception中 mark by pgl
     *@author pengguoli
     * @param trackingNo
     * @param substanceId ID
     * @param  cell
     */

    public void RecordCancelExpresSuccess(String trackingNo, Substance substance) {

        /*将退件成功记录记入到tb_log_cell_exception中 mark by pgl**/
        final CellException cellException = new CellException();

        /*获取快递单号***************/

        cellException.settrackingNo(trackingNo);

        /*通过substanceid获取到当前取件的取件码，既然已经执行到这里来了，说明肯定已经输入取件码，并且验证通过了****/
        cellException.setdynamicPassword(substance.getDynamicPwd());

        /*获取收件人手机号*****************/
        final Member member = ContextUtil.getCurrentMember();

        final String mobile = member != null ? member.getMemberPhone() : null;
        cellException.setoperatorPhone(mobile);

        /*slaveid 副柜ID   cellId  格口ID 这两个参数都可以通过查表SubstanceDao获取到**/

        /*设置当前件存放的副柜号SlaveId**********/
        cellException.setSlaveId(substance.getSlaveId());

        /*设置当前件存放的格口号cellId*********/
        cellException.setcellId(substance.getCellId());

        /*设置当前件取件发生时间*********/
        cellException.setoccureTime(DateUtils.getDateTimeStr());

        cellException.alarmState = AlarmState.CancelExpressSuccess;

        cellException.setsystemOperateType(cellException.alarmState.getDescription());

        /*设置当前发生详情*/
        cellException.setContent(
                "快递员 " + cellException.getoperatorPhone() + " 在  " + cellException.getoccureTime() + "退件。" + "该快件存放格口为"
                        + "(" + cellException.getslaveId() + "," + cellException.getcellId() + ")。 " + "取件码为: "
                        + cellException.getdynamicPassword() + " 快递单号为: " + trackingNo + " 操作结果为: 打开格口成功;");

        CellExceptionDao.getInstance().add(cellException);

        /*将退件成功记录记入到tb_log_cell_exception中 mark by  pgl**/

    }

    /*将格口异常打开过程记入到tb_log_cell_exception中 
     *@author pengguoli
     * @param substanceId 样品ID。
     */
    public void RecordCellOpenException(String slaveId, String cell) {

        /*在这里打开格口异常报警到数据库表  格口自然打开告警***/

        /*到数据库中 tb_log_cell_exception***/
        final CellException cellException = new CellException();
        logger.info("收到工控机主动上报格口异常打开告警CellExceptionDao数据库表格口自动打开记录");

        cellException.setSlaveId(Integer.valueOf(slaveId).intValue());

        cellException.setcellId(Integer.valueOf(cell).intValue());

        cellException.setoccureTime(DateUtils.getDateTimeStr());

        cellException.setContent("格口(" + slaveId + "," + cell + ") 在" + cellException.getoccureTime() + "异常弹开");
        CellExceptionDao.getInstance().add(cellException);
    }

    /*记录远程格口操作
     *@author pengguoli
     * @param substanceId 样品ID。
     */
    public void RecordCellOperation(String content, int slaveId, int cellId) {

        /*将取件成功记录记入到tb_log_cell_exception中**/

        logger.info("将格口操作过程记录到tb_log_cell_exception中");
        final CellException cellException = new CellException();

        /*设置当前件取件发生时间*******************/
        cellException.setoccureTime(DateUtils.getDateTimeStr());

        cellException.alarmState = AlarmState.RomoteCellOperation;
        /*设置副柜ID*/
        cellException.setSlaveId(slaveId);
        /*设置格口ID*/
        cellException.setcellId(cellId);

        cellException.setsystemOperateType(cellException.alarmState.getDescription());

        /*设置当前发生详情**************************/
        cellException.setContent(content);

        CellExceptionDao.getInstance().add(cellException);

    }

    /*记录守护程序或者应用程序有崩溃的情况
     *@author pengguoli
     * @param substanceId 样品ID。
     */
    public void RecordDamonProgramCrashed(String content) {

        /*将取件成功记录记入到tb_log_cell_exception中**/

        logger.info("将守护进程崩溃记录tb_log_cell_exception中");
        final CellException cellException = new CellException();

        /*设置当前件取件发生时间*******************/
        cellException.setoccureTime(DateUtils.getDateTimeStr());

        cellException.alarmState = AlarmState.ProgramCrashed;

        cellException.setsystemOperateType(cellException.alarmState.getDescription());

        /*设置当前发生详情**************************/
        cellException.setContent("在  " + cellException.getoccureTime() + content);

        CellExceptionDao.getInstance().add(cellException);

    }

    /*记录人工重启业务程序
     *@author pengguoli
     * @param substanceId 样品ID。
     */
    public void RecordManMadeRestartBizProgram() {

        /*将取件成功记录记入到tb_log_cell_exception中**/

        logger.info("将人工重启业务过程记录到tb_log_cell_exception中");
        final CellException cellException = new CellException();

        /*设置当前件取件发生时间*******************/
        cellException.setoccureTime(DateUtils.getDateTimeStr());

        cellException.alarmState = AlarmState.ManMadeRestartBizProgram;

        cellException.setsystemOperateType(cellException.alarmState.getDescription());

        /*设置当前发生详情**************************/
        cellException.setContent("  在  " + cellException.getoccureTime() + "成功重启业务程序");

        CellExceptionDao.getInstance().add(cellException);

    }

    /*记录人工重启系统
     *@author pengguoli
     * @param substanceId 样品ID。
     */
    public void RecordManMadeRestartSystem() {

        /*将取件成功记录记入到tb_log_cell_exception中**/

        logger.info("将人工重启系统过程记录到tb_log_cell_exception中");
        final CellException cellException = new CellException();

        /*设置当前件取件发生时间*******************/
        cellException.setoccureTime(DateUtils.getDateTimeStr());

        cellException.alarmState = AlarmState.ManMadeRestartSystem;

        cellException.setsystemOperateType(cellException.alarmState.getDescription());

        /*设置当前发生详情**************************/
        cellException.setContent("  在  " + cellException.getoccureTime() + "成功重启系统");

        CellExceptionDao.getInstance().add(cellException);

    }

    /*
     * 记录报警信息入数据库表tb_exception_dao中
     *@author pengguoli
     * @param module 模块名称。
     * @param slaveId 副柜地址，如果不是副柜则为0 如果是副柜0表示全部副柜。
     * @param state
     * @param alarmState
     */
    public void recordOperateAlarm(AlarmState alarmState, int slaveId, int cellId) {

        final CellException cellException = new CellException();

        cellException.setSlaveId(slaveId);

        cellException.setcellId(cellId);

        cellException.setoccureTime(DateUtils.getDateTimeStr());

        final Member member = ContextUtil.getCurrentMember();

        final String mobile = member != null ? member.getMemberPhone() : null;

        cellException.setoperatorPhone(mobile);
        final String content = String.format(
                "快递员: " + cellException.getoperatorPhone() + "在" + cellException.getoccureTime()
                        + alarmState.getDescription() + "相关格口为" + "(" + cellException.getslaveId() + ","
                        + cellException.getcellId() + ")");

        cellException.alarmState = alarmState;
        /*需要得到格口打开成功的取件码。。一个是放快件成功 一个是上架样品打开格口成功*/

        List<CellException> autocellexception = CellExceptionDao.getInstance().getDynamicPasswordByslaveIdAndcellId(
                AlarmState.PutExpressSuccess.getDescription(),
                slaveId,
                cellId,
                cellException.getoccureTime());
        if (autocellexception.size() > 0) {
            /*取最后一条记录值*/
            cellException.setdynamicPassword(autocellexception.get(autocellexception.size() - 1).getdynamicPassword());
        } else {
            autocellexception = CellExceptionDao.getInstance().getDynamicPasswordByslaveIdAndcellId(
                    AlarmState.AddSubstanceSuccess.getDescription(),
                    slaveId,
                    cellId,
                    cellException.getoccureTime());

            if (autocellexception.size() > 0) {
                /*取最后一条记录值*/
                cellException
                        .setdynamicPassword(autocellexception.get(autocellexception.size() - 1).getdynamicPassword());
            }
        }
        logger.info("得到的取件码是：" + cellException.getdynamicPassword());
        /*设置操作员手机号*/

        cellException.setsystemOperateType(cellException.getsystemOperateType());

        cellException.setContent(content);

        /*content内容就是“取件时关闭格口失败”，“取件时打开格口失败”等内容**/

        //  if (null == mobile) {
        //    content = content.replace(", 用户: null", " ");
        //   }

        //取件时格口打开操作做了特殊的处理，所以可以在这里进行屏蔽

        CellExceptionDao.getInstance().add(cellException);

    }

    /*将快件放件失败过程记入到tb_log_cell_exception中 mark by pgl
      *@author pengguoli
      * @param trackingNo
      * @param substanceId ID
      * @param  cell
      */
    /*将快件放件失败记录记入到tb_log_cell_exception中 mark by pgl**/
    public void RecordPutExpresFailed(String trackingNo, Substance substance) {
        final CellException cellException = new CellException();

        /*获取快递单号***************/

        cellException.settrackingNo(trackingNo);

        /*通过substanceid获取到当前取件的取件码，既然已经执行到这里来了，说明肯定已经输入取件码，并且验证通过了****/
        cellException.setdynamicPassword(substance.getDynamicPwd());

        /*获取收件人手机号*****************/
        final Member member = ContextUtil.getCurrentMember();

        final String mobile = member != null ? member.getMemberPhone() : null;
        cellException.setoperatorPhone(mobile);

        /*设置当前件存放的副柜号SlaveId**********/
        cellException.setSlaveId(substance.getSlaveId());

        /*设置当前件存放的格口号cellId*********/
        cellException.setcellId(substance.getCellId());

        /*设置当前件取件发生时间*********/
        cellException.setoccureTime(DateUtils.getDateTimeStr());

        cellException.alarmState = AlarmState.ErrorForPutOpen;

        cellException.setsystemOperateType(cellException.alarmState.getDescription());

        /*设置当前发生详情*/
        cellException.setContent(
                "快递员 " + cellException.getoperatorPhone() + " 在  " + cellException.getoccureTime() + "放件。" + "该快件存放格口为"
                        + "(" + cellException.getslaveId() + "," + cellException.getcellId() + ")。 " + "取件码为: "
                        + cellException.getdynamicPassword() + " 快递单号为: " + trackingNo + " 操作结果为: 放件打开格口失败;");

        CellExceptionDao.getInstance().add(cellException);

        /*将取件成功记录记入到tb_log_cell_exception中 mark by  pgl**/
    }

    /*将回收样品成功过程记入到tb_log_cell_exception中 mark by pgl
     *@author pengguoli
     * @param substance 样品信息。
     *@param outPutTime  样品回收时间。*/

    /*记录放件时点击否
     *@author pengguoli
     * @param
     */
    public void RecordPutExpressClickNo() {

        //放件点击否 ，记做格口异常 
        //log.info("格口打开失败/关闭失败告警保存到数据库tb_log_cell_excception");
        logger.info("记录放件点击【否】");
        final CellException cellException = new CellException();
        /*获取收件人手机号*****************/
        final Member member = ContextUtil.getCurrentMember();

        final String mobile = member != null ? member.getMemberPhone() : null;
        cellException.setoperatorPhone(mobile);
        /*放件点击否的人的手机号*/
        cellException.setContent("放件点击否:操作人的手机号是：" + mobile);

        cellException.alarmState = AlarmState.PutExpressClickNo;

        cellException.setsystemOperateType(cellException.getsystemOperateType());

        cellException.setoccureTime(DateUtils.datetimeToString(new Date()));

        CellExceptionDao.getInstance().add(cellException);//

    }

    /*将快件成功投递过程记入到tb_log_cell_exception中 mark by pgl
     *@author pengguoli
     * @param substanceId ID。
     */
    public void recordPutExpresSuccess(String trackingNo, Substance substance, CabinetCell cell) {
        /*将放件成功记录记入到tb_log_cell_exception中 mark by pgl**/

        final CellException cellException = new CellException();

        /*获取快递单号***************/

        cellException.settrackingNo(trackingNo);

        /*通过substance获取到当前取件的取件码，既然已经执行到这里来了，说明肯定已经产生了经取件码****/
        cellException.setdynamicPassword(substance.getDynamicPwd());

        /*获取收件人手机号*****************/
       
        final Member member = ContextUtil.getCurrentMember();

        final String mobile = member != null ? member.getMemberPhone() : null;
        cellException.setoperatorPhone(mobile);

        //  cellException.setoperatorPhone();
        /*设置当前件存放的副柜号SlaveId**********/
        cellException.setSlaveId(cell.getSlaveId());

        /*设置当前件存放的格口号cellId*********/
        cellException.setcellId(cell.getCellId());

        cellException.alarmState = AlarmState.PutExpressSuccess;

        cellException.setsystemOperateType(cellException.alarmState.getDescription());

        /*设置当前件取件发生时间*********/
        cellException.setoccureTime(substance.getInputTime());

        /*设置当前发生详情*/
        cellException.setContent(
                "快递员 " + cellException.getoperatorPhone() + " 在  " + cellException.getoccureTime() + "放件。" + "该快件存放格口为"
                        + "(" + cellException.getslaveId() + "," + cellException.getcellId() + ")。 " + "取件码为: "
                        + cellException.getdynamicPassword() + " 快递单号为: " + trackingNo + " 操作结果为: 放件打开格口成功;");
        CellExceptionDao.getInstance().add(cellException);
        /*将放件成功记录记入到tb_log_cell_exception中 mark by  pgl**/

    }

    public void RecordRecycleSubstanceSuccess(Substance substance, String outPutTime) {
        /*将回收成功记录记入到tb_log_cell_exception中 mark by  pgl**/
        final CellException cellException = new CellException();
        /*获取取件码***************/
        cellException.setdynamicPassword(substance.getDynamicPwd());

        /*获取操作员的手机号*****************/
        // cellException.setoperatorPhone(activity.getActivityPhone());

        /*设置当前样品存放的副柜号SlaveId**********/
        cellException.setSlaveId(substance.getSlaveId());

        final Member member = ContextUtil.getCurrentMember();

        final String mobile = member != null ? member.getMemberPhone() : null;
        cellException.setoperatorPhone(mobile);

        /*设置当前样品存放的格口号cellId*********/
        cellException.setcellId(substance.getCellId());

        /*设置当前样品的回收时间*********/
        cellException.setoccureTime(outPutTime);
        cellException.alarmState = AlarmState.RecycleSubstanceSuccess;
        cellException.setsystemOperateType(cellException.getsystemOperateType());

        /*设置当前发生详情*/
        cellException.setContent(
                "样品专员 " + cellException.getoperatorPhone() + " 在  " + cellException.getoccureTime() + "下架样品。"
                        + "该样品存放格口为" + "(" + cellException.getslaveId() + "," + cellException.getcellId() + ")。 "
                        + "取件码为: " + cellException.getdynamicPassword() + "操作结果为: 回收成功;");

        CellExceptionDao.getInstance().add(cellException);

        /*将回收成功记录记入到tb_log_cell_exception中 mark by  pgl**/

    }

    /*记录副柜失联
     *@author pengguoli
     * @param substanceId 样品ID。
     */
    public void RecordSlaveOffline(String content) {

        /*将副柜失联记录记入到tb_log_cell_exception中**/

        logger.info("将副柜失联记录到tb_log_cell_exception中");
        final CellException cellException = new CellException();

        /*设置当前件取件发生时间*******************/
        cellException.setoccureTime(DateUtils.getDateTimeStr());

        cellException.alarmState = AlarmState.SlaveOffLine;

        cellException.setsystemOperateType(cellException.alarmState.getDescription());

        /*设置当前发生详情**************************/
        cellException.setContent("在  " + cellException.getoccureTime() + content);

        CellExceptionDao.getInstance().add(cellException);

    }

    /**记录开机启动
     *@author pengguoli
     * @param
     */
    public void RecordSystemStart() {

        logger.info("程序重启时更新tb_log_cell_exception");
        final CellException cellException = new CellException();

        cellException.setContent(AlarmState.SystemStart.getDescription());

        cellException.setoccureTime(DateUtils.datetimeToString(new Date()));

        cellException.alarmState = AlarmState.SystemStart;

        cellException.setsystemOperateType(cellException.getsystemOperateType());
        CellExceptionDao.getInstance().add(cellException);

    }

    /*将领取快件成功过程记入到tb_log_cell_exception中 mark by pgl
     *@author pengguoli
     * @param trackingNo 快递单号。
     * @param substance 样品。
     * @param cell 格口信息
    
     */
    public void RecordTakeExpressSuccess(String trackingNo, Substance substance, CabinetCell cell) {

        /*将放件成功记录记入到tb_log_cell_exception中 mark by pgl**/

        final CellException cellException = new CellException();

        /*获取快递单号***************/

        cellException.settrackingNo(trackingNo);

        /*通过substance获取到当前取件的取件码，既然已经执行到这里来了，说明肯定已经产生了经取件码****/
        cellException.setdynamicPassword(substance.getDynamicPwd());

        /*获取收件人手机号*****************/
        //  cellException.setoperatorPhone();
        /*设置当前件存放的副柜号SlaveId**********/
        cellException.setSlaveId(cell.getSlaveId());

        /*设置当前件存放的格口号cellId*********/
        cellException.setcellId(cell.getCellId());

        /*设置当前件取件发生时间*********/
        cellException.setoccureTime(substance.getInputTime());

        /*设置当前发生详情*********/
        /*领取快件打开格口成功**/
        cellException.alarmState = AlarmState.TakeExpressSuccess;

        cellException.setsystemOperateType(cellException.getsystemOperateType());

        /*设置当前发生详情*/
        cellException.setContent(
                "收件人在 " + cellException.getoccureTime() + "取件。" + "该快件存放格口为" + "(" + cellException.getslaveId() + ","
                        + cellException.getcellId() + ")。 " + "取件码为: " + cellException.getdynamicPassword() + " 快递单号为: "
                        + trackingNo + " 操作结果为: 取件成功;");

        CellExceptionDao.getInstance().add(cellException);
        /*将放件成功记录记入到tb_log_cell_exception中 mark by  pgl**/
    }

    /*将领取样品或者快件失败的过程记入到tb_log_cell_exception中 mark by pgl
     *@author pengguoli
     * @param substance 样品。
     * @param slaveId 副柜信息
     *@param cellId   格口信息
     */
    public void RecordTakeFailed(Substance substance, int slaveId, int cellId) {
        final CellException cellException = new CellException();

        /*通过substance获取到当前取件的取件码，既然已经执行到这里来了，说明肯定已经输入取件码，并且验证通过了****/
        cellException.setdynamicPassword(substance.getDynamicPwd());

        /*设置当前件存放的副柜号SlaveId**********/
        cellException.setSlaveId(slaveId);

        /*设置当前件存放的格口号cellId*********/
        cellException.setcellId(cellId);

        /*设置当前件取件发生时间*********/
        cellException.setoccureTime(DateUtils.getDateTimeStr());
        /*取件格口打开失败**/
        cellException.alarmState = AlarmState.ErrorForTakeOpen;

        cellException.setsystemOperateType(cellException.alarmState.getDescription());

        /*设置当前发生详情*/
        cellException.setContent(
                "快递员 " + cellException.getoperatorPhone() + " 在  " + cellException.getoccureTime() + "取件。" + "该快件存放格口为"
                        + "(" + cellException.getslaveId() + "," + cellException.getcellId() + ")。 " + "取件码为: "
                        + cellException.getdynamicPassword() + " 快递单号为: " + " 操作结果为: 打开格口失败;");

        CellExceptionDao.getInstance().add(cellException);

        /*将取件成功记录记入到tb_log_cell_exception中 mark by  pgl**/

    }

    /*将领取样品成功过程记入到tb_log_cell_exception中 mark by pgl
    *@author pengguoli
    * @param substanceId 样品ID。
    */
    public void RecordTakeSubstanceSuccess(String substanceId) {

        /*将取件成功记录记入到tb_log_cell_exception中 mark by pgl**/

        logger.info("将取样品成功过程记录到tb_log_cell_exception中");
        final CellException cellException = new CellException();

        /*通过substanceid获取到当前取件的取件码，既然已经执行到这里来了，说明肯定已经输入取件码，并且验证通过了****/
        cellException.setdynamicPassword(SubstanceDao.getInstance().getById(substanceId).getDynamicPwd());
        /*获取收件人手机号*****************/
        final Member member = ContextUtil.getCurrentMember();
        final String mobile = member != null ? member.getMemberPhone() : null;
        cellException.setoperatorPhone(mobile);

        /*取样品成功记录*/

        logger.info("操作员的手机号是" + mobile);
        /*slaveid 副柜ID   cellId  格口ID 这两个参数都可以通过查表SubstanceDao获取到**/

        /*设置当前件存放的副柜号SlaveId**********/
        cellException.setSlaveId(SubstanceDao.getInstance().getById(substanceId).getSlaveId());

        /*设置当前件存放的格口号cellId*****************/
        cellException.setcellId(SubstanceDao.getInstance().getById(substanceId).getCellId());

        /*设置当前件取件发生时间*******************/
        cellException.setoccureTime(DateUtils.getDateTimeStr());

        /*领取样品格口打开成功*/
        cellException.alarmState = AlarmState.TakeSubstanceSuccess;

        cellException.setsystemOperateType(cellException.alarmState.getDescription());

        /*设置当前发生详情**************************/
        cellException.setContent(
                "操作员  " + cellException.getoperatorPhone() + "  在  " + cellException.getoccureTime() + "成功取走样品.该样品存放于 "
                        + "(" + cellException.getslaveId() + "," + cellException.getcellId() + ")." + " 样品取件码为: "
                        + cellException.getdynamicPassword());
 
        CellExceptionDao.getInstance().add(cellException);
        /*将领取快件成功过程记入到tb_log_cell_exception中 **/
    }

    /*记录升级程序失败
     *@author pengguoli
     * @param substanceId 样品ID。
     */
    public void RecordUpdateProgramFailed(String content) {

        /*将取件成功记录记入到tb_log_cell_exception中**/

        logger.info("将升级失败过程记录到tb_log_cell_exception中");
        final CellException cellException = new CellException();

        /*设置当前件取件发生时间*******************/
        cellException.setoccureTime(DateUtils.getDateTimeStr());

        cellException.alarmState = AlarmState.UpdateProgramFailed;

        cellException.setsystemOperateType(cellException.alarmState.getDescription());

        /*设置当前发生详情**************************/
        cellException.setContent(content);

        CellExceptionDao.getInstance().add(cellException);

    }
    
    /*记录升级程序成功
     *@author pengguoli
     *
     */
    public void RecordUpdateProgramSucceed(String content) {

        /*将取件成功记录记入到tb_log_cell_exception中**/

        logger.info("将升级成功过程记录到tb_log_cell_exception中");
        final CellException cellException = new CellException();

        /*设置当前件取件发生时间*******************/
        cellException.setoccureTime(DateUtils.getDateTimeStr());

        cellException.alarmState = AlarmState.UpdateProgramSuccess;

        cellException.setsystemOperateType(cellException.alarmState.getDescription());

        /*设置当前发生详情**************************/
        cellException.setContent(content);

        CellExceptionDao.getInstance().add(cellException);

    }
}
