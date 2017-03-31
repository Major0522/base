/*
 * 深圳壹号柜科技股份有限公司源代码，版权归深圳壹号柜科技股份有限公司所有。
 * 
 * 项目名称 : 终端项目
 * 创建日期 : 2016年3月25日
 * 修改历史 : 
 *     1. [2016年3月25日]创建文件 by niujunhong
 */
package com.easyget.terminal.base.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * <p>
 * 文件工具类
 * </p>
 * 
 * @author niujunhong
 */
public class FileUtil {
	/** log4j 日志 */
	private static final Logger logger = Logger.getLogger(FileUtil.class.getName());

	public static void finalClose(Object obj) {
		if (obj == null) {
			return;
		}
		try {
			if (obj instanceof InputStream) {
				((InputStream) obj).close();
			} else if (obj instanceof OutputStream) {
				((OutputStream) obj).close();
			} else if (obj instanceof RandomAccessFile) {
				((RandomAccessFile) obj).close();
			} else if (obj instanceof FileChannel) {
				((FileChannel) obj).close();
			} else if (obj instanceof Writer) {
				((Writer) obj).close();
			} else if (obj instanceof Closeable) {
				((Closeable) obj).close();
			} else {
				logger.error("finalClose ignro!");
			}
		} catch (final Exception ex) {
			ex.printStackTrace();
			logger.error("finalClose", ex);
		}
	}

	public static byte[] getFileToBytes(String filePath) {
		InputStream ins = null;
		try {
			ins = new FileInputStream(filePath);
			final byte[] bytes = new byte[ins.available()];
			ins.read(bytes);
			final byte[] arrayOfByte1 = bytes;
			return arrayOfByte1;
		} catch (final IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return null;
		} finally {
			finalClose(ins);
		}
	}

	public static List<String> getFileToList(String filePath) {
		final List<String> list = new ArrayList<String>();
		InputStream ins = null;
		InputStreamReader inr = null;
		BufferedReader br = null;
		try {
			ins = new FileInputStream(filePath);
			inr = new InputStreamReader(ins, "UTF-8");
			br = new BufferedReader(inr);
			String line = null;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
		} catch (final IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			finalClose(br);
			finalClose(inr);
			finalClose(ins);
		}
		return list;
	}

	public static String getFileToString(String filePath) {
		return getFileToStringBuffer(filePath).toString();
	}

