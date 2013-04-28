package com.beirtipol.binding.core.binders.component;

import com.beirtipol.binding.core.binders.AbstractBinder;
import com.beirtipol.binding.core.delegates.IExpandItemDelegate;

public abstract class AbstractExpandItemBinder extends AbstractBinder<IExpandItemDelegate> implements IExpandItemBinder {
	protected boolean isExpanded = true;
	protected boolean isSelected;

	@Override
	public void updateUI() {
		IExpandItemDelegate delegate = getDelegate();
		if (delegate != null) {
			delegate.setExpanded(isExpanded());
			delegate.setTitle(getTitle());
			delegate.setToolTipText(getToolTip());
			getDelegate().setSelected(isSelected);
			getComponentBinder().updateUI();
		}
	}

	@Override
	public void setExpandedIntoModel(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	@Override
	public boolean isExpanded() {
		return isExpanded;
	}

	@Override
	protected void setupListeners() {
		IExpandItemDelegate delegate = getDelegate();
		if (delegate != null) {
			delegate.addExpandListener(this);
		}
	}

	@Override
	public String getToolTip() {
		return null;
	}

	@Override
	public boolean isSelected() {
		return isSelected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.isSelected = selected;
	}
}