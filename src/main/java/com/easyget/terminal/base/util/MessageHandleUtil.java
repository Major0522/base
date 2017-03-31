package com.easyget.terminal.base.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.easyget.terminal.base.provider.service.McsMessageHandler;
import com.easyget.terminal.base.provider.service.MessageHandler;
import com.seagen.ecc.ectcps.protocol.CommandMessage;
import com.seagen.ecc.ectcps.protocol.McsMessage;

/**这个文件以下几个作用：
 *1,  创建消息处理器并放置到Map中待用*
  2,发送后台消息和模块控制消息处理*/

public class MessageHandleUtil {

    private static Logger logger = Logger.getLogger(MessageHandleUtil.class);

    /*用于后台消息处理的map*/
    private static Map<Integer, Method> messageHandlerMap;

    /*用于模块控制消息处理的map*/
    private static Map<Integer, Method> mcsMessageHandlerMap;

    public static void initMessageHandlerMap() {
        /*创建后台消息处理map*/
        messageHandlerMap = new HashMap<Integer, Method>();

        /*创建模块控制消息处理map*/
        mcsMessageHandlerMap = new HashMap<Integer, Method>();

        /*获取所有类名包含“com.easyget”的类*/
        final Set<Class<?>> classes = ClassUtil.getClasses("com.easyget");
        for (final Class<?> cls : classes) {
            final Method[] methods = cls.getDeclaredMethods();
            if (methods == null || methods.length == 0) {
                continue;
            }

            /*先获取methods，再获取functioned，利用了反射*/
            for (final Method method : methods) {

                final MessageHandler handler = method.getAnnotation(MessageHandler.class);
                if (handler != null) {
                    final int functionCode = handler.functionCode();
                    if (functionCode == 0) {
                        continue;
                    }
                    messageHandlerMap.put(functionCode, method);
                    continue;
                }
                final McsMessageHandler mcsHandler = method.getAnnotation(McsMessageHandler.class);
                if (mcsHandler != null) {
                    final int functionCode = mcsHandler.functionCode();
                    if (functionCode == 0) {
                        continue;
                    }
                    mcsMessageHandlerMap.put(functionCode, method);
                }
            }
        }
    }

    public static void messageHandOut(CommandMessage cmd) {
        final int functionCode = cmd.getFunctionCode();
        final Method method = messageHandlerMap.get(functionCode);
        if (method != null) {
            try {
                logger.info("功能码=" + functionCode);
                method.invoke(null, cmd);//反射
            } catch (final Exception e) {
                logger.error(e.toString());
            }
        } else {
            logger.warn("Method not fond!!! FunctionCode = " + functionCode);
        }
    }

    public static void messageHandOut(McsMessage cmd) {
        final int functionCode = cmd.getFunctionCode();
        final Method method = mcsMessageHandlerMap.get(functionCode);
        if (method != null) {
            try {
                method.invoke(null, cmd);//反射
            } catch (final Exception e) {
                logger.error(e.toString());
            }
        } else {
            logger.warn("Method not fond!!! FunctionCode = " + functionCode);
        }
    }
}