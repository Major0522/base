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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.easyget.terminal.base.model.CallResult;
import com.easyget.terminal.base.provider.service.Api;
import com.easyget.terminal.base.provider.service.ServiceApi;
import com.easyget.terminal.base.provider.service.ServiceProvider;

/**
 * <p>
 *    服务工具类
 *    功能类同于MessageHandleUtil，创建所有的serviceapi处理器，并放置到Map中待用
 * </p> 
 * 
 * @author niujunhong
 */
@SuppressWarnings("rawtypes")
public class ServiceUtil {
    /**log4j 日志*/
    private static final Logger logger = Logger.getLogger(ServiceUtil.class);

    /**执行服务api线程池*/
    public static final ExecutorService service = Executors.newCachedThreadPool();

    /**创建处理器，并放到MAP中待用*/
    private static final Map<String, Method> objectMethodMap = new HashMap<String, Method>();

    static {
        final Method[] methods = Object.class.getMethods();
        for (final Method method : methods) {
            objectMethodMap.put(method.toGenericString().trim(), method);
        }
    }

    /**
     * 
     * 调用服务，采用松散的方法调用，返回值也是松散的
     * @param serviceName
     * @param prameters
     * @return
     */
    public static CallResult callServiceApi(String serviceName, Object... prameters) {
        final CallResult t = new CallResult();
        try {
            final ServiceApi serviceApi = getServiceApiByServiceName(serviceName);
            final Class<?> cls = serviceApi.getCls();
            final Object serviceObj = cls.newInstance();
            final Object result = serviceApi.getMethod().invoke(serviceObj, prameters);
            t.setStatus(CallResult.STATUS_SUCCESS);
            t.setException(null);

            if (result != null) {
                t.setData(result);
                t.setDataCls(result.getClass().getName());
                /*调用该API，执行到这里应该是执行成功的*/
            }
        } catch (final Throwable ex) {
            logger.error(ex);
            t.setStatus(CallResult.STATUS_ERROR);
            t.setException(ex);
            /*调用该API，执行失败*/
        }
        return t;
    }

    public static ServiceApi getServiceApiByServiceName(String serviceName) {
        if (StringUtils.isEmpty(serviceName)) {
            return null;
        }
        return PluginUtil.getServiceapimap().get(serviceName.trim());
    }

    public static final Map<String, ServiceApi> getServiceApiMap() {
        final Map<String, ServiceApi> serviceApiMap = new ConcurrentHashMap<String, ServiceApi>();
        /*遍历所有的ServiceApi*/
        final Set<Class<?>> classes = ClassUtil.getClasses("com.easyget");
        for (final Class<?> cls : classes) {
            final Api an = cls.getAnnotation(Api.class);
            if (an == null) {
                continue;
            }
            /*如果未激活，跳过*/
            if (!an.isEnable()) {
                continue;
            }
            final Method[] methods = cls.getMethods();
            if (methods != null && methods.length > 0) {
                for (final Method method : methods) {
                    final String key = method.toGenericString().trim();
                    //FIXME 忽略掉Object 类的方法
                    if (objectMethodMap.containsKey(key)) {
                        continue;
                    }
                    /*保证健值的唯一性*/

                    final ServiceApi api = new ServiceApi(method, cls);
                    final String serviceName = cls.getSimpleName() + "@" + method.getName();
                    serviceApiMap.put(serviceName, api);
                }
            }
        }
        return serviceApiMap;
    }

    /*启动服务式插件中的各个线程*/
    public static void runService() {
        try {
            final TreeSet<ServiceProvider> asynSet = PluginUtil.getAsyncserviceprovider();
            if (asynSet != null) {
                // 异步处理，通过线程来运行。
                for (final ServiceProvider s : asynSet) {
                    try {
                        //FIXME used Thread!
                        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                s.doCall();
                            }
                        });
                    } catch (final Exception ex) {
                        ex.printStackTrace();
                        logger.error(ex);
                    }
                }
            }

            final TreeSet<ServiceProvider> syncSet = PluginUtil.getSyncserviceprovider();
            if (syncSet != null) {
                // 同步处理，顺序执行。
                for (final ServiceProvider s : syncSet) {
                    try {
                        s.doCall();
                    } catch (final Exception ex) {
                        logger.error(ex);
                    }
                }
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            logger.error(ex);
        }
    }

    private ServiceUtil() {
    }
}