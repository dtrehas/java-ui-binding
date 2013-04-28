package com.beirtipol.binding.core.binders.widget;

import java.awt.Color;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.delegates.ITextDelegate;

public interface ITextBinder extends IBasicBinder<ITextDelegate> {
	String getTextFromModel();

	void setTextIntoModel(String text);

	Color getForeground();

	Color getBackground();
}