package com.beirtipol.binding.core.binders.dialog;

import java.awt.Color;

import com.beirtipol.binding.core.binders.IPresenter;
import com.beirtipol.binding.core.delegates.IPresentableDialogDelegate;
import com.beirtipol.binding.core.util.IFreeable;

public abstract class AbstractPresentableDialogBinder<P extends IPresenter> implements IPresentableDialogBinder<P>, IFreeable {

	private IPresentableDialogDelegate<P> delegate;

	@Override
	public void setDelegate(IPresentableDialogDelegate<P> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void open() {
		if (delegate != null) {
			P presenter = getPresenter();
			delegate.openDialog();
			delegate.setPresenter(presenter);
			presenter.updateUI();
		}
	}

	@Override
	public Color getBackground() {
		return null;
	}

	@Override
	public Color getForeground() {
		return null;
	}

	@Override
	public void free() {
		if (delegate != null) {
			delegate.free();
		}
	}
}