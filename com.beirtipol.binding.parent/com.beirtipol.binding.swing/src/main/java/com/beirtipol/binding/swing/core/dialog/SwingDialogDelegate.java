package com.beirtipol.binding.swing.core.dialog;

import javax.swing.JComponent;
import javax.swing.JDialog;

import com.beirtipol.binding.core.delegates.IDialogDelegate;
import com.beirtipol.binding.swing.util.SwingDialogUtils;

public class SwingDialogDelegate implements IDialogDelegate {

	private JDialog dialog;
	private JComponent parentComponent;

	public SwingDialogDelegate(JDialog dialog) {
		this.dialog = dialog;
	}

	public SwingDialogDelegate(JDialog dialog, JComponent parentComponent) {
		this.dialog = dialog;
		this.parentComponent = parentComponent;
	}

	@Override
	public void free() {
		this.dialog = null;
	}

	@Override
	public void openDialog() {
		SwingDialogUtils.openDialogNextToParent(parentComponent, dialog);
	}
}