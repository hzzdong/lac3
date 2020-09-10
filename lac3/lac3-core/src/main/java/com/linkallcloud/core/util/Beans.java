package com.linkallcloud.core.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;

/**
 * 基于CGlib 实体工具类，目前copy不支持map、list
 */
public final class Beans extends org.springframework.beans.BeanUtils {
	private Beans() {
	}

	/**
	 * 实例化对象
	 * 
	 * @param clazz 类
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<?> clazz) {
		try {
			return (T) clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException e1) {
			throw new RuntimeException(e1);
		} catch (IllegalAccessException e1) {
			throw new RuntimeException(e1);
		} catch (IllegalArgumentException e1) {
			throw new RuntimeException(e1);
		} catch (InvocationTargetException e1) {
			throw new RuntimeException(e1);
		} catch (NoSuchMethodException e1) {
			throw new RuntimeException(e1);
		} catch (SecurityException e1) {
			throw new RuntimeException(e1);
		}
	}

	/**
	 * 实例化对象
	 * 
	 * @param clazzStr 类名
	 * @return 对象
	 */
	public static <T> T newInstance(String clazzStr) {
		try {
			Class<?> clazz = Class.forName(clazzStr);
			return newInstance(clazz);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * copy 对象属性到另一个对象，默认不使用Convert
	 * 
	 * @param src
	 * @param clazz 类名
	 * @return T
	 */
	public static <T> T copy(Object src, Class<T> clazz) {
		BeanCopier copier = BeanCopier.create(src.getClass(), clazz, false);

		T to = newInstance(clazz);
		copier.copy(src, to, null);
		return to;
	}

	/**
	 * 拷贝对象
	 * 
	 * @param src  源对象
	 * @param dist 需要赋值的对象
	 */
	public static void copy(Object src, Object dist) {
		BeanCopier copier = BeanCopier.create(src.getClass(), dist.getClass(), false);

		copier.copy(src, dist, null);
	}

	/**
	 * 将对象装换为map
	 * 
	 * @param bean
	 * @return
	 */
	public static <T> Map<String, Object> beanToMap(T bean) {
		Map<String, Object> map = Maps.newHashMap();
		if (bean != null) {
			BeanMap beanMap = BeanMap.create(bean);
			for (Object key : beanMap.keySet()) {
				map.put(key + "", beanMap.get(key));
			}
		}
		return map;
	}

	/**
	 * 将map装换为javabean对象
	 * 
	 * @param map
	 * @param bean
	 * @return
	 */
	public static <T> T mapToBean(Map<String, Object> map, T bean) {
		BeanMap beanMap = BeanMap.create(bean);
		beanMap.putAll(map);
		return bean;
	}

	/**
	 * 将List<T>转换为List<Map<String, Object>>
	 * 
	 * @param objList
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> List<Map<String, Object>> objectsToMaps(List<T> objList) {
		List<Map<String, Object>> list = Lists.newArrayList();
		if (objList != null && objList.size() > 0) {
			Map<String, Object> map = null;
			T bean = null;
			for (int i = 0, size = objList.size(); i < size; i++) {
				bean = objList.get(i);
				map = beanToMap(bean);
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 将List<Map<String,Object>>转换为List<T>
	 * 
	 * @param maps
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> List<T> mapsToObjects(List<Map<String, Object>> maps, Class<T> clazz) throws Exception {
		List<T> list = Lists.newArrayList();
		if (maps != null && maps.size() > 0) {
			Map<String, Object> map = null;
			T bean = null;
			for (int i = 0, size = maps.size(); i < size; i++) {
				map = maps.get(i);
				bean = clazz.getDeclaredConstructor().newInstance();
				mapToBean(map, bean);
				list.add(bean);
			}
		}
		return list;
	}
}
