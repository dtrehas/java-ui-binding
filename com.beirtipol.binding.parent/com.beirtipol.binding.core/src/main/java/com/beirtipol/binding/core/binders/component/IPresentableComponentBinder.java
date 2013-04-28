package com.beirtipol.binding.core.binders.component;

import java.awt.Color;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.binders.IPresenter;
import com.beirtipol.binding.core.delegates.IPresentableComponentDelegate;

public interface IPresentableComponentBinder<T extends IPresenter> extends IBasicBinder<IPresentableComponentDelegate<T>> {
	public Color getForeground();

	public Color getBackground();

	public T getPresenter();
}
