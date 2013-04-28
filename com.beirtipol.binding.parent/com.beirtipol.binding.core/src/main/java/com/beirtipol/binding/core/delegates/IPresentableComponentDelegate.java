package com.beirtipol.binding.core.delegates;

import com.beirtipol.binding.core.binders.IPresenter;

public interface IPresentableComponentDelegate<P extends IPresenter> extends
		IComponentDelegate {
	void setPresenter(P presenter);
}