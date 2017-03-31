package com.easyget.terminal.base.dao;

import java.util.List;

import com.easyget.terminal.base.entity.Substance;
import com.easyget.terminal.base.provider.BaseDao;
import com.easyget.terminal.base.util.Const;

public class SubstanceDao extends BaseDao<Substance> {

	private static SubstanceDao INSTANCE;

	public static SubstanceDao getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SubstanceDao();
		}

		return INSTANCE;
	}

	public Substance getById(String substanceId) {
		String sql = "SELECT * FROM tb_substance WHERE substanceId=?";
		return this.get(sql, substanceId);
	}

	public Substance getDistributeSubstance(int slaveId, int cellId) {
		String sql = "SELECT * FROM tb_substance WHERE slaveId=? AND cellId=? AND state=? AND type=?";
		return this.get(sql, slaveId, cellId, Const.TRADING_STATE_SAVING, Const.BUSSINESS_TYPE_ACTIVITY);
	}

	public List<Substance> list(int state, int type) {
		String sql = "select * from tb_substance where state=? and type=?";
		return super.list(sql, state, type);
	}

	public boolean takeOff(String substanceId) {
		String sql = "UPDATE tb_substance SET state=? WHERE substanceId=?";
		return this.update(sql, Const.TRADING_STATE_TAKEOFF, substanceId);
	}

	public boolean add(Substance substance) {
		Substance s = this.get("SELECT * FROM tb_substance WHERE slaveId=? AND cellId=? AND state=?",
				substance.getSlaveId(), substance.getCellId(), substance.getState());
		if (s != null) {
			return false;
		}
		String sql = "INSERT INTO tb_substance " + "(substanceId,slaveId,cellId,state,type,dynamicPwd,inputTime) "
				+ "VALUES(?,?,?,?,?,?,?)";
		Object[] params = { substance.getSubstanceId(), substance.getSlaveId(), substance.getCellId(),
				substance.getState(), substance.getType(), substance.getDynamicPwd(), substance.getInputTime() };
		return this.update(sql, params);
	}

	public synchronized Substance getCountLikePwd(String str) {
		String sql = "SELECT * FROM tb_substance WHERE dynamicPwd like '%" + str + "%' limit 1";
		return this.get(sql);
	}

	public boolean isUsed(int slaveId, int cellId) {
		String sql = "SELECT * FROM tb_substance WHERE slaveId=? AND cellId=? AND state=?";
		Substance substance = this.get(sql, slaveId, cellId, Const.TRADING_STATE_SAVING);
		if (substance != null) {
			return true;
		}
		return false;
	}

	public Substance getByTakeCode(String takeCode) {
		String sql = "SELECT * FROM tb_substance WHERE dynamicPwd=?";
		return this.get(sql, takeCode);
	}

	public void delete(String date) {
		String sql = "DELETE FROM tb_substance WHERE state=? AND inPutTime<?";
		this.update(sql, Const.TRADING_STATE_TAKEOFF, date);
	}
}
