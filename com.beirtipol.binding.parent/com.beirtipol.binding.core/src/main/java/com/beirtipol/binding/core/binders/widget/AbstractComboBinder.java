package com.beirtipol.binding.core.binders.widget;

import java.awt.Color;

import com.beirtipol.binding.core.delegates.IComboDelegate;

public abstract class AbstractComboBinder extends AbstractWidgetBinder<IComboDelegate> implements IComboBinder {
	@Override
	public void setDelegate(IComboDelegate delegate) {
		super.setDelegate(delegate);
		if (delegate != null) {
			delegate.setBinder(this);
		}
	}

	@Override
	public void updateUI() {
		final IComboDelegate delegate = getDelegate();
		if (delegate != null) {
			try {
				delegate.setVisible(isVisible());
				if (!isVisible()) {
					return;
				}
				Object selected = getSelectedItem();
				delegate.setSelected(selected);
				delegate.setBackground(getBackground());
				delegate.setForeground(getForeground());
				delegate.setEnabled(isEnabled());
				delegate.setToolTip(getToolTip());
			} catch (Exception e) {
				e.printStackTrace();
				delegate.setBackground(Color.RED);
				if (e.getMessage() != null) {
					delegate.setSelected(e.getMessage());
				} else {
					delegate.setSelected(e.getClass().getCanonicalName());
				}
			}
		}
	}

	@Override
	protected void setupListeners() {
		IComboDelegate delegate = getDelegate();
		if (delegate != null) {
			delegate.addTraverseListener(this);
			delegate.addSelectionListener(this);
			delegate.addFocusListener(this);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getToolTip() {
		Object selectedItem = getSelectedItem();
		if (toolTipModel == null && selectedItem != null) {
			return getItemBinder().convert(getSelectedItem());
		}
		return super.getToolTip();
	}
}