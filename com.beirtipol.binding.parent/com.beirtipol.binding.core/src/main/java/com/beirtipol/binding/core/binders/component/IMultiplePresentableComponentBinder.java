package com.beirtipol.binding.core.binders.component;

import java.util.List;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.delegates.IMultiplePresentableComponentDelegate;

public interface IMultiplePresentableComponentBinder extends
		IBasicBinder<IMultiplePresentableComponentDelegate> {
	@SuppressWarnings("rawtypes")
	public List<IPresentableComponentBinder> getComponentBinders();
}