package com.easyget.terminal.base.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.seagen.ecc.utils.DateUtils;

/**
 * Http工具
 */
public class HttpClientUtils {

    private static final Logger logger = Logger.getLogger(HttpClientUtils.class);

    public static boolean download(String downloadUrl, File file) {
        boolean result = false;
        FileOutputStream fos = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        try {
            final URL url = new URL(downloadUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);

            connection.connect();
            // File file = new File(savePath);
            final File parent = file.getParentFile();
            if (parent == null) {
                return result;
            }
            if (!parent.exists()) {
                parent.mkdirs();
            }

            if (file.exists()) {
                file.delete();
            }
            final int contentlenth = connection.getContentLength();
            logger.info("即将下载文件长度：" + contentlenth);
            is = connection.getInputStream();
            fos = new FileOutputStream(file);
            int readlen = 0;
            final byte[] buffer = new byte[512];
            boolean faileFlag = false;
            while ((readlen = is.read(buffer)) != -1) {
                fos.write(buffer, 0, readlen);
                if (0 == readlen) {
                    faileFlag = true;
                    break;
                }
            }
            if (faileFlag) {
                logger.info("下载时因为断网出错：HttpURLConnection download  error");
            } else {
                logger.info("文件包下载完毕！！");
                result = true;
            }

        } catch (final IOException e) {
            e.printStackTrace();
            logger.error("下载出错：HttpURLConnection download  error", e);
        } finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(is);
        }
        logger.info("下载结束返回！！");
        return result;
    }

    /**
     * Http请求
     * 
     * @param url
     */
    public static boolean downloadByGet(String url, OutputStream out) {
        final HttpGet get = new HttpGet(url);
        HttpResponse response;
        try {

            response = HttpClients.createDefault().execute(get);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {// 返回成功
                final InputStream is = response.getEntity().getContent();
                final BufferedInputStream bis = new BufferedInputStream(is);
                final BufferedOutputStream bos = new BufferedOutputStream(out);
                final byte[] buff = new byte[2048];
                int bytesRead;
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
                bos.close();
                bis.close();
                is.close();

                return true;

            } else {
                logger.warn("文件下载失败，请确定URL是否正确！");
            }
        } catch (final IOException e) {
            logger.error("Http Get download  error", e);
        } finally {
            try {
                out.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 以POST方式提交
     * 
     * @param url
     *            服务地址
     * @param params
     *            参数
     */
    public static boolean post(String url, Map<String, Object> params) throws IOException {
        final HttpPost post = new HttpPost(url);

        // 对请求的表单域进行填充
        final MultipartEntityBuilder meb = MultipartEntityBuilder.create();
        meb.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        meb.setCharset(Charset.forName("utf-8"));
        post.addHeader("enctype=", "multipart/form-data");
        for (final String key : params.keySet()) {
            final Object o = params.get(key);
            if (o == null) {
                meb.addTextBody(key, "");
            } else if (o instanceof Date) {
                // 时间格式处理
                final Date d = (Date) o;
                final Calendar c = Calendar.getInstance();
                c.setTime(d);
                if (c.get(Calendar.HOUR_OF_DAY) == 0 && c.get(Calendar.MINUTE) == 0 && c.get(Calendar.SECOND) == 0) {
                    // 日期
                    meb.addTextBody(key, DateUtils.datetimeToString("yyyy-MM-dd", d));
                } else {
                    // 带时间
                    meb.addTextBody(key, DateUtils.datetimeToString("yyyy-MM-dd HH:mm:ss", d));
                }
            } else if (o instanceof File) {// 文件处理
                // meb.addBinaryBody(key, (File) o);
                meb.addBinaryBody(key, (File) o, ContentType.DEFAULT_BINARY, ((File) o).getName());
            } else if (o instanceof InputStream) {// 二进制流处理
                meb.addBinaryBody(key, (InputStream) o, ContentType.DEFAULT_BINARY, key);
            } else {
                meb.addTextBody(key, o.toString());
            }
        }

        final HttpEntity entity = meb.build();
        post.setEntity(entity);
        try {
            // 执行
            final HttpResponse response = HttpClients.createDefault().execute(post);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {// 返回成功
                final String s = EntityUtils.toString(response.getEntity());
                logger.debug(s);
                return true;
            } else {
                final String s = EntityUtils.toString(response.getEntity());
                logger.error(s);
            }
        } catch (final IOException e) {
            logger.error("Http Post execute error", e);
        }
        return false;
    }
}