package com.beirtipol.binding.core.binders.component;

import java.util.List;

import com.beirtipol.binding.core.binders.AbstractBinder;
import com.beirtipol.binding.core.delegates.IMultiplePresentableComponentDelegate;

@SuppressWarnings("rawtypes")
public abstract class AbstractMultiplePresentableComponentBinder extends
		AbstractBinder<IMultiplePresentableComponentDelegate> implements
		IMultiplePresentableComponentBinder {
	@Override
	public void updateUI() {
		if (delegate != null) {
			List<IPresentableComponentBinder> binders = getComponentBinders();
			delegate.setComponentBinders(binders);
			for (IPresentableComponentBinder binder : binders) {
				binder.updateUI();
			}
		}
	}

	@Override
	protected void setupListeners() {
	}
}