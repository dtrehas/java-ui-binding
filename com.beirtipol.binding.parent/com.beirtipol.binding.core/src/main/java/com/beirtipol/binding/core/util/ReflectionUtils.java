package com.beirtipol.binding.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides methods to get all declared and inherited methods and fields from a
 * particular class. Caches these fields and methods to provide faster lookups.
 * 
 * @author o041484
 */
public class ReflectionUtils {
	private static Map<Class<?>, List<Field>> DECLARED_FIELDS_BY_CLASS = new HashMap<Class<?>, List<Field>>();
	private static Map<Class<?>, List<Method>> DECLARED_METHODS_BY_CLASS = new HashMap<Class<?>, List<Method>>();
	private static Map<Class<?>, HashMap<String, Field>> DECLARED_FIELDS_BY_CLASS_BY_NAME = new HashMap<Class<?>, HashMap<String, Field>>();
	private static Map<Class<?>, HashMap<String, Method>> DECLARED_METHODS_BY_CLASS_BY_NAME = new HashMap<Class<?>, HashMap<String, Method>>();

	@SuppressWarnings("unchecked")
	public static <T> T cast(Object o) {
		return (T) o;
	}

	public static List<Field> getAllDeclaredFields(Object obj) {
		if (obj == null) {
			return new ArrayList<Field>();
		} else {
			Class<?> clazz = obj.getClass();
			return getAllDeclaredFields(clazz);
		}
	}

	public static List<Field> getAllDeclaredFields(Class<?> clazz) {
		List<Field> result = DECLARED_FIELDS_BY_CLASS.get(clazz);
		if (result != null) {
			return result;
		}

		result = new ArrayList<Field>();

		if (clazz == null) {
			return result;
		}

		// Local class
		List<Field> objFields = Arrays.asList(clazz.getDeclaredFields());
		result.addAll(objFields);

		// Super classes
		Class<?> superclass = clazz.getSuperclass();
		while (superclass != null) {
			List<Field> superClassFields = Arrays.asList(superclass.getDeclaredFields());

			result.addAll(superClassFields);

			superclass = superclass.getSuperclass();
		}

		DECLARED_FIELDS_BY_CLASS.put(clazz, result);

		return result;
	}

	public static List<Method> getAllDeclaredMethods(Class<?> clazz) {
		List<Method> result = DECLARED_METHODS_BY_CLASS.get(clazz);
		if (result != null) {
			return result;
		}

		result = new ArrayList<Method>();

		if (clazz == null) {
			return result;
		}

		// Local class
		List<Method> objFields = Arrays.asList(clazz.getDeclaredMethods());
		result.addAll(objFields);

		// Super classes
		Class<?> superclass = clazz.getSuperclass();
		while (superclass != null) {
			List<Method> superClassFields = Arrays.asList(superclass.getDeclaredMethods());

			result.addAll(superClassFields);

			superclass = superclass.getSuperclass();
		}

		DECLARED_METHODS_BY_CLASS.put(clazz, result);

		return result;
	}

	public static Field getDeclaredField(Object obj, String fieldName) throws NoSuchFieldException {
		if (obj == null) {
			return null;
		} else {
			return getDeclaredField(obj.getClass(), fieldName);
		}
	}

	public static Field getDeclaredField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
		Field result = null;
		HashMap<String, Field> map = getAllDeclaredFieldsMap(clazz);

		result = map.get(fieldName);
		if (result == null) {
			throw new NoSuchFieldException("The field '" + fieldName + "' does not exists for '" + clazz.getSimpleName() + "' or any of its superclasses");
		}
		return result;
	}

	public static Method getDeclaredMethod(Class<?> clazz, String methodName) throws NoSuchFieldException {
		Method result = null;
		HashMap<String, Method> map = getAllDeclaredMethodsMap(clazz);

		result = map.get(methodName);
		if (result == null) {
			throw new NoSuchFieldException("The method '" + methodName + "' does not exists for '" + clazz.getSimpleName() + "' or any of its superclasses");
		}
		return result;
	}

	public static HashMap<String, Field> getAllDeclaredFieldsMap(Class<?> clazz) {
		HashMap<String, Field> map = DECLARED_FIELDS_BY_CLASS_BY_NAME.get(clazz);
		if (map == null) {
			map = new HashMap<String, Field>();

			List<Field> fields = getAllDeclaredFields(clazz);

			for (Field field : fields) {
				map.put(field.getName(), field);
			}
			// Insert the map at the end so we can avoid having to synchronize.
			DECLARED_FIELDS_BY_CLASS_BY_NAME.put(clazz, map);
		}
		return map;
	}

	public static HashMap<String, Method> getAllDeclaredMethodsMap(Class<?> clazz) {
		HashMap<String, Method> map = DECLARED_METHODS_BY_CLASS_BY_NAME.get(clazz);
		if (map == null) {
			map = new HashMap<String, Method>();

			List<Method> fields = getAllDeclaredMethods(clazz);

			for (Method method : fields) {
				map.put(method.getName(), method);
			}
			// Insert the map at the end so we can avoid having to synchronize.
			DECLARED_METHODS_BY_CLASS_BY_NAME.put(clazz, map);
		}
		return map;
	}
}
