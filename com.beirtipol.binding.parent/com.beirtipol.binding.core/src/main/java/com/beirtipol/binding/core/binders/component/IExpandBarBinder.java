package com.beirtipol.binding.core.binders.component;

import java.util.List;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.delegates.IExpandBarDelegate;

public interface IExpandBarBinder extends IBasicBinder<IExpandBarDelegate> {
	public List<IExpandItemBinder> getExpandItemBinders();
}