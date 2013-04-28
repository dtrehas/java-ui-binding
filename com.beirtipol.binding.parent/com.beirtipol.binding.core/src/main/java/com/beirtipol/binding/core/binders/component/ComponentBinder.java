package com.beirtipol.binding.core.binders.component;

import com.beirtipol.binding.core.binders.AbstractBinder;
import com.beirtipol.binding.core.delegates.IComponentDelegate;

public class ComponentBinder extends AbstractBinder<IComponentDelegate>
		implements IComponentBinder {
	@Override
	public void updateUI() {
		final IComponentDelegate delegate = getDelegate();
		if (delegate != null) {
			try {
				delegate.setVisible(isVisible());
				if (!isVisible()) {
					return;
				}
				delegate.setEnabled(isEnabled());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void setupListeners() {
	}
}