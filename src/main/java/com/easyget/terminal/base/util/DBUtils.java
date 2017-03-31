package com.easyget.terminal.base.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.easyget.terminal.base.dao.AppConfigDao;
import com.easyget.terminal.base.dao.DBVersionDao;

public final class DBUtils {

    private static Logger logger = Logger.getLogger(DBUtils.class);

    //如果数据库有改动，次将数据库版本加１
    private static final int DB_VERSION = 7;

    private static Connection conn;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取/创建数据库连接（AutoCommit=true）
     * @return 
     */
    public static Connection getConnection() {
        if (conn == null) {
            try {
                final String url = "jdbc:sqlite:" + Options.get("file.database");
                conn = DriverManager.getConnection(url, "root", "root");
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            conn.setAutoCommit(true);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 程序启动执行sql脚本文件
     */
    public static void updateDB() {
        final int oldDBVersion = DBVersionDao.getInstance().getDBVersion().getVersion();
        logger.info("oldDBVersion=" + oldDBVersion + ", currentDBVersion=" + DB_VERSION);

        if (oldDBVersion >= DB_VERSION) {
            logger.info("no need upgrade DB");
            return;
        }
        try {
            for (int version = oldDBVersion + 1; version <= DB_VERSION; version++) {
                final File file = new File("C:/ecc/Terminal/conf/file/v" + version);
                if (file == null || file.listFiles() == null) {
                    continue;
                }
                String[] sqlList = new String[] {};
                for (final File f : file.listFiles()) {
                    if (f.getName().endsWith(".sql")) {
                        logger.info("执行SQL脚本:" + f.getName());
                        final String text = FileUtils.readFileToString(f);
                        logger.info("text=" + text);
                        sqlList = ArrayUtils.concat(sqlList, text.split(";"));
                    }
                }
                final boolean flag = AppConfigDao.getInstance().execSqlList(Arrays.asList(sqlList));
                if (flag) {
                    file.delete();
                } else {
                    break;
                }
            }
        } catch (final Exception e) {
            logger.error("执行脚本失败", e);
        }
        DBVersionDao.getInstance().setDBVersion(DB_VERSION);
        logger.info("Update DB version to : " + DB_VERSION);
    }
}