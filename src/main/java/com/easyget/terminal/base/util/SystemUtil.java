/*
 * 深圳壹号柜科技股份有限公司源代码，版权归深圳壹号柜科技股份有限公司所有。
 * 
 * 项目名称 : 终端项目
 * 创建日期 : 2016年3月25日
 * 修改历史 : 
 *     1. [2016年3月25日]创建文件 by niujunhong
 */
package com.easyget.terminal.base.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.easyget.terminal.base.provider.embed.EmbedPluginProvider;
import com.easyget.terminal.base.provider.embed.EmbedProvider;
import com.easyget.terminal.base.provider.page.PagePluginProvider;
import com.easyget.terminal.base.provider.page.PageProvider;
import com.easyget.terminal.base.provider.service.ServicePluginProvider;
import com.easyget.terminal.base.provider.service.ServiceProvider;

/**
 * 
 * 系统启动的时候 需要记录启动的系统信息，例如，系统环境+插件信息+服务api 信息
 * 
 * @author niujunhong
 */
public class SystemUtil {

    private static final Logger logger = Logger.getLogger(SystemUtil.class);

    /** 调用CMD执行命令，返回命令执行的结果 */
    public static String callCmd(String locationCmd) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();

        final Process child = Runtime.getRuntime().exec(locationCmd);
        final InputStream in = child.getInputStream();

        int c;

        while ((c = in.read()) != -1) {
            bos.write(c);
        }

        in.close();

        try {
            child.waitFor();
        } catch (final InterruptedException e) {
            bos.write(e.getMessage().getBytes());
            e.printStackTrace();
        }
        // System.out.println("done");

