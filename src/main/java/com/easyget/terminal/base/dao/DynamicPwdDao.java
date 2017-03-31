package com.easyget.terminal.base.dao;

import com.easyget.terminal.base.entity.DynamicPassword;
import com.easyget.terminal.base.provider.BaseDao;

public class DynamicPwdDao extends BaseDao<DynamicPassword> {

    private static DynamicPwdDao INSTANCE;
    
    private DynamicPwdDao() {
        super();
    }

    public static synchronized DynamicPwdDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DynamicPwdDao();
        }
        
        return INSTANCE;
    }

    public synchronized DynamicPassword getCountLikePwd(String str) {
    	String sql = "SELECT * FROM tb_dynamic_password WHERE dynamicPassword like '%" + str + "%' limit 1";
		return this.get(sql);
    }

    public synchronized int getDynamicPasswordCount() {
        String sql = "SELECT COUNT(*) FROM tb_dynamic_password";
        return (Integer) this.query(sql);
    }

    public synchronized DynamicPassword get() {
        DynamicPassword dyncmicPwd = this.get("select * from tb_dynamic_password limit 1");
        return dyncmicPwd;
    }

    public synchronized void add(String password) {
        String sql = "insert into tb_dynamic_password(dynamicPassword) values(?)";
        Object[] params = { password };
        this.update(sql, params);
    }
    
	public synchronized void delete(DynamicPassword pwd) {
		String sql = "delete from tb_dynamic_password where dynamicPassword=?";
        this.update(sql, pwd.getDynamicPassword());
	}
}