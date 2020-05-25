/**
 * package name：IocFactory.java
 * description：
 * creator：baiml
 * create time：2020年5月19日
 * modifier： 
 * modify time:
 */
package com.bmldyq.simple.core.container.ioc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.bmldyq.simple.annotation.AutoWired;
import com.bmldyq.simple.annotation.Bean;
import com.bmldyq.simple.annotation.Component;
import com.bmldyq.simple.annotation.Property;
import com.bmldyq.simple.core.container.ContainerProxy;
import com.bmldyq.simple.core.container.aspect.AspectFactory;
import com.bmldyq.simple.core.container.aspect.AspectInfo;
import com.bmldyq.simple.scan.AbstractScanRegisterAnnon;
import com.bmldyq.simple.scan.ioc.ScanIocHandler;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;


/**
 *
 * description： 
 * creator： baiml
 * create time：2020年5月19日
 * modifier： 
 * modify time:
 */
public class IOCFactory  extends AspectFactory {
	
	/**
	 * bean容器
	 */
	protected static Map<String,FactoryBean> beanFactory = Maps.newConcurrentMap();
	
	/**
	 * Integer函数转换器
	 */
	private Function<String, Integer> functionInteger = Integer::valueOf;
	/**
	 * BigDecimal函数转化器
	 */
	private Function<String,BigDecimal> functionBigdecimal = BigDecimal::new;
	/**
	 * Double函数转化器
	 */
	private Function<String,Double> functionDouble = Double::valueOf;
	/**
	 * Float函数转化器
	 */
	private Function<String,Float> functionFloat = Float::valueOf;
	
	private List<AspectInfo> aspectInfo;
	
	
	/**
	 * 
	 * method name：init
	 * description：初始化
	 * return_type：void
	 * creator：baiml
	 * create time：2020年5月19日
	 * modifier： 
	 * modify time：
	 * @throws Exception 
	 */
	protected synchronized  void init() throws Exception {
		AbstractScanRegisterAnnon scan = new ScanIocHandler();
		List<Class<? extends Object>> list = scan.scanRegisterClass();
		if(list != null && list.size() > 0 ) {
			/**
			 * 1.加载切面信息 
			 */
			setAspectInfo();
			/**
			 * 2.对注册了component的类注册为容器Bean
			 */
			newInstance(list);
			/**
			 * 3.对新注册的Bean进行容器化
			 * 
			 */
			findBean();
			/**
			 * 4.对配置了autoWired的注解进行依赖注入
			 */
			autoWired();
			
		}
	}
	
	private  void findBean() throws Exception {
		String [] keys = beanFactory.keySet().toArray(new String[beanFactory.size()]);
		Map<String,FactoryBean> methodBean = Maps.newConcurrentMap();
		for(String key : keys) {
			FactoryBean fbean = beanFactory.get(key);
			Class<? extends Object> clzz = fbean.getBeanClzz();
			Method [] methods = clzz.getDeclaredMethods();
			for(Method method : methods) {
				boolean isAccessable = method.isAccessible();
				method.setAccessible(true);
				if(method.isAnnotationPresent(Bean.class)) {
					/**
					 * 找到一个Bean，将其注册为容器的Bean
					 */
					FactoryBean newBean = registerBean(method,fbean);
					if(newBean != null ) {
						methodBean.put(newBean.getBeanName(), newBean);
					}
				}
				method.setAccessible(isAccessable);
			}
			/**
			 * 将新找到的Bean注册到容器中
			 */
			if(!methodBean.isEmpty()) {
				beanFactory.putAll(methodBean);
			}
		}
	}
	
	
	private  FactoryBean registerBean(Method method,FactoryBean factoryBean ) throws Exception {
		FactoryBean fbean = null;
		Bean bean = method.getAnnotation(Bean.class);
		/**
		 * 这边注册的bean默认没有参数，默认空参数的方法,Bean的默认都是单例
		 */
		Object object = method.invoke(getObject(factoryBean), new Object[0]);
		if(object != null ) {
			/**
			 * 判断Bean是否有初始化的参数，有的话进行初始化
			 */
			initBeanProperty(object, bean);
			fbean = new FactoryBean();
			fbean.setBeanClzz(object.getClass());
			fbean.setBeanInstance(object);
			fbean.setBeanName(getBeanNameByMethod(method, bean));
			fbean.setComponent(factoryBean.getComponent());
		}
		return fbean;
	}
	
