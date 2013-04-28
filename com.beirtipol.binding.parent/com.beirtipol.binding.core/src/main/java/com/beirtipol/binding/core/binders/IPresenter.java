package com.beirtipol.binding.core.binders;

import com.beirtipol.binding.core.util.IFreeable;

public interface IPresenter extends IFreeable {
	/**
	 * Best practice is to use PresentationModelReflectionSupport. This will
	 * reflectively iterate each child field of the presentation model and call
	 * updateGUI() on any IBinder or IPresenter contained within. On some edge
	 * cases, this may lead to a recursive call, when you have anonymous inner
	 * classes referring to the outer class as a field. This is not implemented
	 * as an abstract class to encourage developers to consider the design
	 * first.
	 * 
	 * <pre>
	 * <code>
	 * void updateGUI()
	 * {
	 *   PresentationModelReflectionSupport.updateGUI(this);
	 * }
	 * </code>
	 * @see PresentationModelReflectionSupport#updateUI(Object)
	 * @see IBinder#updateUI()
	 */
	void updateUI();
}
