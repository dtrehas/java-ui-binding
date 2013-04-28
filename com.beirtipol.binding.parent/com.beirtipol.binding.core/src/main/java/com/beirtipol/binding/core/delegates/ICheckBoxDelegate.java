package com.beirtipol.binding.core.delegates;

import com.beirtipol.binding.core.binders.widget.ICheckBoxBinder;

public interface ICheckBoxDelegate extends IWidgetDelegate {
	void setChecked(boolean checked);

	void addPressedListener(ICheckBoxBinder binder);

	void setText(String text);
}
