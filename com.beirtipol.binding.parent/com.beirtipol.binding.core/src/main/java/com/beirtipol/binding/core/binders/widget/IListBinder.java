package com.beirtipol.binding.core.binders.widget;

import java.awt.Color;
import java.util.List;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.delegates.IListDelegate;
import com.beirtipol.binding.core.util.ItemBinder;

public interface IListBinder<T> extends IBasicBinder<IListDelegate> {
	public List<T> getAllItems();

	public List<T> getSelectedItems();

	public void setSelectedItems(List<?> items);

	public Color getBackground();

	@Override
	public String getToolTip();

	public void execute(T item);

	ItemBinder<T> getItemBinder();
}