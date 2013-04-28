package com.beirtipol.binding.core.binders.dialog;

import java.util.HashMap;
import java.util.Map;

import com.beirtipol.binding.core.delegates.IFileDialogDelegate;

public abstract class FileDialogBinder extends AbstractFileDialogBinder<IFileDialogDelegate> {

	protected String getFileName() {
		return null;
	}

	protected Map<String, String> getFilters() {
		return new HashMap<String, String>();
	}

	@Override
	public void open() {
		if (delegate != null) {
			delegate.setFileName(getFileName());
			delegate.setFilters(getFilters());
			delegate.setFilterPath(getFilterPath());
			delegate.openDialog();
		}
	}

	@Override
	protected String getFilterPath() {
		return null;
	}
}