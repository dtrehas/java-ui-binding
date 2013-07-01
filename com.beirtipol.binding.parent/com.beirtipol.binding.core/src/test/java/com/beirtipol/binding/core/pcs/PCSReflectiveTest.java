package com.beirtipol.binding.core.pcs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beirtipol.binding.core.pcs.BindableMethod;
import com.beirtipol.binding.core.pcs.BindableMethod.Type;
import com.beirtipol.binding.core.pcs.IBindable;
import com.beirtipol.binding.core.util.ReflectionUtils;

public class PCSReflectiveTest {

	@SuppressWarnings("serial")
	private static class PCSException extends RuntimeException {
		public PCSException(String message) {
			super(message);
		}

		public PCSException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	public static void testObject(Object o) throws PCSException {
		if (o == null) {
			throw new IllegalArgumentException("Object cannot be null");
		}
		Class<? extends Object> clazz = o.getClass();

		if (!(o instanceof IBindable)) {
			// Do not test
			return;
		}

		Method[] methods = clazz.getDeclaredMethods();
		boolean bindableAnnotationFound = false;
		for (Method method : methods) {
			BindableMethod bindableAnnotation = method.getAnnotation(BindableMethod.class);
			if (bindableAnnotation == null) {
				continue;
			}
			bindableAnnotationFound = true;
			final String fieldName = bindableAnnotation.fieldName();

			if (bindableAnnotation.type() == Type.GET) {
				continue;
			}

			Object fieldValue = null;
			Method getMethod = null;
			List<Method> allDeclaredMethods = ReflectionUtils.getAllDeclaredMethods(clazz);
			getterSearchLoop: for (Method getter : allDeclaredMethods) {
				BindableMethod getterAnnotation = getter.getAnnotation(BindableMethod.class);
				if (getterAnnotation == null) {
					continue;
				}
				if (getterAnnotation.type() == Type.GET && fieldName.equals(getterAnnotation.fieldName())) {
					getMethod = getter;
					break getterSearchLoop;
				}
			}

			if (getMethod == null) {
				throw new PCSException("Could not detect 'get' method to mirror '" + method.getName() + "' in class '" + clazz.getName() + "'");
			}
			getMethod.setAccessible(true);
			try {
				fieldValue = getMethod.invoke(o, new Object[0]);
			} catch (Exception e1) {
				throw new PCSException("Error occurred invoking getter", e1);
			}
			if (fieldValue == null) {
				throw new PCSException(clazz.getName() + "." + getMethod.getName() + " returned null. Please test with a fully populated object.");
			}

			// Recursively test objects
			testObject(fieldValue);
			final Map<String, Boolean> fieldChangeEvents = new HashMap<String, Boolean>();

			PropertyChangeListener fieldChangeListener = new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					fieldChangeEvents.put(fieldName, true);
				}
			};

			PropertyChangeSupport changeSupport = ((IBindable) o).changeSupport();
			changeSupport.addPropertyChangeListener(fieldName, fieldChangeListener);

			Object newValue = null;

			// We cannot set 'null' in a primitive method as it will barf.
			// Subtract 1 in case the value has not been set in the test as
			// primitive numbers will default to 0 if not initialised
			// explicitly.
			if (fieldValue instanceof Integer) {
				newValue = (Integer) fieldValue - 1 * -1;
			} else if (fieldValue instanceof Double) {
				if (Double.isNaN(((Double) fieldValue).doubleValue())) {
					throw new PCSException(clazz.getName() + "." + getMethod.getName() + " returned NaN. Please populate this field in your test.");
				}
				newValue = (Double) fieldValue - 1 * -1;
			} else if (fieldValue instanceof Float) {
				newValue = (Float) fieldValue - 1 * -1;
			} else if (fieldValue instanceof Long) {
				newValue = (Long) fieldValue - 1 * -1;
			} else if (fieldValue instanceof Byte) {
				newValue = (Byte) fieldValue - 1 * -1;
			} else if (fieldValue instanceof Character) {
				newValue = (Character) fieldValue - 1 * -1;
			} else if (fieldValue instanceof Short) {
				newValue = (Short) fieldValue - 1 * -1;
			} else if (fieldValue instanceof Boolean) {
				newValue = !(Boolean) fieldValue;
			}
			try {
				method.setAccessible(true);
				method.invoke(o, newValue);
			} catch (Exception e) {
				throw new PCSException("Error occurred calling " + clazz.getCanonicalName() + "." + method.getName() + "(null)", e);
			}
			try {
				Object setValue = getMethod.invoke(o, new Object[0]);

				if (setValue != null && !setValue.equals(newValue)) {
					throw new PCSException("Field '" + fieldName + "' should have been set to '" + newValue + "' when calling '" + method.getName() + "' on class '" + clazz.getCanonicalName() + "'");
				}
			} catch (Exception e1) {
				throw new PCSException("Error occurred invoking getter", e1);
			}
			if (fieldChangeEvents.get(fieldName) == null) {
				throw new PCSException("Expected that an event would be generated when calling  '" + method.getName() + "' on class '" + clazz.getCanonicalName() + "'");
			}

			try {
				// return the original value
				method.invoke(o, fieldValue);
			} catch (Exception e1) {
				throw new PCSException("Error occurred resetting value", e1);
			}
		}
		if (!bindableAnnotationFound) {
			throw new PCSException("No bindable methods found in this class. Please tag any bindable methods with the @BindableMethod annotation");
		}
	}
}
