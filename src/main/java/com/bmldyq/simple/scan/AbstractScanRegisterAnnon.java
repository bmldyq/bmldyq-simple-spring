package com.bmldyq.simple.scan;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 
 * 文件名：AbstractScanRegisterAnnon.java
 * 说 明：扫描系统注册的注解处理器
 * 创 建 人： baiml
 * 创 建 日 期：2019年12月3日
 * 修 改 人：
 * 修 改 日 期：
	 */
	public abstract class AbstractScanRegisterAnnon {
		private Logger logger = LoggerFactory.getLogger(AbstractScanRegisterAnnon.class);

		/**
	 * 
	 * 方 法 名：getClassLoader
	 * 
	 * @return 返 回 类 型：ClassLoader 说 明：获取类加载器 创 建 人：baiml 创 建 日 期：2019年12月3日 修 改 人：
	 *         修 改 日 期：
	 */
	protected abstract ClassLoader getClassLoader();

	/**
	 * 
	 * 方 法 名：getScanPackage
	 * 
	 * @return 返 回 类 型：String 说 明：获取扫描的注解包策略 创 建 人：baiml 创 建 日 期：2019年12月3日 修 改 人： 修
	 *         改 日 期：
	 */
	protected abstract String getScanPackage();

	/**
	 * 
	 * 方 法 名：isAddClass
	 * 
	 * @param clzz
	 * @return 返 回 类 型：boolean 说 明：是否可以添加扫描到的类 创 建 人：baiml 创 建 日 期：2019年12月3日 修 改 人：
	 *         修 改 日 期：
	 */
	protected abstract boolean isAddClass(Class<? extends Object> clzz);

	/**
	 * 
	 * 方 法 名：isAgreeCheck
	 * 
	 * @param packName
	 * @return 返 回 类 型：boolean 说 明：是否通过校验 创 建 人：baiml 创 建 日 期：2019年12月3日 修 改 人： 修 改
	 *         日 期：
	 */
	protected abstract boolean isAgreeCheck(String packName);
	/**
	 * 
	 * 方 法 名：checkPatten
	 * 系 统 名 称：       银 联 业 务 系 统
	 * @param packName
	 * @return              
	 * 返 回 类 型：       boolean
	 * 说 明：               检查匹配
	 * 创 建 人：          baiml
	 * 创 建 日 期：       2019年12月23日
	 * 修 改 人： 
	 * 修 改 日 期：
	 */
	protected abstract boolean checkPatten(String packName);

	/**
	 * 
	 * 方 法 名：scanRegisterClass
	 * 
	 * @return
	 * @throws Exception 返 回 类 型：List<Class<? extends Object>> 说 明：扫描 创 建 人：baiml 创
	 *                   建 日 期：2019年12月3日 修 改 人： 修 改 日 期：
	 */
	public List<Class<? extends Object>> scanRegisterClass() {
		List<Class<? extends Object>> result = Lists.newArrayList();
		String packageName = getScanPackage();
		if (logger.isDebugEnabled()) {
			logger.debug("AbstractScanRegisterAnnon.scanRegisterClass 扫包开始 packageName={}...", packageName);
		}
		try {
			scanPackage(packageName, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("AbstractScanRegisterAnnon.scanRegisterClass 扫包结束packageName={}...", packageName);
		}
		return result;
	}

	private void scanPackage(String classpack, List<Class<? extends Object>> list) throws Exception {
		URL url = getClassLoader().getResource(classpack.replace(".", "/"));
		String protocol = url.getProtocol();
		if (protocol.equals("file")) {
			// 本地class
			scanLocalClass(classpack, url.toURI(), list);
		} else if (protocol.equals("jar")) {
			// jar扫描
			scanLocalJar(classpack, list);
		}
	}

	private void scanLocalJar(String classpack, List<Class<? extends Object>> list) {
		String pathName = classpack.replace(".", "/");
		try {
			Enumeration<URL> dir = Thread.currentThread().getContextClassLoader().getResources(pathName);
			List<JarFile> arrayList = Lists.newCopyOnWriteArrayList();
			while (dir.hasMoreElements()) {
				URL u = dir.nextElement();
				String path = u.getPath();
				if(path.indexOf(".jar") != -1 ) {
					JarURLConnection c = (JarURLConnection) u.openConnection();
					JarFile f = c.getJarFile();
					arrayList.add(f);
				}
			}
			// 去重,不知道为什么系统启动过后自动加载，distinct没有效果，而从调用来执行，是有效果的
			arrayList = arrayList.stream().distinct().collect(Collectors.toList());
			Map<String, String> co = Maps.newConcurrentMap();
			arrayList.forEach(item -> {
				if (!co.containsKey(item.getName())) {
					scanJarClass(item, pathName, list);
					co.put(item.getName(), "1");
				}

			});
		} catch (IOException e) {
			e.printStackTrace();
			if (logger.isErrorEnabled()) {
				logger.error("未找到jar的资源");
			}
		}
	}

	private void scanJarClass(JarFile jarFile, String pathName, List<Class<? extends Object>> list) {
		Enumeration<JarEntry> jarEnties = jarFile.entries();
		while (jarEnties.hasMoreElements()) {
			JarEntry jarEntry = jarEnties.nextElement();
			String jarEntryName = jarEntry.getName();
			if (jarEntryName.contains(pathName) && !jarEntryName.equals(pathName + "/")) {
				if (jarEntry.isDirectory()) {
					continue;
				}
				if (isAgreeCheck(jarEntry.getName())) {
					try {
						Class<? extends Object> clzz = getClassLoader().loadClass(jarEntry.getName().replace("/", ".").replace(".class", ""));
						if (isAddClass(clzz)) {
							list.add(clzz);
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void scanLocalClass(String classpack, URI url, List<Class<? extends Object>> list) {
		File file = new File(url);
		file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				String filename = f.getName();
				if (f.isDirectory()) {
					try {
						scanPackage(classpack + "." + filename, list);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					if (isAgreeCheck(f.getAbsolutePath())) {
						try {
							Class<? extends Object> clzz = getClassLoader().loadClass(classpack + "." + filename.replace(".class", ""));
							if (isAddClass(clzz)) {
								list.add(clzz);
							}
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}

					}
				}
				return false;
			}
		});
	}

}
