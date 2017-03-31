package com.easyget.terminal.base.util;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.easyget.terminal.base.provider.PageManager;
import com.easyget.terminal.base.provider.PluginManager;
import com.easyget.terminal.base.provider.page.ShortCut;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * 终端应用工具类
 */
public class PageUtil {
	/** log4j 日志 */
	private static final Logger logger = Logger.getLogger(PageUtil.class);

	private static String BACK_GROUND = "-fx-background-image: url('base/images/main.png');";

	/** 判断是否首页正在显示中 */
	public static boolean bootShowing(){
		Page boot = PluginUtil.getBootPage();
		Page curr = ContextUtil.getCurrentPage();
		
		return curr.equals(boot);
	}
	
	/**
	 * 
	 * 组装页面
	 * 
	 * @param page
	 * @param root
	 * @throws Exception
	 */
	private static void asmPage(Page page, Pane root) throws Exception {
		final List<Area> areaList = page.getAreaList();
		if (areaList != null && !areaList.isEmpty()) {
			for (final Area area : areaList) {
				final FXMLLoader areaLoader = area.getLoader();
				if (areaLoader == null) {
					continue;
				}
				areaLoader.setRoot(null);
				areaLoader.setController(null);
				final Pane areaPane = (Pane) areaLoader.load();
				areaPane.setLayoutX(area.getLayoutX());
				areaPane.setLayoutY(area.getLayoutY());
				root.getChildren().add(areaPane);
			}
		}

		final List<ShortCut> shortCutList = page.getShortCutList();
		if (shortCutList != null && !shortCutList.isEmpty()) {
			for (final ShortCut sc : shortCutList) {
				final ImageView icon = new ImageView();
				icon.setImage(new Image(sc.getIcon()));
				icon.setLayoutX(sc.getLayoutX());
				icon.setLayoutY(sc.getLayoutY());
				icon.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						final String location = sc.getSelf();
						logger.debug("TODO,jump to ->" + location);
						PageUtil.jumpToPage(location);
					}
				});
				root.getChildren().add(icon);
			}
		}
	}

	private static void callOnShowPageComplete(final FXMLLoader loader, final Page currentPage,
			final Stage currentStage) {
		if (loader == null) {
			return;
		}
		final Object controller = loader.getController();
		if (controller == null) {
			return;
		}
		if (controller instanceof PageManager) {
			final PageManager pageShow = (PageManager) controller;
			try {
				pageShow.onShow();
			} catch (final Exception e) {
				logger.error("callOnShowPageComplete exception", e);
			}
		}
	}

	/**
	 * 
	 * 调用插件初始化，给插件传递初始化参数，初始化插件
	 * 
	 * @param page
	 * @param root
	 * @throws Exception
	 */
	private static void callPluginInitConfig(Page page, Pane root) throws Exception {
		final List<Area> areaList = page.getAreaList();
		if (areaList != null && !areaList.isEmpty()) {
			for (final Area area : areaList) {
				final FXMLLoader areaLoader = area.getLoader();
				if (areaLoader == null) {
					continue;
				}
				final Object controller = areaLoader.getController();
				if (controller == null) {
					continue;
				}
				if (controller instanceof PluginManager) {
					final Map<String, String> pluginConfig = area.getPluginConfig();
					((PluginManager) controller).initCall(pluginConfig);
				}
			}
		}
	}

	/**
	 * 跳转到另外一个页面
	 * 
	 * @param location
	 */
	public static void jumpToPage(String location) {
		String currentLocation = "<none>";
		final Page currentPage = ContextUtil.getCurrentPage();
		
		if (currentPage != null) {
			final FXMLLoader loader = currentPage.getLoader();
			currentLocation = FxmlUtil.getKey(loader);
		}
		
		logger.info("jump from " + currentLocation + "  to ->" + location);
		
		try {
			final Page page = PluginUtil.getPageByKey(location);
		
			show(page);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 显示启动主界面
	 * 
	 * @param stage
	 * @throws Exception
	 */
	public static void showBootStrap() throws Exception {
		final Page bootPage = PluginUtil.getBootPage();
		if (bootPage == null) {
			try {
				throw new Exception(
						"没有启动主界面(no boot page)!请设置一个页面为启动主界面（Please set a page to start the main interface）");
			} catch (final Exception ex) {
				ex.printStackTrace();
				logger.error("showBootStrap error:", ex);
				System.exit(-1);
			}
		}
		show(bootPage);
	}

	/**
	 * 
	 * 显示页面
	 * 
	 * @param page
	 * @throws Exception
	 */
	private static void show(final Page page) throws Exception {
		final FXMLLoader loader = page.getLoader();
		Pane pane = page.getPane();
		if (pane == null) {
			loader.setRoot(null);
			loader.setController(null);
			pane = loader.load();
			pane.setStyle(BACK_GROUND);
			page.setPane(pane);
			asmPage(page, pane);
		}
		callPluginInitConfig(page, pane);
		Scene scene = pane.getScene();
		if (scene == null) {
			scene = new Scene(pane);
		}
		if (!ApplicationUtil.isDebug()) {
			scene.setCursor(Cursor.NONE);
		}
		ContextUtil.getPrimaryStage().setScene(scene);
		ContextUtil.getPrimaryStage().show();
		ContextUtil.setCurrentPage(page);
		callOnShowPageComplete(loader, page, ContextUtil.getPrimaryStage());
	}
}