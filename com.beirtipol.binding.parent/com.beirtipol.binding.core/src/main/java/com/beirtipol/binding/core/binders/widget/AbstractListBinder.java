package com.beirtipol.binding.core.binders.widget;

import java.awt.Color;
import java.util.List;

import com.beirtipol.binding.core.delegates.IListDelegate;

public abstract class AbstractListBinder<T> extends
		AbstractWidgetBinder<IListDelegate> implements IListBinder<T> {

	@Override
	public void updateUI() {
		final IListDelegate delegate = getDelegate();
		if (delegate != null) {
			try {
				delegate.setVisible(isVisible());
				if (!isVisible()) {
					return;
				}
				delegate.setItemBinder(getItemBinder());
				delegate.setAllItems(getAllItems());
				List<T> selected = getSelectedItems();
				delegate.setSelectedItems(selected);
				delegate.setBackground(getBackground());
				delegate.setForeground(getForeground());
				delegate.setEnabled(isEnabled());
				delegate.setToolTip(getToolTip());
			} catch (Exception e) {
				e.printStackTrace();
				delegate.setBackground(Color.RED);
			}
		}
	}

	@Override
	protected void setupListeners() {
		delegate.addKeyListener(this);
		delegate.addMouseListener(this);
		delegate.addSelectionListener(this);
	}
}