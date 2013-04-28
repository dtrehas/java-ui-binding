package com.beirtipol.binding.swt.core.dialog;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.beirtipol.binding.core.binders.dialog.FileDialogBinder;
import com.beirtipol.binding.core.delegates.IFileDialogDelegate;

public class SWTFileDialogDelegate implements IFileDialogDelegate {

	private FileDialog dialog;
	private FileDialogBinder binder;

	public SWTFileDialogDelegate(FileDialog dialog) {
		this.dialog = dialog;
	}

	@Override
	public void setFilters(Map<String, String> filters) {
		String[] filterExtensions = new String[filters.size()];
		String[] filterNames = new String[filters.size()];
		int i = 0;
		for (Entry<String, String> entry : filters.entrySet()) {
			filterNames[i] = entry.getKey();
			filterExtensions[i] = entry.getValue();
			i++;
		}
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterNames(filterNames);
	}

	@Override
	public void setFileName(String fileName) {
		dialog.setFileName(fileName);
	}

	@Override
	public void setFilterPath(String filterPath) {
		dialog.setFilterPath(filterPath);
	}

	@Override
	public void openDialog() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				String result = dialog.open();
				if (binder != null && StringUtils.isNotBlank(result)) {
					binder.onClose(new File(result));
				}
			}
		});
	}

	@Override
	public void setBinder(FileDialogBinder binder) {
		this.binder = binder;
	}

	@Override
	public void free() {
		this.binder = null;
		this.dialog = null;
	}
}
