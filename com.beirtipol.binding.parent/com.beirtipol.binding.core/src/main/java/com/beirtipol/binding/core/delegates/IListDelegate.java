package com.beirtipol.binding.core.delegates;

import java.awt.Color;
import java.util.List;

import com.beirtipol.binding.core.binders.widget.IListBinder;
import com.beirtipol.binding.core.util.ItemBinder;

public interface IListDelegate extends IDelegate {
	public void setSelectedItems(List<?> items);

	public void setAllItems(List<?> items);

	public void setItemBinder(ItemBinder<?> converter);

	public void addSelectionListener(IListBinder<?> binder);

	public void addMouseListener(IListBinder<?> binder);

	public void addKeyListener(IListBinder<?> binder);

	public void setBackground(Color background);

	public void setForeground(Color textColour);

	public void setEnabled(boolean enabled);

	public void setVisible(boolean visible);

	public void setToolTip(String text);
}
