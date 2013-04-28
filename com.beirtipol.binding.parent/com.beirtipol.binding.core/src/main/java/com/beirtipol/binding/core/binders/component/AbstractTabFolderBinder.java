package com.beirtipol.binding.core.binders.component;

import java.util.List;

import com.beirtipol.binding.core.binders.AbstractBinder;
import com.beirtipol.binding.core.delegates.ITabFolderDelegate;

public abstract class AbstractTabFolderBinder extends AbstractBinder<ITabFolderDelegate> implements ITabFolderBinder {
	@Override
	public synchronized void updateUI() {
		if (delegate != null) {
			List<ITabItemBinder> binders = getTabItemBinders();
			delegate.setTabItemBinders(binders);
		}
	}

	@Override
	protected void setupListeners() {
	}
}