package com.beirtipol.binding.core.binders.widget;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.delegates.IButtonDelegate;

public interface IButtonBinder extends IBasicBinder<IButtonDelegate> {
	void addPressedListener();

	void pressed();

	String getText();

	String getImagePath();
}