package com.beirtipol.binding.core.pcs;

import java.lang.reflect.Method;
import java.util.List;

import com.beirtipol.binding.core.pcs.BindableMethod.Type;
import com.beirtipol.binding.core.util.ReflectionUtils;
import com.beirtipol.binding.core.util.StringStack;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class FieldAccessor<T> {

	@SuppressWarnings("unchecked")
	public T getRealValue(Object bom, StringStack path) {
		Class<? extends Object> bomClazz = bom.getClass();
		String fieldName = path.peek();
		if (path.size() == 1) {
			Method getter = getMethod(bom, fieldName, Type.GET);
			try {
				return (T) getter.invoke(bom, (Object[]) null);
			} catch (Exception e) {
				throw new RuntimeException("Unable to set field '" + fieldName + "' on object '" + bomClazz.getName() + "'", e);
			}
		} else {
			Object innerBom = getInner(bom, fieldName);
			return getRealValue(innerBom, path.drop());
		}
	}

	public void setRealValue(Object bom, StringStack path, T value) {
		Class<? extends Object> bomClazz = bom.getClass();
		String fieldName = path.peek();
		if (path.size() == 1) {
			Method setter = getMethod(bom, fieldName, Type.SET);

			try {
				setter.invoke(bom, value);
			} catch (Exception e) {
				throw new RuntimeException("Unable to set field '" + fieldName + "' on object '" + bomClazz.getName() + "'", e);
			}
		} else {
			Object innerBom = getInner(bom, fieldName);
			setRealValue(innerBom, path.drop(), value);
		}
	}

	private Object getInner(Object bom, String fieldName) {
		Class<? extends Object> bomClazz = bom.getClass();
		Object innerBom = null;
		Method method = getMethod(bom, fieldName, Type.GET);
		try {
			innerBom = method.invoke(bom, (Object[]) null);
		} catch (Exception e) {
			throw new RuntimeException("Unable to get field '" + fieldName + "' from object '" + bomClazz.getName() + "'", e);
		}
		return innerBom;
	}

	private Method getMethod(Object bom, String fieldName, BindableMethod.Type methodType) {
		Class<? extends Object> bomClazz = bom.getClass();
		List<Method> methods = ReflectionUtils.getAllDeclaredMethods(bomClazz);
		Iterable<Method> applicableMethods = Iterables.filter(methods, new BindableMethodPredicate(fieldName, methodType));
		if (applicableMethods.iterator().hasNext()) {
			return applicableMethods.iterator().next();
		}
		throw new RuntimeException(methodType + " method not found for field '" + fieldName + "' on object '" + bomClazz.getName() + "'");
	}

	private class BindableMethodPredicate implements Predicate<Method> {
		private final Type type;
		private final String field;

		public BindableMethodPredicate(String field, BindableMethod.Type type) {
			this.field = field;
			this.type = type;
		}

		@Override
		public boolean apply(Method input) {
			BindableMethod bindableAnnotation = input.getAnnotation(BindableMethod.class);
			if (bindableAnnotation != null) {
				boolean result = type == bindableAnnotation.type();
				result &= field.equals(bindableAnnotation.fieldName());
				return result;
			}
			return false;
		}
	}
}
