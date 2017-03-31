/*
 * 深圳壹号柜科技股份有限公司源代码，版权归深圳壹号柜科技股份有限公司所有。
 * 
 * 项目名称 : 终端项目
 * 创建日期 : 2016年3月25日
 * 修改历史 : 
 *     1. [2016年3月25日]创建文件 by niujunhong
 */
package com.easyget.terminal.base.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.easyget.terminal.base.provider.embed.EmbedPluginProvider;
import com.easyget.terminal.base.provider.embed.EmbedProvider;
import com.easyget.terminal.base.provider.embed.Insert;
import com.easyget.terminal.base.provider.page.PagePluginProvider;
import com.easyget.terminal.base.provider.page.PageProvider;
import com.easyget.terminal.base.provider.page.ShortCut;
import com.easyget.terminal.base.provider.service.ServiceApi;
import com.easyget.terminal.base.provider.service.ServiceCall;
import com.easyget.terminal.base.provider.service.ServicePluginProvider;
import com.easyget.terminal.base.provider.service.ServiceProvider;

import javafx.fxml.FXMLLoader;

/**
 * <p>
 * 插件工具类，将各种插件的<k,V>存入到相应的处理器MAP中待用。
 * </p>
 * 
 * @author niujunhong
 */
@SuppressWarnings("rawtypes")
public class PluginUtil {
    /** log4j 日志 */
    private static final Logger logger = Logger.getLogger(PluginUtil.class);

    /** 系统启动 xml path */
    private static String bootKey;

    /** 所有页面 fxml loader */
    public final static Map<String, Page> pageFxmlLoaderMap = new ConcurrentHashMap<String, Page>();

    /** 嵌入式插件供应者 */
    public static final ServiceLoader<EmbedPluginProvider> embedLoader = ServiceLoader.load(EmbedPluginProvider.class);

    /** 页面插件供应者 */
    public static final ServiceLoader<PagePluginProvider> pageLoader = ServiceLoader.load(PagePluginProvider.class);

    /** 服务插件供应者 */
    public static final ServiceLoader<ServicePluginProvider> serviceLoader = ServiceLoader
            .load(ServicePluginProvider.class);

    /** 异步服务插件供应者 */
    private static final TreeSet<ServiceProvider> asyncServiceProvider = new TreeSet<ServiceProvider>();

    /** 同步服务插件供应者 */
    private static final TreeSet<ServiceProvider> syncServiceProvider = new TreeSet<ServiceProvider>();

    /** 发布的 服务api map */
    public final static Map<String, ServiceApi> serviceApiMap = ServiceUtil.getServiceApiMap();

    public static TreeSet<ServiceProvider> getAsyncserviceprovider() {
        return asyncServiceProvider;
    }

    /** 获取首页 */
    public static Page getBootPage() {
        // FIXME -- 首页资源只有一个，启动后应该已经固定下来了，实际不需要这样通过名称去获取，建议修改
        return getPageByKey(bootKey);
    }

