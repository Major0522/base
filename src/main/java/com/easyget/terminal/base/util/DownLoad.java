package com.easyget.terminal.base.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

//断点续传
public class DownLoad {
    private static Logger logger = Logger.getLogger(DownLoad.class);

    private static long startPos = 0, endPos = 0;

    private static long localFileSize = 0;

    public static void down(String URL, long nPos, String savePathAndFile) {
        try {

            final URL url = new URL(URL);
            final HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            // 设置User-Agent
            httpConnection.setRequestProperty("User-Agent", "NetFox");
            // 设置断点续传的开始位置
            httpConnection.setRequestProperty("RANGE", "bytes=" + nPos);
            // 获得输入流
            startPos = nPos;
            logger.info("开始下载的位置是" + startPos);
            logger.info("结束下载的位置是" + endPos);
            final InputStream input = httpConnection.getInputStream();
            input.skip(nPos);//跳过以前的字节
            final RandomAccessFile oSavedFile = new RandomAccessFile(savePathAndFile, "rw");
            // 定位文件指针到nPos位置
            oSavedFile.seek(nPos);
            final byte[] b = new byte[1024];
            int nRead, count = 0;
            // 从输入流中读入字节流，然后写到文件中

            while (((nRead = input.read(b, 0, 1024)) > 0)) {

                (oSavedFile).write(b, 0, nRead);
                startPos += nRead;
                count++;
                if (count > 100) {
                    logger.info("断开下载");//用于测试使用
                    //   break;
                }
            }
            oSavedFile.close();//关闭流文件

            logger.info("文件下载结束");
            httpConnection.disconnect();
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadfile(String url, String savePath, String fileName) {
        //        try {
        //            final URL ur1123 = new URL(url);
        //            ur1123.openConnection();
        //        } catch (final Exception e) {
        //            e.printStackTrace();
        //        }
        final File file = new File(savePath + fileName);
        // 获得远程文件大小
        final long remoteFileSize = getRemoteFileSize(url);
        endPos = remoteFileSize;

        logger.info("远程文件大小=" + remoteFileSize);
        if (file.exists()) {
            // 先看看是否是完整的，完整，换名字，跳出循环，不完整，继续下载
            localFileSize = file.length();

            logger.info("已有文件大小为:" + localFileSize);

            if (localFileSize < remoteFileSize) {
                logger.info("文件续传");
                down(url, localFileSize, savePath + fileName);
            } else {
                logger.info("文件已经存在");
            }
            // 下面表示文件存在，改名字
        } else {
            try {
                file.createNewFile();
                logger.info("下载中");
                down(url, 0, savePath + fileName);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static long getRemoteFileSize(String url) {
        long size = 0;
        try {
            final HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
            size = conn.getContentLength();
            conn.disconnect();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return size;
    }

}