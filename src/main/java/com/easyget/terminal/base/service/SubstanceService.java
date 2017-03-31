package com.easyget.terminal.base.service;

import org.apache.log4j.Logger;

import com.easyget.terminal.base.dao.CellDao;
import com.easyget.terminal.base.dao.SubstanceDao;
import com.easyget.terminal.base.entity.Substance;
import com.easyget.terminal.base.model.CallResult;
import com.easyget.terminal.base.model.Result;
import com.easyget.terminal.base.util.Const;
import com.easyget.terminal.base.util.ServiceUtil;
import com.seagen.ecc.utils.StringUtils;

public class SubstanceService {

	private static Logger logger = Logger.getLogger(SubstanceService.class);

	private static SubstanceService INSTANCE;

	public static SubstanceService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SubstanceService();
		}
		return INSTANCE;
	}

	public Result validateTakeCode(String takeCode) {
		logger.info("SubstanceService.validateTakeCode(),takeCode:" + takeCode);
		Result result = new Result();

		if (StringUtils.isEmpty(takeCode) || takeCode.length() != 6) {
			result.setValue("请输入6位提取码");
		} else {
			Substance substance = SubstanceDao.getInstance().getByTakeCode(takeCode);
			if (substance == null) {
				result.setValue("提取码无效，请重新输入");
			} else {
				CallResult callResult = null;
				if (substance.getType() == Const.BUSSINESS_TYPE_ACTIVITY) {
					callResult = ServiceUtil.callServiceApi("DistributeServiceApi@validateTrading", substance.getSubstanceId());
					
				} else if (substance.getType() == Const.BUSSINESS_TYPE_EXPRESS) {
					callResult = ServiceUtil.callServiceApi("ExpressServiceApi@validateTrading", substance.getSubstanceId());
				}
				
				if (callResult != null && callResult.getStatus() == CallResult.STATUS_SUCCESS) {
					Result ret = (Result) callResult.getData();
					if (!ret.isSuccess()) {
						result.setValue(ret.getValue());
					} else {
						result.setSuccess(true);
						result.setValue(substance);
					}
				}else{
					result.setValue("无交易记录");
				}
			}
		}
		return result;
	}

	public void take(Substance substance) {
		logger.info("SubstanceService.validateTakeCode(),substanceId:" + substance.getSubstanceId());

		if (SubstanceDao.getInstance().takeOff(substance.getSubstanceId())) {
			// 更改格口状态
			CellDao.getInstance().updateState(substance.getCellId(), substance.getSlaveId(), Const.CELL_STATE_UNUSE);

			if (substance.getType() == Const.BUSSINESS_TYPE_ACTIVITY) { // 样品分发业务
				ServiceUtil.callServiceApi("DistributeServiceApi@take", substance.getSubstanceId());
			} else if (substance.getType() == Const.BUSSINESS_TYPE_EXPRESS) { // 快递业务
				ServiceUtil.callServiceApi("ExpressServiceApi@take", substance.getSubstanceId());
			}
		}
	}
}