package com.beirtipol.binding.core.binders;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.beirtipol.binding.core.util.ReflectionUtils;

public class PresentationModelReflectionSupport {

	static Set<Class<?>> binderPresenterTypes = new HashSet<Class<?>>(2);
	static {
		binderPresenterTypes.add(IBinder.class);
		binderPresenterTypes.add(IPresenter.class);
	}

	/**
	 * Reflectively iterate over each field of the provided object, calling
	 * updateUI() on any IBinder or IPresenter. On encountering an IPresenter,
	 * recursively iterate through the children.
	 * 
	 * @see IBinder
	 * @see IPresenter
	 * @param c
	 */
	public static void updateUI(Object c) {
		List<Field> declaredFields = ReflectionUtils.getAllDeclaredFields(c
				.getClass());

		for (Field field : declaredFields) {
			if ("this$0".equals(field.getName())) {
				// We do not care about the reference to 'this' as it will
				// happen if I am an inner class, i.e. am contained by another
				// presenter which overrides some of my methods.
				continue;
			}
			field.setAccessible(true);
			try {
				Object object = field.get(c);
				if (object instanceof IBinder) {
					IBinder binder = (IBinder) object;
					binder.updateUI();
				} else if (object instanceof IPresenter) {
					IPresenter presentable = (IPresenter) object;
					presentable.updateUI();
				}
				// Right now, I have not used or seen any patterns in which a
				// presenter would hold just a collection of child binders or
				// presenters and need to update them all in a single call.
				// These collections are usually held as parts of other binders
				// or presenters and not all would need to be updated at once.
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}