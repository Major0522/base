package com.easyget.terminal.base.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.easyget.terminal.base.entity.Member;
import com.easyget.terminal.base.provider.PageManager;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

/**
 * <p>
 * 上下文工具类
 * </p>
 */
public class ContextUtil {

	/** log4j 日志 */
	private static final Logger logger = Logger.getLogger(ContextUtil.class);

	/**
	 * 当前操作页面
	 */
	private static Page currentPage;

	/**
	 * 当前登录用户
	 */
	private static Member currentMember;

	/**
	 * 当前Stage
	 */
	private static Stage primaryStage;

	public static void callControllerDestory(FXMLLoader loader) {
		if (loader == null) {
			return;
		}
		final Object areaController = loader.getController();
		if (areaController == null) {
			return;
		}
		if (areaController instanceof PageManager) {
			final PageManager manager = (PageManager) areaController;
			try {
				manager.onLeave();
			} catch (final Exception e) {
				logger.error("Leave exception", e);
			}
		}
	}

	public static Member getCurrentMember() {
		return ContextUtil.currentMember;
	}

	public static Page getCurrentPage() {
		return currentPage;
	}

	/**
	 * 终端机操作程序的主Stage
	 */
	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void setCurrentMember(Member currentMember) {
		ContextUtil.currentMember = currentMember;
	}

	public static void setCurrentPage(Page currentPage) {
		// 销毁旧资源资源
		final Page oldPage = ContextUtil.currentPage;
		if (oldPage != null) {
			final List<Area> areaList = oldPage.getAreaList();
			if (areaList != null && !areaList.isEmpty()) {
				for (final Area area : areaList) {
					if (area == null) {
						continue;
					}
					final FXMLLoader areaLoader = area.getLoader();
					callControllerDestory(areaLoader);
				}
			}
			final FXMLLoader oldLoader = oldPage.getLoader();
			callControllerDestory(oldLoader);

		}
		ContextUtil.currentPage = currentPage;
	}

	public static void setPrimaryStage(Stage currentStage) {
		ContextUtil.primaryStage = currentStage;
	}
}