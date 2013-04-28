package com.beirtipol.binding.core.binders.component;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.delegates.IExpandItemDelegate;

public interface IExpandItemBinder extends IBasicBinder<IExpandItemDelegate> {
	public boolean isExpanded();

	void setExpandedIntoModel(boolean isExpanded);

	public String getTitle();

	@SuppressWarnings("rawtypes")
	public IPresentableComponentBinder getComponentBinder();

	public boolean isSelected();

	void setSelected(boolean selected);
}
