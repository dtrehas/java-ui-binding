package com.beirtipol.binding.core.binders.component;

import java.awt.Color;

import com.beirtipol.binding.core.binders.AbstractBinder;
import com.beirtipol.binding.core.binders.IPresenter;
import com.beirtipol.binding.core.delegates.IComponentDelegate;
import com.beirtipol.binding.core.delegates.IPresentableComponentDelegate;

public abstract class AbstractPresentableComponentBinder<T extends IPresenter>
		extends AbstractBinder<IPresentableComponentDelegate<T>> implements
		IPresentableComponentBinder<T> {
	@Override
	public void updateUI() {
		final IPresentableComponentDelegate<T> delegate = getDelegate();
		if (delegate != null) {
			try {
				boolean visible = isVisible();
				delegate.setVisible(visible);
				if (!visible) {
					return;
				}
				delegate.setEnabled(isEnabled());
				T presenter = getPresenter();
				if (presenter != null) {
					delegate.setPresenter(presenter);
					presenter.updateUI();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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
	protected void setupListeners() {
		IComponentDelegate delegate = getDelegate();
		if (delegate != null) {
			// No listeners required (yet)
		}
	}

	@Override
	public void free() {
		if (delegate != null) {
			delegate.free();
		}
	}
}