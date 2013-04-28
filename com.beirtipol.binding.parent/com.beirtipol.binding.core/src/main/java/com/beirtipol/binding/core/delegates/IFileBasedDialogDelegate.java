package com.beirtipol.binding.core.delegates;

import com.beirtipol.binding.core.binders.dialog.AbstractFileDialogBinder;

/**
 * Common hierarchy for File and Directory dialog binding.
 * 
 * @author O041484
 * 
 * @param <B>
 *            The type of FileBasedBinder to be used.
 */
@SuppressWarnings("rawtypes")
public interface IFileBasedDialogDelegate<B extends AbstractFileDialogBinder>
		extends IDialogDelegate {
	void setFilterPath(String filterPath);

	void setBinder(B binder);
}
