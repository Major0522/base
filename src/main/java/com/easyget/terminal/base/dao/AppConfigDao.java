package com.easyget.terminal.base.dao;

import java.util.Date;
import java.util.List;

import com.easyget.terminal.base.entity.AppConfig;
import com.easyget.terminal.base.provider.BaseDao;
import com.seagen.ecc.utils.DateUtils;

public class AppConfigDao extends BaseDao<AppConfig> {

	private static AppConfigDao INSTANCE;

	public static AppConfigDao getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AppConfigDao();
		}
		
		return INSTANCE;
	}

	public List<AppConfig> list() {
		String sql = "SELECT * FROM tb_app_config order by id asc";
		return list(sql);
	}

	public List<AppConfig> listByIds(String ids) {
		String sql = "SELECT * FROM tb_app_config where id in (" + ids + ")";
		return list(sql);
	}

	public boolean update(AppConfig config) {
		String sql = "update tb_app_config set value=?,lastUpdated=?,state=? where id=?";
		return update(sql, config.getValue(), DateUtils.datetimeToString(new Date()), config.getState(),
				config.getId());
	}

	public AppConfig load(Long id) {
		String sql = "SELECT * FROM tb_app_config where id=?";
		return get(sql, id);
	}

	public void save(AppConfig config) {
		String sql = "insert into tb_app_config(id,key,value,lastUpdated,state,remark) values(?,?,?,?,?,?);";
		update(sql, config.getId(), config.getKey(), config.getValue(), config.getLastUpdated(), config.getState(),
				config.getRemark());
	}

	public void delete(Long id) {
		String sql = "delete from  tb_app_config where id=?";
		update(sql, id);

	}

	public List<AppConfig> listNoComfirmedAlarm() {
		String sql = "SELECT * FROM tb_app_config where value='1' and state=0 and id<200000";
		return list(sql);
	}

	public String getByKey(String key) {
		String sql = "SELECT value FROM tb_app_config where key=?";
		return super.query(sql, key) + "";
	}

	public boolean updateValue(String key, String value) {
		String sql = "update tb_app_config set value=? where key=?";
		return super.update(sql, value, key);
	}
}