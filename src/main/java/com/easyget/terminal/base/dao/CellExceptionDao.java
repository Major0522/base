package com.easyget.terminal.base.dao;

import java.util.List;

import com.easyget.terminal.base.entity.CellException;
import com.easyget.terminal.base.provider.BaseDao;
import com.easyget.terminal.base.util.Options;

//格口异常记录日志

/*格口异常日志记录数据库文件，所涉及的日志表是： tb_log_cell_exception***/
public class CellExceptionDao extends BaseDao<CellException> {

    private static CellExceptionDao INSTANCE;

    public static CellExceptionDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CellExceptionDao();
        }

        return INSTANCE;
    }

    public CellExceptionDao() {
    }

    // 格口异常记录表 tb_log_exception
    // 增加一条记录 包括修改的字段是 serialId，content和occureTime

    /*应日志自动分析需求，需要在tb_log_exception中增加cabinetNo***/
    public synchronized boolean add(CellException cellexception) {
        final String sql = "INSERT INTO tb_log_cell_exception(cabinetNo,operateType,operatorPhone,trackingNo,slaveId,cellId,dynamicPassword,content,occureTime)VALUES(?,?,?,?,?,?,?,?,?)";
        return this.update(
                sql,
                Options.getLong("ectcps.cabinetNo"), /*需要增加当前的柜子编号进去*/
                cellexception.getsystemOperateType(), /*操作类型*/
                cellexception.getoperatorPhone(), /*操作员手机号*/
                cellexception.gettrackingNo(), /*单号*/
                cellexception.getslaveId(), /*副柜ID*/
                cellexception.getcellId(), /*格口ID*/
                cellexception.getdynamicPassword(), /*取件码*/
                cellexception.getContent(), /*发生详情*/
                cellexception.getoccureTime()); /*发生时间*/
    }

    /*通过快递员手机号获取到当前表tb_log_cell_exception中的一条记录并且操作类型是*****************/
    public synchronized List<CellException> getCountByCouriorPhoneAndOperateType(String str, String systemOperateType) {
        final String sql = "SELECT * FROM tb_log_cell_exception WHERE operatorPhone=? AND operateType=?";
        return this.list(sql, str, systemOperateType);
    }

    /*通过提取码和操作类型获取到当前表tb_log_cell_exception中的一条记录****************/
    public synchronized CellException getCountByPwdAndOperateType(String str, String systemOperateType) {
        final String sql = "SELECT * FROM tb_log_cell_exception WHERE dynamicPassword=? AND operateType=?   limit 1";
        return this.get(sql, str, systemOperateType);
    }

    /*通过快递单号和操作类型获取到当前表tb_log_cell_exception中的一条记录并且操作类型是*****************/
    public synchronized CellException getCountByTrackingNoAndOperateType(String str, String systemOperateType) {
        final String sql = "SELECT * FROM tb_log_cell_exception WHERE trackingNo=? AND operateType=?   limit 1";
        return this.get(sql, str, systemOperateType);
    }

    /*通过副柜号和格口号以及放件的时间和取件的时间查询数据库tb_log_cell_exception是否有格口自动弹开的记录******************/
    public synchronized CellException getCountLikesystemOperateTypeByTimeAndCell(String systemOperateType, int slavaId,
            int cellId, String occurTime, String currentTime) {
        final String sql = "SELECT * FROM tb_log_cell_exception WHERE operateType=? and slaveId=? and cellId=?  and occuretime between ? and ? limit 1";
        return this.get(sql, systemOperateType, slavaId, cellId, occurTime, currentTime);
    }

    /*通过副柜号和格口号和发生时间以查询数据库tb_log_cell_exception是否有该格口成功放件/上架的信息******************/
    public synchronized List<CellException> getDynamicPasswordByslaveIdAndcellId(String systemOperateType, int slavaId,
            int cellId, String occurTime) {
        final String sql = "SELECT * FROM tb_log_cell_exception WHERE operateType=? and slaveId=? and cellId=?  and occuretime <?";
        return this.list(sql, systemOperateType, slavaId, cellId, occurTime);
    }

    public synchronized List<CellException> listAll() {
        final String sql = "SELECT * FROM tb_log_cell_exception order by serialId asc limit 500";
        return this.list(sql);
    }

    public synchronized boolean remove(long serialId) {
        return this.update("DELETE FROM tb_log_cell_exception WHERE serialId=?", serialId);
    }

}