        return new String(bos.toByteArray(), "GBK");
    }

    /** 获取所有嵌入式插件提供者bean的信息 */
    public static String getEmbedProvider() {
        final StringBuilder sb = new StringBuilder();

        for (final EmbedPluginProvider pp : PluginUtil.embedLoader) {
            try {
                final EmbedProvider provider = pp.getProvider();
                sb.append(toJsonString(provider)).append("\r\n");
            } catch (final Exception ex) {
                ex.printStackTrace();
            }
        }

        return sb.toString();
    }

    /** 获取所有页面 fxml loader的信息 */
    public static String getPageFxmlLoaderMap() {
        return JSON.toJSONString(PluginUtil.pageFxmlLoaderMap, true);
    }

    /** 获取所有页面插件供应者的信息 */
    public static String getPageProvider() {
        final StringBuilder sb = new StringBuilder();

        for (final PagePluginProvider pp : PluginUtil.pageLoader) {
            try {
                final PageProvider provider = pp.getProvider();
                sb.append(toJsonString(provider)).append("\r\n");
            } catch (final Exception ex) {
                ex.printStackTrace();
            }
        }

        return sb.toString();
    }

    /** 获取属性的信息 */
    public static String getProperties(Properties p) {
        final StringBuilder sb = new StringBuilder();
        final Set<Entry<Object, Object>> pset = p.entrySet();

        for (final Entry<Object, Object> entry : pset) {
            final Object key = entry.getKey();
            final Object value = entry.getValue();
            sb.append(key + "=" + value).append("\r\n");
        }

        return sb.toString();
    }

    /** 获取发布的 服务api map的信息 */
    public static String getServiceApiMap() {
        return toJsonString(PluginUtil.serviceApiMap);
    }

    /** 获取所有服务插件供应者的信息 */
    @SuppressWarnings("rawtypes")
    public static String getServiceProvider() {
        final StringBuilder sb = new StringBuilder();

        for (final ServicePluginProvider pp : PluginUtil.serviceLoader) {
            try {
                final ServiceProvider provider = pp.getProvider();
                sb.append(toJsonString(provider)).append("\r\n");
            } catch (final Exception ex) {
                ex.printStackTrace();
            }
        }

        return sb.toString();
    }

    /** 获取系统环境变量的信息 */
    public static String getSystemEvn() {
        final StringBuilder sb = new StringBuilder();

        sb.append("<#################systemEvn#########################").append("\r\n");

        final Map<String, String> env = System.getenv();
        final Set<Entry<String, String>> set = env.entrySet();

        if (set != null && !set.isEmpty()) {
            for (final Entry<String, String> entry : set) {
                final String key = entry.getKey();
                final String value = entry.getValue();
                sb.append(key + "=" + value).append("\r\n");
            }
        }

        final Properties p = System.getProperties();
        final Set<Entry<Object, Object>> pset = p.entrySet();

        for (final Entry<Object, Object> entry : pset) {
            final Object key = entry.getKey();
            final Object value = entry.getValue();
            sb.append(key + "=" + value).append("\r\n");
        }

        sb.append("#################systemEvn#########################>").append("\r\n");

        return sb.toString();
    }

    /** <p>判断是否是通过批处理文件启动的</p>
     		<p>批处理文件中，定义了startCmd变量。</p>
     		<p>如果是批处理文件启动，代表是在终端环境下正常执行。</p>
     */
    public static boolean isBatStartup() {
        final String startCmd = System.getenv().get("startCmd");
        return StringUtils.isNotEmpty(startCmd) && startCmd.contains(".jar");
    }

    /** 打印环境变量信息 */
    public static void printSystemEvn() {
        logger.info(getSystemEvn());
    }

    /** 打印工程所有插件的fxml加载器，API等信息 */
    public static void printSystemInfo() {

        /*注意StringBuilder与String的区别*/

        final StringBuilder sb = new StringBuilder();

        /*换行处理*/
        sb.append("\r\n");

        /*得到系统的环境变量*/

        sb.append(getSystemEvn());

        sb.append("<#################PageFxmlLoaderMap#########################").append("\r\n");

        /** 获取所有页面 fxml loader的map信息，怎样理解这个 fxml loader？ */
        /**现在理解为一个每个jar包所对应的fxml文件，比如
         * "/ad/fxml/screensaver.fxml":{
        "areaList":[],
        "boot":false,
        "loader":{
            "builderFactory":{},
            "charset":"UTF-8",
            "location":"jar:file:/C:/ecc/Terminal/lib/plugin_ad-1.0.jar!/ad/fxml/screensaver.fxml",
            "namespace":{}
        }
        },下同**/

        sb.append(getPageFxmlLoaderMap());
        sb.append("#################PageFxmlLoaderMap#########################>").append("\r\n");

        sb.append("<#################PageProvider#########################").append("\r\n");
        sb.append(getPageProvider());
        sb.append("#################PageProvider#########################>").append("\r\n");

        sb.append("<#################ServiceProvider#########################").append("\r\n");
        sb.append(getServiceProvider());
        sb.append("#################ServiceProvider#########################>").append("\r\n");

        sb.append("<#################EmbedProvider#########################").append("\r\n");
        sb.append(getEmbedProvider());
        sb.append("#################EmbedProvider#########################>").append("\r\n");

        sb.append("<#################ServiceApiMap#########################").append("\r\n");
        sb.append(getServiceApiMap());
        sb.append("#################ServiceApiMap#########################>").append("\r\n");

        logger.info(sb);
    }

    /** 把对象及其属性信息输出为Json串 */
    public static String toJsonString(Object obj) {
        return (obj != null ? obj.getClass().getName() : "") + "=\r\n" + JSON.toJSONString(obj, true);
    }

    /** 提取所有的插件Jar并解压 */
    public static void unzipAllPluginConfig() {

        /*** 不是生产环境下的批处理文件启动，就直接退出，不执行后续的解压处理****/

        if (!isBatStartup()) {
            return;
        }

        //-------------------------------------------------------
        // 生产环境通过批处理文件启动，需要执行下面的解压处理
        //-------------------------------------

        // 获取ClassPath根下的资源文件
        String filePath = SystemUtil.class.getResource("/").getFile();

        try {
            filePath = java.net.URLDecoder.decode(filePath, "UTF-8");
        } catch (final Exception e) {
            e.printStackTrace();
        }

        /*在C:\ecc\Terminal\bin\startup.bat文件里，有“set CLASSPATH=C:\ecc\Terminal\conf”这么一句*/
        /*所以当前的file为C:\ecc\Terminal\conf*/

        final File file = new File(filePath);
        logger.info("unzipAllPluginConfig---->" + file);

        /*parentFile为C:\ecc\Terminal*/

        final File parentFile = file.getParentFile();
        logger.info("unzipAllPluginConfig---->" + parentFile);

        /*libDir为C:\ecc\Terminal\lib*/

        final File libDir = new File(parentFile, "lib");

        logger.info("unzipAllPluginConfig---->" + libDir);

        /*confDir为 C:\ecc\Terminal\conf*/

        final File confDir = new File(parentFile, "conf");

        /* 筛选出需要解压的jar包:需要的jar包为plugin_*.jar或者bootstrap.jar,将文件名字存于数组files里面*/
        final File[] files = libDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar") && (name.startsWith("plugin_") || name.startsWith("bootstrap"));
            }
        });

        if (files != null && files.length > 0) {
            for (final File f : files) {
                try {
                    //对单个jar文件进行解压，将jar文件里的配置文件解析出来，生成到指定目录里
                    unZipPluginJarResource(f.getAbsolutePath(), confDir);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** 对单个jar文件解压 ，/*对jar里面所有的文件信息进行枚举/遍历，筛选出自己想要的文件，并且写到指定文件夹中*/

    /*一个例子：
     * Provider.json---->base/TakeProvider.json
       jarEntry---->base/TakeProvider.json
      dimFile---->C:\ecc\Terminal\conf\base\TakeProvider.json*/
    public static void unZipPluginJarResource(String jarFileFullPath, File dimDir) throws IOException {
        JarFile jarFile = null;

        try {
            jarFile = new JarFile(jarFileFullPath);
            //打开jarFile进行读取
            logger.info("unZipPluginJarResource---->" + jarFile);

            final Enumeration<JarEntry> jarEntrys = jarFile.entries();

            while (jarEntrys.hasMoreElements()) {
                final JarEntry jarEntry = jarEntrys.nextElement();

                if (jarEntry.isDirectory()) {
                    continue;
                }

                final String name = jarEntry.getName().trim();

                if (!name.endsWith("Provider.json")) {
                    continue;
                }

                logger.info("Provider.json---->" + name);
                logger.info("jarEntry---->" + jarEntry);

                final File file = new File(dimDir, name);
                InputStream inputStream = null;

                try {
                    inputStream = jarFile.getInputStream(jarEntry);

                    logger.info("dimFile---->" + file);

                    file.getParentFile().mkdirs();
                    final byte[] bytes = new byte[inputStream.available()];

                    inputStream.read(bytes);
                    FileUtil.writeToFile(bytes, file);
                } finally {
                    FileUtil.finalClose(inputStream);
                }
            }
        } finally {
            if (jarFile != null) {
                jarFile.close();
            }
        }
    }

    private SystemUtil() {
    }
}
