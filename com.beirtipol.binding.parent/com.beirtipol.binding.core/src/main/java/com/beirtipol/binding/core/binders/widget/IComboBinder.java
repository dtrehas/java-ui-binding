package com.beirtipol.binding.core.binders.widget;

import java.awt.Color;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.delegates.IComboDelegate;
import com.beirtipol.binding.core.util.ItemBinder;

public interface IComboBinder extends IBasicBinder<IComboDelegate> {
	Object[] getAvailableItems();

	Object getSelectedItem();

	void setSelectedItem(Object o);

	Color getForeground();

	Color getBackground();

	@SuppressWarnings("rawtypes")
	ItemBinder getItemBinder();
}