    public static Page getPageByKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        return pageFxmlLoaderMap.get(key.trim());
    }

    public static Map<String, ServiceApi> getServiceapimap() {
        return serviceApiMap;
    }

    public static TreeSet<ServiceProvider> getSyncserviceprovider() {
        return syncServiceProvider;
    }

    public static void init() {
        processPagePluginProvider();
        processEmbedPluginProvider();
        processServicePluginProvider();
        processPagePluginProviderShortCut();
    }

    public static void processEmbedPluginProvider() {
        if (embedLoader != null) {
            for (final EmbedPluginProvider pp : embedLoader) {
                try {
                    final EmbedProvider provider = pp.getProvider();
                    logger.debug("provider:" + provider);
                    if (provider == null) {
                        continue;
                    }
                    provider.init();

                    final FXMLLoader loader = provider.getFxmlLoader();
                    final List<Insert> insertList = provider.getInsertList();
                    if (insertList == null || insertList.isEmpty()) {
                        continue;
                    }
                    for (final Insert insert : insertList) {
                        final String parent = insert.getParent();
                        if (StringUtils.isEmpty(parent)) {
                            continue;
                        }
                        final Page page = pageFxmlLoaderMap.get(parent);
                        if (page == null) {
                            continue;
                        }
                        List<Area> areaList = page.getAreaList();
                        if (areaList == null) {
                            areaList = new ArrayList<Area>();
                        }
                        final Area area = new Area(loader, insert.getLayoutX(), insert.getLayoutY(),
                                insert.getPluginInitConfig());

                        if (insert.getLevel() == Integer.MAX_VALUE) {
                            areaList.add(area);
                        } else {
                            areaList.add(insert.getLevel(), area);
                        }
                        page.setAreaList(areaList);
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void processPagePluginProvider() {
        if (pageLoader != null) {
            for (final PagePluginProvider pp : pageLoader) {
                try {
                    final PageProvider provider = pp.getProvider();
                    logger.debug("provider:" + provider);
                    if (provider == null) {
                        continue;
                    }
                    provider.init();
                    final boolean isBoot = provider.isBoot();
                    if (isBoot) {
                        final FXMLLoader mainLoader = provider.getMainLoader();
                        if (mainLoader != null) {
                            bootKey = FxmlUtil.getKey(mainLoader);
                        }
                    }
                    final List<FXMLLoader> exportLoaderList = provider.getExportLoaderList();
                    if (exportLoaderList != null && !exportLoaderList.isEmpty()) {
                        for (final FXMLLoader fxmlLoader : exportLoaderList) {
                            putAndGetExportLoader(fxmlLoader);
                        }
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void processPagePluginProviderShortCut() {
        if (pageLoader != null) {
            for (final PagePluginProvider pp : pageLoader) {
                try {
                    final PageProvider provider = pp.getProvider();
                    logger.debug("provider:" + provider);
                    if (provider == null) {
                        continue;
                    }
                    final FXMLLoader mainLoader = provider.getMainLoader();
                    if (mainLoader == null) {
                        continue;
                    }
                    final List<ShortCut> shortCutList = provider.getShortCutList();
                    logger.debug("shortCutList:" + shortCutList);
                    if (shortCutList != null && !shortCutList.isEmpty()) {
                        for (final ShortCut shortCut : shortCutList) {
                            putShortCutToDimPage(mainLoader, shortCut);
                        }
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void processServicePluginProvider() {
        if (serviceLoader != null) {
            for (final ServicePluginProvider pp : serviceLoader) {
                try {
                    final ServiceProvider provider = pp.getProvider();
                    logger.debug("provider:" + provider);
                    if (provider == null) {
                        continue;
                    }
                    provider.init();
                    final Method method = provider.getClass().getMethod("doCall");
                    final ServiceCall serviceCall = method.getAnnotation(ServiceCall.class);
                    boolean isAsync = false;
                    if (serviceCall != null) {
                        isAsync = serviceCall.isAsync();
                    }
                    if (isAsync) {
                        asyncServiceProvider.add(provider);
                    } else {
                        syncServiceProvider.add(provider);
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Page putAndGetExportLoader(FXMLLoader xmlLoader) {
        final String key = FxmlUtil.getKey(xmlLoader);
        final List<Area> areaList = new ArrayList<Area>();
        final Page page = new Page(xmlLoader, areaList, null);
        pageFxmlLoaderMap.put(key.trim(), page);
        return page;
    }

    private static void putShortCutToDimPage(FXMLLoader xmlLoader, ShortCut shortCut) {
        final String key = shortCut.getParent();
        final Page page = pageFxmlLoaderMap.get(key);
        if (page == null) {
            logger.warn("dim page is null,this ShortCut->" + shortCut + " ignro!");
            return;
        }

        String self = shortCut.getSelf();
        if (StringUtils.isEmpty(self)) {
            self = FxmlUtil.getKey(xmlLoader);
        }

        final Page srcPage = pageFxmlLoaderMap.get(self);
        if (srcPage == null) {
            logger.warn(
                    "src page is null,this xmlLoader->" + xmlLoader.getLocation().toString() + ";this ShortCut->"
                            + shortCut + " ignro!");
        }
        shortCut.setSelf(self);

        List<ShortCut> shortCutList = page.getShortCutList();
        if (shortCutList == null) {
            shortCutList = new ArrayList<ShortCut>();
        }

        shortCutList.add(shortCut);
        page.setShortCutList(shortCutList);
        pageFxmlLoaderMap.put(key.trim(), page);
    }

    private PluginUtil() {
    }
}
