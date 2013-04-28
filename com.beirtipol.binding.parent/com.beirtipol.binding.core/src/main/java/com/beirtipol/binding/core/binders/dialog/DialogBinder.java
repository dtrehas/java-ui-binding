package com.beirtipol.binding.core.binders.dialog;

import com.beirtipol.binding.core.delegates.IDialogDelegate;

public class DialogBinder<D extends IDialogDelegate> implements IDialogBinder<D> {
	protected D delegate;

	@Override
	public void setDelegate(D delegate) {
		this.delegate = delegate;
	}

	@Override
	public void open() {
		if (delegate != null) {
			delegate.openDialog();
		}
	}

}
