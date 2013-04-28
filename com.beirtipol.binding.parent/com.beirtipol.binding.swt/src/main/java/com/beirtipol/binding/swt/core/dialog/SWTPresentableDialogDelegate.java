package com.beirtipol.binding.swt.core.dialog;

import org.eclipse.swt.widgets.Display;

import com.beirtipol.binding.core.binders.IPresenter;
import com.beirtipol.binding.core.delegates.IPresentableDialogDelegate;

public class SWTPresentableDialogDelegate<P extends IPresenter> extends SWTDialogDelegate implements IPresentableDialogDelegate<P> {

	private final SWTPresentableDialog<P> dialog;

	public SWTPresentableDialogDelegate(SWTPresentableDialog<P> dialog) {
		super(dialog);
		this.dialog = dialog;

	}

	@Override
	public void setPresenter(final P presenter) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				dialog.setPresenter(presenter);
			}
		});
	}
}