package com.beirtipol.binding.swt.core.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;

import com.beirtipol.binding.core.binders.IPresenter;

public abstract class SWTPresentableDialog<P extends IPresenter> extends Dialog {
	protected SWTPresentableDialog(Shell parentShell) {
		super(parentShell);
	}

	public abstract void setPresenter(P presenter);
}