package com.easyget.terminal.base.dao;

import org.apache.log4j.Logger;

import com.easyget.terminal.base.entity.DBVersion;
import com.easyget.terminal.base.provider.BaseDao;

public class DBVersionDao extends BaseDao<DBVersion> {

    private static Logger logger = Logger.getLogger(DBVersionDao.class);
   
    private static DBVersionDao INSTANCE;
   
    public static DBVersionDao getInstance() {
        if (INSTANCE == null) {
        	INSTANCE = new DBVersionDao();
        }
        
        return INSTANCE;
    }

    private DBVersionDao() {
        super();
        
        if (!checkTableExist()) {
            boolean result = this.update("create table tb_db_version(version integer primary key)");
            logger.info("create tb_db_version table : " + result);
        }

        if (!checkDBVersionExist()) {
            boolean result = this.update("insert into tb_db_version values(0)");
            logger.info("insert default tb_db_version to table : " + result);
        }

    }

    public DBVersion getDBVersion() {
        return this.get("select * from tb_db_version");
    }

    public void setDBVersion(int newVersion) {
        this.update("update tb_db_version set version=?", newVersion);
    }

    private boolean checkTableExist() {
    	String sql = "select count(*) as exist from sqlite_master where type='table' and name='tb_db_version'";
        int count = (int)this.query(sql);
        
        return count > 0 ? true : false;
    }

    private boolean checkDBVersionExist() {
    	String sql = "select count(*) as exist from tb_db_version";
        int count = (int)this.query(sql);
        
        return count > 0 ? true : false;
    }
    
}