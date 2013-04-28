package com.beirtipol.binding.core.binders.dialog;

import com.beirtipol.binding.core.delegates.IDialogDelegate;

/**
 * To be used for displaying information dialogs only. If return values are
 * required, @see IPresentableDialogBinder
 * 
 * @author O041484
 */
public interface IDialogBinder<D extends IDialogDelegate> {
	void setDelegate(D delegate);

	void open();
}