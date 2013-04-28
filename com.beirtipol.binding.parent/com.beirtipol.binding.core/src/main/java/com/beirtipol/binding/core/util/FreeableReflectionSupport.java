package com.beirtipol.binding.core.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FreeableReflectionSupport {
	/**
	 * WARNING: This should only be called when you are truly sure you want to
	 * destroy an object graph. Do not call it from the 'free' method of an
	 * IFreeable as you could end up in endless-recursive-alice-in-wonderland.
	 * Don't belive me? Try it out...
	 * 
	 * @param c
	 */
	public static void free(Object c) {
		free(c, new IdentityArrayList<Object>());
	}

	/**
	 * Here we track the objects we are freeing to try and avoid cycles and
	 * repetetive frees.
	 * 
	 * @param parent
	 * @param objects
	 */
	@SuppressWarnings("rawtypes")
	public static void free(Object parent, IdentityArrayList<Object> objects) {
		if (parent != null) {
			List<Field> declaredFields = ReflectionUtils
					.getAllDeclaredFields(parent.getClass());

			for (Field field : declaredFields) {
				if ("this$0".equals(field.getName())) {
					// We do not care about the reference to 'this' as it will
					// happen if I am an
					// inner
					// class, i.e. am contained by another presenter which
					// overrides some of my
					// methods.
					continue;
				}
				field.setAccessible(true);
				try {
					Object child = field.get(parent);
					if (child == null) {
						continue;
					}
					if (child instanceof IFreeable) {
						freeMe(objects, child);
					} else if (child instanceof Collection) {
						for (Object element : ((Collection) child)) {
							freeMe(objects, element);
						}
					} else if (child instanceof IFreeable[]) {
						for (Object element : ((Object[]) child)) {
							freeMe(objects, element);
						}
					} else if (child instanceof Map) {
						for (Object key : ((Map) child).keySet()) {
							freeMe(objects, key);
						}

						for (Object value : ((Map) child).values()) {
							freeMe(objects, value);
						}

					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			if (parent instanceof IFreeable) {
				((IFreeable) parent).free();
			}
		}
	}

	public static void freeMe(IdentityArrayList<Object> objects, Object element) {
		if (!objects.contains(element)) {
			objects.add(element);
			free(element, objects);
		}

	}
}
