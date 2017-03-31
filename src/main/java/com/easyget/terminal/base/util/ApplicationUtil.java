package com.easyget.terminal.base.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.easyget.terminal.base.communication.BcsHandler;
import com.easyget.terminal.base.communication.BscCommandSender;
import com.easyget.terminal.base.module.McsHandler;
import com.easyget.terminal.base.service.LogAutoAnalysisService;
import com.easyget.terminal.base.task.DataClear;
import com.easyget.terminal.base.task.TakeCodeTask;
import com.seagen.ecc.common.vo.ProgramInfo;
import com.seagen.ecc.common.win.TaskManage;
import com.seagen.ecc.common.win.WinCmd;
import com.seagen.ecc.utils.JsonUtil;
import com.seagen.ecc.utils.StringUtils;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class ApplicationUtil {

    private static Logger logger = Logger.getLogger(ApplicationUtil.class);

    /**
     * 终端系统当前是否使用中
     */
    private static boolean isUsing = true;

    private static int freeTime = 20;

    /**
     * 拷贝更新程序到指定目录
     */
    private static void copyJar2UpdateDir() {
        final File updateFile = new File("C:/ecc/Terminal/conf/file/ecc_am.jar");
        try {
            FileUtils.copyFileToDirectory(updateFile, new File(Options.getString("dir.updateDir")));
        } catch (final IOException e) {
            logger.error("拷贝更新文件失败", e);
        }
    }

    /**
     * 创建守护进程信息
     */
    public static void createDaemonInfo() {
        try {
            final List<ProgramInfo> list = new ArrayList<ProgramInfo>();
            for (int i = 0; i <= Short.BYTES; i++) {
                final String prefix = "daemon." + i + ".";
                final Map<String, String> map = Options.getAllWithStart(prefix);
                if (map.size() == 0) {
                    break;
                }
                final ProgramInfo pi = new ProgramInfo();
                pi.setProcess(map.get(prefix + "process"));
                pi.setStartFilePath(map.get(prefix + "startFilePath"));
                list.add(pi);
            }
            final ProgramInfo[] pis = list.toArray(new ProgramInfo[list.size()]);
            FileUtils
                    .write(new File(Options.getString("dir.updateDir"), "daemoning"), JsonUtil.ojbToJsonPrettyStr(pis));
        } catch (final IOException e) {
            logger.error("写入守护进程信息失败", e);
        }
    }

    /**
     * 程序启动执行bat脚本文件
     */
    public static void execBat() {
        final File file = new File("C:/ecc/Terminal/conf/file");
        try {
            for (final File f : file.listFiles()) {
                if (!f.getName().endsWith(".bat")) {
                    continue;
                }
                logger.info("执行BAT脚本:" + f.getName());
                WinCmd.execBat(f);
                for (int i = 0; i < 20; i++) {// 等待10秒
                    if (!f.exists()) {
                        break;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                f.delete();
            }
        } catch (final Exception e) {
            logger.error("执行脚本失败", e);
        }
    }

    /**
     * 获取柜子通信号
     */
    public static Long getCabinetNo() {
        return Options.getLong("ectcps.cabinetNo");
    }

    /**
     * 获取守护进程运行命令
     */
    public static String getStartDaemonCmd() {
        final StringBuilder ret = new StringBuilder("start \"\" \"");
        ret.append(Options.getString("java.home"));
        ret.append("/bin/javaw.exe\" -cp ");
        ret.append(Options.getString("dir.updateDir") + "/ecc_am.jar com.seagen.ecc.am.main.Daemon");
        return ret.toString();
    }

    public static void init(String version) {
        ConfigUtil.init(version);

        Options.loadProperties("appConfig.properties");// 读取系统配置
        if (!isDebug()) {
            TaskManage.stopProcess("javaw.exe");// 关闭其它java进程
        }

        // 根据Options的数据填充CommandHandler内的一些默认值
        BscCommandSender.fillDefaultCabinet();

        execBat();
        DBUtils.updateDB();//更新数据库

        ConfigUtil.readConfigFromDB();
        makeDirs();
        if (!isDebug()) {
            logger.info("not debug");
            setStartup();// 设置开机启动
            TaskManage.killExplorer();// 关闭windows资源管理器
            startDaemon();// 守护进程
        }

        startModuleControl();
        startCommuncation();
        Thread.setDefaultUncaughtExceptionHandler(new AppUncaughtExceptionHandler());// 对未捕获的异常进行处理
        /**记录开机启动***/
        LogAutoAnalysisService.getInstance().RecordSystemStart();

        startScheduledTask();
    }

    private static void initTimeLine() {
        final EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!PageUtil.bootShowing() || !isUsing) {
                    return;
                }
                freeTime--;
                if (freeTime <= 0) {
                    logger.info("系统空闲");
                    isUsing = false;
                }
            }
        };
        final Timeline timeLine = new Timeline();
        timeLine.setCycleCount(Animation.INDEFINITE);
        timeLine.getKeyFrames().add(new KeyFrame(Duration.seconds(1), handler));
        timeLine.play();
    }

    public static boolean isDebug() {
        return Options.getBoolean("debug");
    }

    public static boolean isTest() {
        return Options.getBoolean("test");
    }

    public static boolean isUsing() {
        return isUsing;
    }

    /**
     * 创建程序所需目录
     */
    public static void makeDirs() {
        logger.info("create necessary dirs");
        try {
            final Map<String, String> dirs = Options.getAllWithStart("dir.");
            if (dirs == null || dirs.isEmpty()) {
                logger.info("dirs is null");
                return;
            }
            for (final String key : dirs.keySet()) {
                final String dir = dirs.get(key);
                final File f = new File(dir);
                if (!f.exists()) {
                    logger.info("创建目录:" + key + "=" + dir);
                    f.mkdirs();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加开机启动项
     */
    public static void setStartup() {
        logger.info("检测程序是否开机启动...");
        final String startPath = System.getProperty("user.home")
                + "/AppData/Roaming/Microsoft/Windows/Start Menu/Programs/Startup";
        final File startDir = new File(startPath);

        if (!startDir.exists()) {
            logger.error("开机启动项目录不存在:" + startDir.getAbsolutePath());
            return;
        }
        // 获取配置中需要开机启动的程序
        final List<File> startFiles = new ArrayList<File>();
        for (int i = 0; i < Byte.MAX_VALUE; i++) {
            final String t = Options.get("startup." + i);
            if (StringUtils.isEmpty(t)) {
                break;
            }
            final File f = new File(t);
            if (f.exists()) {
                logger.info("开机启动项：" + t);
                startFiles.add(f);
                continue;
            } else {
                logger.warn("开机启动文件不存在:" + t);
            }
        }
        if (startFiles == null || startFiles.size() == 0) {
            logger.info("没有配置开机启动项目");
            return;
        }
        // 添加启动脚本 ecc.bat
        try {
            final File start = new File(startDir, "ecc.bat");
            final Charset charset = Charset.forName("GBK");
            String oldData = "";
            if (start.exists()) {
                oldData = FileUtils.readFileToString(start, charset);
            }
            final StringBuilder data = new StringBuilder();
            for (final File f : startFiles) {
                data.append("set file=" + f.getAbsolutePath() + "\r\n");
                data.append("if exist %file% (\r\n");
                if (f.getParentFile() != null) {
                    data.append("cd " + f.getParentFile().getAbsolutePath() + "\r\n");
                }
                data.append("start %file%\r\n)\r\n");
            }
            data.append("exit\r\n");

            if (oldData.equals(data.toString())) {
                logger.info("开机启动已存在");
                return;
            }
            new Thread("Thread_ApplicationUtility-setStartup") {
                @Override
                public void run() {// 修改启动项可能被阻止
                    try {
                        FileUtils.writeStringToFile(start, data.toString(), charset);
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                    logger.info("成功更新开机启动");
                };
            }.start();
        } catch (final Exception e) {
            logger.error("设置开机启动项异常", e);
        }
    }

    /**
     * 设置占用系统
     * @param seconds 占用时间（单位：秒）
     */
    public static void setUsing(int seconds) {
        if (!isUsing) {
            logger.info("系统开始使用");
        }
        freeTime = 20 + seconds;//补偿20秒
        isUsing = true;
    }

    /**
     * 启动系统通信
     */
    private static void startCommuncation() {
        logger.info("start Communcation");
        BcsHandler.getInstance().start();
        McsHandler.getInstance().start();
    }

    /**
     * 启动守护进程
     */
    private static void startDaemon() {
        logger.info("start Daemon");
        try {
            ApplicationUtil.copyJar2UpdateDir();
            ApplicationUtil.createDaemonInfo();
            WinCmd.execBat(ApplicationUtil.getStartDaemonCmd());
        } catch (final Exception e) {
            logger.error("守护进程启动失败", e);
        }
    }

    public static void startMainListener() {
        initTimeLine();

        ContextUtil.getPrimaryStage().addEventFilter(MouseEvent.MOUSE_PRESSED, systemListener());
    }

    /**
     * 启动模块控制程序
     */
    private static void startModuleControl() {
        logger.info("start ModuleControl.exe");
        new Thread("Thread_ApplicationUtility-startModuleControl") {
            @Override
            public void run() {
                final String pathName = Options.get("file.module");
                if (new File(pathName).exists() && !TaskManage.isRunning("ModuleControl.exe")) {
                    TaskManage.start(pathName);
                }
            }
        }.start();
    }

    private static void startScheduledTask() {
        TakeCodeTask.start();
        DataClear.start();
    }

    /**
     * 终止应用程序
     * 
     * @param process
     */
    public static void stopProcess(String process) {
        logger.info("stop Process:" + process);
        try {
            TaskManage.stopProcess(process);
        } catch (final Exception e) {
            logger.error("终止应用失败:" + process);
        }
    }

    private static EventHandler<MouseEvent> systemListener() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logger.debug("点击事件(" + event.getSceneX() + "," + event.getSceneY() + "),freeTime=" + freeTime);
                setUsing(0);
            }
        };
    }
}