	public static StringBuffer getFileToStringBuffer(String filePath) {
		final StringBuffer sb = new StringBuffer();
		InputStream ins = null;
		InputStreamReader inr = null;
		BufferedReader br = null;
		try {
			ins = new FileInputStream(filePath);
			inr = new InputStreamReader(ins, "UTF-8");
			br = new BufferedReader(inr);
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\r\n");
			}
		} catch (final IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			finalClose(br);
			finalClose(inr);
			finalClose(ins);
		}
		return sb;
	}

	public static Properties getPropertiesFromClassPathFile(String classPathFile) {
		final Properties propertis = new Properties();
		final InputStream ins = FileUtil.class.getResourceAsStream("/" + classPathFile);
		try {
			propertis.load(ins);
		} catch (final IOException e) {
			e.printStackTrace();
			logger.error("getPropertiesFromClassPathFile", e);
		} finally {
			finalClose(ins);
		}
		return propertis;
	}

	public static Properties getPropertiesFromFilePath(String filePath) {
		final Properties propertis = new Properties();
		InputStream ins = null;
		try {
			ins = new FileInputStream(filePath);
			propertis.load(ins);
		} catch (final IOException e) {
			e.printStackTrace();
			logger.error("getPropertiesFromFilePath", e);
		} finally {
			finalClose(ins);
		}
		return propertis;
	}

	public static String getFromClassPath(String classPathFile) throws IOException {
		InputStream resourceAsStream = null;
		try {
			resourceAsStream = FileUtil.class.getResourceAsStream(classPathFile);
			final byte[] bytes = new byte[resourceAsStream.available()];
			resourceAsStream.read(bytes);
			return new String(bytes, "UTF-8");
		} finally {
			finalClose(resourceAsStream);
		}
	}

	public static void writeAppendToFile(byte[] bytes, File file) {
		RandomAccessFile rFile = null;
		try {
			if (!file.exists()) {
				final boolean isCreated = file.createNewFile();
				if (!isCreated) {
					logger.error("writeToFile error!");
				}
			}
			rFile = new RandomAccessFile(file, "rw");
			rFile.seek(rFile.length());
			rFile.write(bytes);
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			finalClose(rFile);
		}
	}

	public static void writeToFile(byte[] bytes, File file) {
		FileOutputStream fos = null;
		try {
			if (!file.exists()) {
				final boolean isCreated = file.createNewFile();
				if (!isCreated) {
					logger.error("writeToFile error!");
				}
			}
			fos = new FileOutputStream(file);
			fos.write(bytes);
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			finalClose(fos);
		}
	}

	/**
	 * 删除过期文件。
	 * 
	 * @param file 要处理的文件或目录。
	 * @param day 过期时间。
	 */
    public static void deleteOverdueFiles(File file, final long day) {
        if (file.isDirectory()) {
            for (final File f : file.listFiles()) {
                if (f.isDirectory()) {
                	deleteOverdueFiles(f, day);
                } else {
                    if (f.lastModified() < day) {
                        logger.debug("删除文件-->" + file.getAbsolutePath());
                        f.delete();
                    }
                }
            }
        }
    }

	/**
	 * 判断文件是否存在，根据文件名及文件长度。
	 * @param fileName 文件名。
	 * @param minFileSize 最小的文件大小，如果是0，则不管文件的大小，如果大于0，则文件小于minFileSize则认为文件不存在。
	 * @return 如果文件存在，并且文件大小大于等于minFileSize，则返回true，否则返回false。
	 */
	public static boolean fileExists(String fileName, int minFileSize) {
		File destFile = new File(fileName);
		
		if (destFile.exists() && destFile.length() >= minFileSize) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 复制单个文件。
	 * 
	 * @param srcFileName
	 *            待复制的文件名，全路径
	 * @param descFileName
	 *            目标文件名 ，全路径
	 * @param overlay
	 *            如果目标文件存在，是否覆盖，true覆盖，false不覆盖(在文件后追加)
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyFile(String srcFileName, String destFileName, boolean overlay) {
		File srcFile = new File(srcFileName);

		// 判断源文件是否存在
		if (!srcFile.exists()) {
			logger.error("源文件：" + srcFileName + "不存在！");
			return false;
		} else if (!srcFile.isFile()) {
			logger.error("复制文件失败，源文件：" + srcFileName + "不是一个文件！");
			return false;
		}

		File destFile = loadDescFile(destFileName, overlay);
		if (destFile == null) {
			return false;
		}

		// 复制文件
		InputStream in = null;
		OutputStream out = null;

		try {
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);

			copyStreamData(out, in);

			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			finalClose(out);
			finalClose(in);
		}
	}

	/**
	 * 通过Classloader复制单个文件，使用ClassLoader.getSystemResourceAsStream处理源文件。
	 * 
	 * @param srcFileName
	 *            待复制的文件名 ，相对路径
	 * @param descFileName
	 *            目标文件名 ，全路径
	 * @param overlay
	 *            如果目标文件存在，是否覆盖, true覆盖，false不覆盖(在文件后追加)
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyFileByClassloader(String srcFileName, String destFileName, boolean overlay) {
		OutputStream out = null;
		InputStream in = null;

		try {
			in = ClassLoader.getSystemResourceAsStream(srcFileName);

			// 判断源文件是否存在
			if (in == null) {
				logger.error("源文件：" + srcFileName + "不存在！");
				return false;
			}

			File destFile = loadDescFile(destFileName, overlay);
			if (destFile == null) {
				return false;
			}

			out = new FileOutputStream(destFile);

			// 复制文件
			copyStreamData(out, in);

			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			finalClose(out);
			finalClose(in);
		}
	}

	private static void copyStreamData(OutputStream out, InputStream in) throws IOException {
		int byteread = 0; // 读取的字节数
		byte[] buffer = new byte[1024];

		while ((byteread = in.read(buffer)) != -1) {
			out.write(buffer, 0, byteread);
		}
	}
	
	private static File loadDescFile(String destFileName, boolean overlay) {
		// 判断目标文件是否存在
		File destFile = new File(destFileName);
		if (destFile.exists()) {
			// 如果目标文件存在并允许覆盖
			if (overlay) {
				// 删除已经存在的目标文件，无论目标文件是目录还是单个文件
				new File(destFileName).delete();
			}
		} else {
			// 如果目标文件所在目录不存在，则创建目录
			if (!destFile.getParentFile().exists()) {
				// 目标文件所在目录不存在
				if (!destFile.getParentFile().mkdirs()) {
					// 复制文件失败：创建目标文件所在目录失败
					return null;
				}
			}
		}

		return destFile;
	}

}
