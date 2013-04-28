package com.beirtipol.binding.core.binders.component;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.delegates.ITabItemDelegate;

public interface ITabItemBinder extends IBasicBinder<ITabItemDelegate> {
	String getTitle();

	@SuppressWarnings("rawtypes")
	IPresentableComponentBinder getComponentBinder();

	void onClose();
}
