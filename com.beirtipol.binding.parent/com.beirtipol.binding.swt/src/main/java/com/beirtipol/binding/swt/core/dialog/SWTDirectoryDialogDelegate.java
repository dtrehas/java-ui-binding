package com.beirtipol.binding.swt.core.dialog;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;

import com.beirtipol.binding.core.binders.dialog.DirectoryDialogBinder;
import com.beirtipol.binding.core.delegates.IDirectoryDialogDelegate;

public class SWTDirectoryDialogDelegate implements IDirectoryDialogDelegate {
	private DirectoryDialog dialog;
	private DirectoryDialogBinder binder;

	public SWTDirectoryDialogDelegate(DirectoryDialog dialog) {
		this.dialog = dialog;
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
	public void setBinder(DirectoryDialogBinder binder) {
		if (this.binder != null) {
			throw new IllegalStateException("This delegate already has a binder attached and cannot be changed.");
		}
		this.binder = binder;
	}

	@Override
	public void free() {
		this.dialog = null;
		this.binder = null;
	}
}