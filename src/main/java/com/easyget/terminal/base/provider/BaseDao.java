package com.easyget.terminal.base.provider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;

import com.easyget.terminal.base.util.DBUtils;
import com.seagen.ecc.utils.JsonUtil;
import com.seagen.ecc.utils.StringUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class BaseDao<T>  {
	
	private static Logger logger = Logger.getLogger(BaseDao.class);

	/**
	 * 操作数据库同步对象锁
	 */
	protected final static Object lock = new Object();

	private Class<T> type;

	/**
	 * 通过反射获取子类确定的泛型类
	 */
	public BaseDao() {
		// 获取类型
		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			this.type = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
		} else {
			this.type = null;
		}
	}

	public List<T> list(String sql, Object... params) {
		showSqlOrParams(sql, params);
		synchronized (lock) {
			List<T> lists = null;
			try {
				QueryRunner run = new QueryRunner();
				lists = (List<T>) run.query(DBUtils.getConnection(), sql, 
						new BeanListHandler(type), params);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			return lists;
		}
	}
	
	public T get(String sql, Object... params) {
		showSqlOrParams(sql, params);
		synchronized (lock) {
			Object obj = null;
			try {
				QueryRunner run = new QueryRunner();
				obj = run.query(DBUtils.getConnection(), sql, new BeanHandler(type), params);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			return (T) obj;
		}
	}

	public Object query(String sql, Object... params) {
		showSqlOrParams(sql, params);
		synchronized (lock) {
			Object ret = null;
			try {
				QueryRunner runn = new QueryRunner();
				ret = runn.query(DBUtils.getConnection(), sql, new ScalarHandler(1), params);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			return ret;
		}
	}

	public boolean update(String sql, Object... params) {
		showSqlOrParams(sql, params);
		synchronized (lock) {
			Connection conn = null;
			boolean flag = false;
			try {
				conn = DBUtils.getConnection();
				QueryRunner runn = new QueryRunner();
				int i = runn.update(conn, sql, params);
				if (i > 0) {
					flag = true;
				}
			} catch (SQLException e) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			} 
			return flag;
		}
	}

	public boolean execSqlList(List<String> sqlList) {
		synchronized (lock) {
			Connection conn = null;
			boolean flag = false;
			try {
				conn = DBUtils.getConnection();
				conn.setAutoCommit(false);
				QueryRunner runn = new QueryRunner();
				for (String sql : sqlList) {
					if (StringUtils.isEmpty(sql)) {
						continue;
					}
					logger.info("execSqlList:" + sql);
					runn.update(conn, sql);
				}
				conn.commit();
				flag = true;
			} catch (SQLException e) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			} 
			return flag;
		}
	}

	protected void showSqlOrParams(String sql, Object... params) {
		String funcName = new Throwable().getStackTrace()[1].getMethodName();
		logger.info(String.format(funcName + ",sql:%s,params:%s", sql,
				JsonUtil.ojbToJsonStr(params)));
	}
}