package com.beirtipol.binding.core.binders.widget;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.delegates.ICheckBoxDelegate;

public interface ICheckBoxBinder extends IBasicBinder<ICheckBoxDelegate> {
	Boolean getCheckedFromModel();

	void setCheckedIntoModel(boolean checked);

	String getText();
}