package com.beirtipol.binding.core.binders.dialog;

import com.beirtipol.binding.core.delegates.IDialogDelegate;

/**
 * An enhancement to {@link IDialogBinder} which provides a callback upon close.
 * 
 * @author O041484
 * @param <T>
 *            The type of object returned via the callback from the dialog
 */
public interface ICallbackDialogBinder<T, D extends IDialogDelegate> extends
		IDialogBinder<D> {
	/**
	 * Provides a callback from the dialog to the binder once the dialog has
	 * been closed.
	 * 
	 * @param result
	 *            The resulting object from the closure of the dialog.
	 */
	void onClose(T result);
}
