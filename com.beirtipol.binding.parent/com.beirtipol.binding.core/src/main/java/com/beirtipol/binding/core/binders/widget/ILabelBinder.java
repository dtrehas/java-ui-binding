package com.beirtipol.binding.core.binders.widget;

import java.awt.Color;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.delegates.ILabelDelegate;

public interface ILabelBinder extends IBasicBinder<ILabelDelegate> {
	String getTextFromModel();

	Color getForeground();

	Color getBackground();
}