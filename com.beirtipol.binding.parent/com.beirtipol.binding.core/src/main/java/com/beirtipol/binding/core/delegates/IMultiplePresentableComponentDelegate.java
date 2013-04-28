package com.beirtipol.binding.core.delegates;

import java.util.Collection;

import com.beirtipol.binding.core.binders.component.IPresentableComponentBinder;

@SuppressWarnings("rawtypes")
public interface IMultiplePresentableComponentDelegate extends IDelegate {
	void setComponentBinders(Collection<IPresentableComponentBinder> binders);
}