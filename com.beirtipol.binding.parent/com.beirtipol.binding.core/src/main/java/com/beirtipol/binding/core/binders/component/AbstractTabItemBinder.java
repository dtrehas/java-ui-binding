package com.beirtipol.binding.core.binders.component;

import com.beirtipol.binding.core.binders.AbstractBinder;
import com.beirtipol.binding.core.delegates.ITabItemDelegate;

public abstract class AbstractTabItemBinder extends
		AbstractBinder<ITabItemDelegate> implements ITabItemBinder {
	protected boolean isExpanded = true;
	protected boolean isSelected;

	@Override
	public void updateUI() {
		ITabItemDelegate delegate = getDelegate();
		if (delegate != null) {
			delegate.setTitle(getTitle());
			getComponentBinder().updateUI();
		}
	}

	@Override
	protected void setupListeners() {
		if (delegate != null) {
			delegate.addCloseListener(this);
		}
	}

	@Override
	public String getToolTip() {
		return null;
	}
}