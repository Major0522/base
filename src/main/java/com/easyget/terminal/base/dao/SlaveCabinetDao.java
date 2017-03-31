package com.easyget.terminal.base.dao;

import java.util.List;

import com.easyget.terminal.base.entity.SlaveCabinet;
import com.easyget.terminal.base.provider.BaseDao;

public class SlaveCabinetDao extends BaseDao<SlaveCabinet> {

	private static SlaveCabinetDao INSTANCE;

	public static SlaveCabinetDao getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SlaveCabinetDao();
		}

		return INSTANCE;
	}

	public List<SlaveCabinet> queryAll() {
		String sql = "select * from tb_slave_cabinet where state<>3 order by slaveId asc";
		return super.list(sql);
	}

	public void update(SlaveCabinet slaveCabinet) {
		String sql = "update tb_slave_cabinet set slaveAddress=?,cellCount=?,slaveName=?,appVersion=?,model=?,state=? where slaveId=?";
		super.update(sql, slaveCabinet.getSlaveAddress(), slaveCabinet.getCellCount(), slaveCabinet.getSlaveName(),
				slaveCabinet.getAppVersion(), slaveCabinet.getModel(), slaveCabinet.getState(),
				slaveCabinet.getSlaveId());
	}

	public SlaveCabinet load(Integer slaveId) {
		String sql = "select * from tb_slave_cabinet where slaveId=?";
		return super.get(sql, slaveId);
	}

	public SlaveCabinet get(String slaveAddress) {
		String sql = "select * from tb_slave_cabinet where slaveAddress=?";
		return super.get(sql, slaveAddress);
	}

	public void delete(Integer slaveId) {
		String sql = "delete from tb_slave_cabinet where slaveId=?";
		super.update(sql, slaveId);

	}

	public void save(SlaveCabinet slaveCabinet) {
		String sql = "insert into tb_slave_cabinet (slaveId, slaveAddress,cellCount,slaveName,appVersion,model,state,slaveType) values(?,?,?,?,?,?,?,?)";
		super.update(sql, slaveCabinet.getSlaveId(), slaveCabinet.getSlaveAddress(), slaveCabinet.getCellCount(),
				slaveCabinet.getSlaveName(), slaveCabinet.getAppVersion(), slaveCabinet.getModel(),
				slaveCabinet.getState(), slaveCabinet.getSlaveType());
	}

	public List<SlaveCabinet> queryByState(int state) {
		String sql = "select * from tb_slave_cabinet where state=?";
		return super.list(sql, state);
	}

	public List<SlaveCabinet> queryByType(int slaveType) {
		String sql = "select * from tb_slave_cabinet where slaveType=?";
		return super.list(sql, slaveType);
	}

	public boolean updateState(int slaveId, int state) {
		String sql = "update tb_slave_cabinet set state=? where slaveId=?";
		return super.update(sql, state, slaveId);
	}

	public List<SlaveCabinet> list(int state, int type) {
		String sql = "SELECT * FROM tb_slave_cabinet WHERE state=? AND slaveType=? ORDER BY slaveId asc";
		return super.list(sql, state, type);
	}

	public List<SlaveCabinet> queryAllSlave() {
		String sql = "select * from tb_slave_cabinet where state<>4 order by slaveId asc";
		return super.list(sql);
	}
}