package com.beirtipol.binding.swt;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.widgets.Composite;

import com.beirtipol.binding.core.binders.IPresenter;

public abstract class SWTAbstractPresentableComposite<P extends IPresenter> extends Composite {
	private AtomicBoolean isInitialised = new AtomicBoolean(false);

	public SWTAbstractPresentableComposite(Composite parent, int style) {
		super(parent, style);
		createControls();
	}

	public abstract void setPresenter(P presenter);

	protected abstract void doCreateControls();

	public void createControls() {
		if (isInitialised.compareAndSet(false, true)) {
			doCreateControls();
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}