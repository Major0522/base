package com.easyget.terminal.base.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.easyget.terminal.base.entity.RunException;
import com.easyget.terminal.base.provider.BaseDao;
import com.seagen.ecc.utils.DateUtils;

//系统运行异常日志

public class RunExceptionDao extends BaseDao<RunExceptionDao> {
   
	protected static Logger logger = Logger.getLogger(RunExceptionDao.class);

    private static RunExceptionDao INSTANCE;

    public static RunExceptionDao getInstance() {
        if (INSTANCE == null) {
        	INSTANCE = new RunExceptionDao();
        }
        
        return INSTANCE;
    }

    public RunExceptionDao() {
    }

    //增加一条记录 包括修改的字段是 serialId，content和occureTime
    public boolean add(RunException info) {
        final String sql = "INSERT INTO tb_log_run_exception(content,occureTime)VALUES(?,?)";
        return this.update(sql, info.getContent(), DateUtils.datetimeToString(new Date()));

    }

    public RunExceptionDao get(long serialId) {
        return this.get("SELECT * FROM tb_log_run_exception WHERE serialId=?", serialId);
    }

    public List<RunExceptionDao> listAll() {
        final String sql = "SELECT * FROM tb_log_run_exception order by serialId asc limit 500";
        return this.list(sql);
    }

    public boolean remove(long serialId) {
        return this.update("DELETE FROM tb_log_run_exception WHERE serialId=?", serialId);
    }
}
