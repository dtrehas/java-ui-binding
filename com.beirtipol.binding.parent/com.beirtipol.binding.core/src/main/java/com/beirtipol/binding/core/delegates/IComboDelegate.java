package com.beirtipol.binding.core.delegates;

import com.beirtipol.binding.core.binders.widget.IComboBinder;

public interface IComboDelegate extends IWidgetDelegate {
	void setSelected(Object o);

	void addTraverseListener(IComboBinder binder);

	void addFocusListener(IComboBinder binder);

	void addSelectionListener(IComboBinder binder);

	void setBinder(IComboBinder comboBinder);

}