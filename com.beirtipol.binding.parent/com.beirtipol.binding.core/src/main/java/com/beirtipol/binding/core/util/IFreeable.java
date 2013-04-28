package com.beirtipol.binding.core.util;

public interface IFreeable {

	/**
	 * For proper usage, call <code>FreeableReflectionSupport.free()</code>
	 * FreeableReflectionSupport takes care of any member variables which are
	 * also freeable, reducing the amount of code required to free resources.
	 * You should only need to free non IFreeable resources. This should ONLY be
	 * called directly by subclasses, e.g. <blockquote>
	 * 
	 * <pre>
	 * <code>
	 * void free()
	 * {
	 *   super.free();
	 *   // free own resources
	 * }
	 * </code>
	 * @see FreeableReflectionSupport#free(Object)
	 */
	void free();
}
