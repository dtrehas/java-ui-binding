package com.beirtipol.binding.core.binders.dialog;

import java.io.File;

import com.beirtipol.binding.core.delegates.IFileBasedDialogDelegate;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class AbstractFileDialogBinder<D extends IFileBasedDialogDelegate>
		extends DialogBinder<D> implements ICallbackDialogBinder<File, D> {
	protected abstract String getFilterPath();

	@Override
	public void open() {
		if (delegate != null) {
			delegate.setFilterPath(getFilterPath());
		}
		super.open();
	}

	@Override
	public void setDelegate(D delegate) {
		super.setDelegate(delegate);
		if (delegate != null) {
			delegate.setBinder(this);
		}
	}

}
