package com.easyget.terminal.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.seagen.ecc.utils.StringUtils;

public class Options {
	private static Logger logger = Logger.getLogger(Options.class);
	private static Map<Object, Object> map = new ConcurrentHashMap<Object, Object>();
	private static final Pattern EL_PATTERN = Pattern
			.compile("\\$\\{([^\\}]+)\\}");

	static {
		try {
			Options.set("java.home", System.getProperties().get("java.home"));
			@SuppressWarnings("deprecation")
			File file = new File(URLDecoder.decode(Options.class
					.getProtectionDomain().getCodeSource().getLocation()
					.getFile()));
			if (!file.isDirectory()) {
				file = file.getParentFile();
			}
			Options.set("executableFilePath", file.getAbsolutePath());
		} catch (Exception e) {
			logger.warn("Options init faild");
		}
	}

	public static void set(Object key, Object value) {
		if (StringUtils.isNotEmpty(key, value)) {
			map.put(key, value);
			logger.info("set:" + key + "=" + value);
		}
	}

	public static void loadProperties(File file) {
		try {
			Properties properties = new Properties();
			InputStream is = new FileInputStream(file);
			properties.load(is);
			map.putAll(properties);
			is.close();
		} catch (Exception e) {
			logger.error("loadProperties failed", e);
		}
	}

	public static void loadPropertiesFromAbsolutePath(String absolutePath) {
		try {
			InputStream is = new FileInputStream(absolutePath);
			Properties properties = new Properties();
			properties.load(is);
			map.putAll(properties);
			is.close();
		} catch (Exception e) {
			logger.error("loadProperties failed", e);
		}
	}

	public static void loadProperties(String file) {
		try {
			Properties properties = new Properties();
			InputStream is = Options.class.getClassLoader()
					.getResourceAsStream(file);
			properties.load(is);
			map.putAll(properties);
			is.close();
		} catch (Exception e) {
			logger.error("loadProperties failed", e);
		}
	}

	public static String get(String key) {
		String ret = "";
		Object value = map.get(key);
		if (value != null) {
			ret = value.toString();
		}
		Matcher m = EL_PATTERN.matcher(ret);
		String g;
		while (m.find()) {
			g = m.group(1);
			ret = m.replaceFirst(get(g));
			m = EL_PATTERN.matcher(ret);
		}
		return ret.trim();
	}

	public static String getString(String key) {
		return get(key);
	}

	public static String getString(String key, String defaultValue) {
		String ret = get(key);
		if (ret == null || "".equals(ret)) {
			ret = defaultValue;
			logger.debug("using defaultValue," + key + "=" + defaultValue);
			if (StringUtils.isNotEmpty(key, defaultValue)) {
				set(key, defaultValue);
			}
		}
		return ret;
	}

	public static String getStringExactly(String key, String defaultValue) {
		String ret = get(key);
		if (ret == null || "".equals(ret)) {
			ret = defaultValue;
			logger.debug("using defaultValue," + key + "=" + defaultValue);
		}
		return ret;
	}

	public static Integer getInt(String key) {
		try {
			return Integer.parseInt(get(key));
		} catch (NumberFormatException e) {
			logger.debug("getInt return null,key=" + key);
		}
		return null;
	}

	public static Integer getInt(String key, int defaultValue) {
		Integer ret = getInt(key);
		if (ret == null) {
			ret = defaultValue;
			logger.debug("using defaultValue," + key + "=" + defaultValue);
			if (ret != null) {
				set(key, ret.toString());
			}
		}
		return ret;
	}

	public static Long getLong(String key) {
		try {
			return Long.parseLong(get(key));
		} catch (NumberFormatException e) {
			logger.debug("getLong return null,key=" + key);
		}
		return null;
	}

	public static Long getLong(String key, long defaultValue) {
		Long ret = getLong(key);
		if (ret == null) {
			ret = defaultValue;
			logger.debug("using defaultValue," + key + "=" + defaultValue);
			if (ret != null) {
				set(key, ret.toString());
			}
		}
		return ret;
	}

	public static boolean getBoolean(String key) {
		if ("true".equals(get(key))) {
			return true;
		}
		return false;
	}

	public static Map<String, String> getAllWithStart(String start) {
		Map<String, String> ret = new HashMap<String, String>();
		for (Map.Entry<Object, Object> e : map.entrySet()) {
			if (e.getKey() instanceof String) {
				String key = (String) e.getKey();
				if (key.startsWith(start)) {
					ret.put(key, get(key));
				}
			}
		}
		return ret;
	}

	public static Map<String, String> getAll() {
		Map<String, String> ret = new HashMap<String, String>();
		for (Map.Entry<Object, Object> e : map.entrySet()) {
			String key = (String) e.getKey();
			ret.put(key, get(key));
		}
		return ret;
	}

	public static void main(String[] args) {
	}

	public static void putAll(Map<String, String> kvs) {
		for (Map.Entry<String, String> e : kvs.entrySet()) {
			String k = e.getKey();
			String v = e.getValue();
			if (StringUtils.isNotEmpty(k, v)) {
				map.put(k, v);
			}
		}
	}
}
