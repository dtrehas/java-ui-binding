package com.beirtipol.binding.swt.core.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;

import com.beirtipol.binding.core.delegates.IDialogDelegate;

public class SWTDialogDelegate implements IDialogDelegate {
	protected Dialog dialog;

	public SWTDialogDelegate(Dialog dialog) {
		this.dialog = dialog;
	}

	@Override
	public void openDialog() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				dialog.open();
			}
		});
	}

	@Override
	public void free() {
		dialog = null;
	}
}