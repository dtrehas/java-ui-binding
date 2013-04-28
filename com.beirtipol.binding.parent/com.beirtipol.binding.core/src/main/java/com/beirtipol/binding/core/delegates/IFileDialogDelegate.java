package com.beirtipol.binding.core.delegates;

import java.util.Map;

import com.beirtipol.binding.core.binders.dialog.FileDialogBinder;

public interface IFileDialogDelegate extends
		IFileBasedDialogDelegate<FileDialogBinder> {
	void setFilters(Map<String, String> filters);

	void setFileName(String fileName);
}