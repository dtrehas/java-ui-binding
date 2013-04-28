package com.beirtipol.binding.core.delegates;

import com.beirtipol.binding.core.binders.widget.IButtonBinder;

public interface IButtonDelegate extends IWidgetDelegate {

	void addPressedListener(IButtonBinder binder);

	void setText(String text);

	void setImagePath(String path);

}