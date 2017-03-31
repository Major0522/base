package com.easyget.terminal.base.dao;

import java.util.List;

import com.easyget.terminal.base.entity.Report;
import com.easyget.terminal.base.provider.BaseDao;

public class ReportDao extends BaseDao<Report> {

	private static ReportDao INSTANCE;

	public static ReportDao getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ReportDao();
		}

		return INSTANCE;
	}

	public boolean add(Report info) {
		String sql = "INSERT INTO tb_report(serialId,content)VALUES(?,?)";
		return this.update(sql, info.getSerialId(), info.getContent());
	}

	public List<Report> listAll() {
		String sql = "SELECT * FROM tb_report order by serialId asc limit 500";// 注意先后顺序，最早的先发
		return this.list(sql);
	}

	public boolean remove(long serialId) {
		return this.update("DELETE FROM tb_report WHERE serialId=?", serialId);
	}

	public Report get(long serialId) {
		return this.get("SELECT * FROM tb_report WHERE serialId=?", serialId);
	}
}