	private String getBeanNameByMethod(Method method,Bean bean) {
		String name = bean.name();
		if(StringUtils.isEmpty(name)) {
			name = method.getName();
		}
		return name;
	}
	/**
	 * 
	 * method name：initBeanProperty
	 * description：初始化
	 * @param object
	 * @param bean
	 * return_type：void
	 * creator：baiml
	 * create time：2020年5月19日
	 * modifier： 
	 * modify time：
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private void initBeanProperty(Object object,Bean bean) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Property[] ps = bean.propertys();
		if (ps != null && ps.length > 0) {
			Class<? extends Object> clzz = object.getClass();
			for (Property property : ps) {
				// name就是对象的属性
				String name = property.name();
				String value = property.value();
				if (StringUtils.isNotEmpty(value)) {
					// 只有配置了并且值不为空的情况下才能进行初始化赋值
					Field field = clzz.getDeclaredField(name);
					boolean isAccessable = field.isAccessible();
					field.setAccessible(true);
                     /**
                      * 设置值
                      */
					setValue(field, object, value);
					field.setAccessible(isAccessable);
				}
			}
		}
	}
	
	private void setValue(Field field,Object instance,String value) throws IllegalArgumentException, IllegalAccessException {
		Class<?> filedType = field.getType();
		if (filedType.equals(String.class) || filedType == String.class) {
			field.set(instance, value);
		}
		if (filedType.equals(BigDecimal.class) || filedType == BigDecimal.class) {
			field.set(instance, functionBigdecimal.apply(value));
		}
		if (filedType.equals(Integer.class) || filedType == Integer.class) {
			field.set(instance, functionInteger.apply(value));
		}
		if (filedType.equals(Double.class) || filedType == Double.class) {
			field.set(instance, functionDouble.apply(value));
		}
		if (filedType.equals(Float.class) || filedType == Float.class) {
			field.set(instance, functionFloat.apply(value));
		}
	}
	
	
	
	
	private  void newInstance(List<Class<? extends Object>> list) throws Exception{
		for(Class<? extends Object> clzz : list) {
			//取得所有的继承接口
			Class<?>[] interfaces = clzz.getInterfaces();
			//获取BEAN的名字
			String beanName = getBeanName(clzz,interfaces);
			Object obj = getInstance(clzz);
			FactoryBean bean = new FactoryBean();
			bean.setBeanName(beanName);
			bean.setBeanClzz(clzz);
			bean.setBeanInstance(obj);
			bean.setComponent(clzz.getAnnotation(Component.class));
			beanFactory.put(beanName, bean);
		}
	}
	/**
	 * 
	 * method name：getBeanName
	 * description：获取BEAN名称
	 * @param objClzz
	 * @param interfaces
	 * @return
	 * return_type：String
	 * creator：baiml
	 * create time：2020年5月19日
	 * modifier： 
	 * modify time：
	 */
	private  String getBeanName(Class<?> objClzz,Class<?>[] interfaces) {
		String beanName = null;
		Component component = objClzz.getAnnotation(Component.class);
		beanName = component.name();
		if(StringUtils.isEmpty(beanName)) {
			if(interfaces != null && interfaces.length > 0 ) {
				Class<?> clzz = interfaces[0];
				String clzzSimpleName = clzz.getSimpleName();
				beanName = clzzSimpleName.substring(0,1).toLowerCase() + clzzSimpleName.substring(1);
			}else {
				String clzzSimpleName = objClzz.getSimpleName();
				beanName = clzzSimpleName.substring(0,1).toLowerCase() + clzzSimpleName.substring(1);
			}
		}
		return beanName;
	}
	
	private  Object getInstance(Class<? extends Object> clzz) throws Exception {
		Object object = null;
		Component component = clzz.getAnnotation(Component.class);
		Component.BEAN_TYPE beanType = component.beanType();
		if(beanType.equals(Component.BEAN_TYPE.singleton)) {
			/**
			 * 判断是否有切面
			 */
			object = getInstanceByAspect(clzz);
		}
		return object;
		
	}
	
	protected Object getInstanceByAspect(Class<? extends Object> clzz) throws Exception {
		Object object = null;
		if (this.aspectInfo != null && aspectInfo.size() > 0) {
			String classname = clzz.getName();
			List<AspectInfo> infolist = aspectInfo.stream().filter(item -> {
				Pattern p = Pattern.compile(item.getPointcut().execution());
				Matcher m = p.matcher(classname);
				return m.matches();
			}).collect(Collectors.toList());
			if (infolist != null && infolist.size() > 0) {
				AspectInfo info = infolist.get(0);
				object = getProxyService(clzz,info);
			}else{
				object = clzz.newInstance();
			}
		}else {
			object = clzz.newInstance();
		}
		return object;
	}
	
	public  Object getProxyService(Class<? extends Object> clzz,AspectInfo info) throws Exception {
		ClassLoader loader = getClass().getClassLoader();
		ContainerProxy proxy = new ContainerProxy();
		proxy.setAfterMethod(info.getAfterMethod());
		proxy.setAfterTrxMethod(info.getAfterTrxMethod());
		proxy.setBeforeMethod(info.getBeforeMethod());
		proxy.setAspectObject(getAspectObject(info));
		Object trueObject = clzz.newInstance();
		proxy.setObject(trueObject);//真正代理的对象
		info.setTrueProxy(trueObject);
		return  Proxy.newProxyInstance(loader, clzz.getInterfaces(), proxy);
	}
	
	public  Object getAspectObject(AspectInfo info) throws Exception {
		Object result = null;
		Class<? extends Object> clzz = info.getAspectClzz();
		String clname = clzz.getSimpleName();
		String beanName = clname.substring(0,1).toLowerCase() + clname.substring(1);
		if(clzz.isAnnotationPresent(Component.class)) {
			Component com = clzz.getAnnotation(Component.class);
			if(StringUtils.isNotEmpty(com.name())) {
				beanName = com.name();
			}
		}
		FactoryBean fbean = beanFactory.get(beanName);
		if(fbean != null ) {
			result = getObject(fbean);
		}else {
			result = clzz.newInstance();
		}
		return result;
	}
	
	private  void autoWired() throws Exception{
		String [] keys = beanFactory.keySet().toArray(new String[beanFactory.size()]);
		for(String key : keys) {
			FactoryBean fbean = beanFactory.get(key);
			autoSet(fbean); 
		}
	}
	
	

	/**
	 * 
	 * method name：autoSet
	 * description：单例的Bean的初始化
	 * @param fbean
	 * @throws Exception
	 * return_type：void
	 * creator：baiml
	 * create time：2020年5月19日
	 * modifier： 
	 * modify time：
	 */
	private  void autoSet(FactoryBean fbean) throws Exception {
		Object beanInstance = fbean.getBeanInstance();
		if (beanInstance != null) {
			denpence(beanInstance);
		}
	}
	/**
	 * 
	 * method name：autoSetByPropto
	 * description：对于非单例的容器Bean，我们没有办法初始化的时候设置定义的属性类对象
	 * 需要在每次请求生成一个新的对象的时候重新进行依赖注入，不能在初始化的时候进行依赖注入
	 * @param beanInstance
	 * @throws Exception
	 * return_type：void
	 * creator：baiml
	 * create time：2020年5月19日
	 * modifier： 
	 * modify time：
	 */
	protected  void autoSetByPropto(Object beanInstance) throws Exception {
		if (beanInstance != null) {
			denpence(beanInstance);
		}

	}
	/**
	 * 
	 * method name：denpence
	 * description：Bean依赖注入
	 * @param beanInstance
	 * return_type：void
	 * creator：baiml
	 * create time：2020年5月19日
	 * modifier： 
	 * modify time：
	 * @throws Exception 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private void denpence(Object beanInstance) throws IllegalArgumentException, IllegalAccessException, Exception {
		String clname = beanInstance.getClass().getName();
		/**
		 * 如果beanInstance对象是包含切面的对象，那么就是Proxy代理的对象，而代理的对象一般都是接口
		 * 我们要依赖注入我们的对象，那么就需要得到真正代理的对象
		 */
		if(clname.startsWith("com.sun.proxy")) {
			ContainerProxy p = (ContainerProxy) Proxy.getInvocationHandler(beanInstance);
			beanInstance = p.getObject();
		}
		Field[] fields = beanInstance.getClass().getDeclaredFields();
		for (Field field : fields) {
			boolean isAccessable = field.isAccessible();
			field.setAccessible(true);
			if (field.isAnnotationPresent(AutoWired.class)) {
				// 实现了依赖注入的注解
				// 需要根据beanName查找对应的对象
				field.set(beanInstance, searchAutoWiredBean(field));
			}
			field.setAccessible(isAccessable);
		}
	
	}
	
	private  Object searchAutoWiredBean(Field field ) throws Exception {
		AutoWired auto = field.getAnnotation(AutoWired.class);
		String val = auto.value();
		String beanName = val;
		if(StringUtils.isEmpty(val)) {
			String fieldName = field.getName();
			beanName = fieldName.substring(0,1).toLowerCase() + fieldName.substring(1);
		}
		return getObject(beanFactory.get(beanName));
	}
	/**
	 * 
	 * method name：getObject
	 * description：通过容器获取类的对象
	 * @param fbean
	 * @return
	 * @throws Exception
	 * return_type：Object
	 * creator：baiml
	 * create time：2020年5月19日
	 * modifier： 
	 * modify time：
	 */
	private Object getObject(FactoryBean fbean) throws Exception {
		Object result = null;
		Object instance = fbean.getBeanInstance();
		if (instance != null) {
			result = instance;
		} else {
			Component component = fbean.getComponent();
			switch (component.beanType()) {
			case prototype:
				/**
				 * 如果是prototype，那么为每一个类定义了的服务重新生成一个对象
				 */
				Class<? extends Object> clzz = fbean.getBeanClzz();
				result = clzz.newInstance();
				break;
			case singleton:
				/**
				 * 单例，整个jvm中值存在一个对象，每一个类的field定义，容器中只能存在一个对象
				 */
				result = fbean.getBeanInstance();
				break;
			}
		}
		return result;
	}


	public void setAspectInfo() throws Exception {
		aspectInfo =  getAspectInfo();
	}
}