package com.beirtipol.binding.core.delegates;

import com.beirtipol.binding.core.binders.component.ITabItemBinder;

public interface ITabItemDelegate extends IDelegate {
	void setTitle(String title);

	void addCloseListener(ITabItemBinder abstractTabItemBinder);
}