package com.beirtipol.binding.core.binders.component;

import java.util.List;

import com.beirtipol.binding.core.binders.AbstractBinder;
import com.beirtipol.binding.core.delegates.IExpandBarDelegate;

public abstract class AbstractExpandBarBinder extends AbstractBinder<IExpandBarDelegate> implements IExpandBarBinder {
	@Override
	public synchronized void updateUI() {
		if (delegate != null) {
			List<IExpandItemBinder> binders = getExpandItemBinders();
			delegate.setExpandItemBinders(binders);
		}
	}

	@Override
	protected void setupListeners() {
	}
}