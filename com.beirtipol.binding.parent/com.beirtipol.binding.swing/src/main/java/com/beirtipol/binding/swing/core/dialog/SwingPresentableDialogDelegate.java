package com.beirtipol.binding.swing.core.dialog;

import javax.swing.JDialog;

import com.beirtipol.binding.core.binders.IPresenter;
import com.beirtipol.binding.core.delegates.IPresentableDialogDelegate;
import com.beirtipol.binding.swing.AbstractPresentableSwingPanel;

public class SwingPresentableDialogDelegate<P extends IPresenter> extends
		SwingDialogDelegate implements IPresentableDialogDelegate<P> {
	private final AbstractPresentableSwingPanel<P> child;

	public SwingPresentableDialogDelegate(JDialog dialog,
			AbstractPresentableSwingPanel<P> child) {
		super(dialog);
		this.child = child;
	}

	@Override
	public void setPresenter(P presenter) {
		child.setPresenter(presenter);
	}
}