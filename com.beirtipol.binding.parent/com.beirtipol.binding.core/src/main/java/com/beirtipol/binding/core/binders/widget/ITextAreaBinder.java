package com.beirtipol.binding.core.binders.widget;

import java.awt.Color;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.delegates.ITextAreaDelegate;

public interface ITextAreaBinder extends IBasicBinder<ITextAreaDelegate> {
	String getTextFromModel();

	void setTextIntoModel(String text);

	Color getForeground();

	Color getBackground();
}
