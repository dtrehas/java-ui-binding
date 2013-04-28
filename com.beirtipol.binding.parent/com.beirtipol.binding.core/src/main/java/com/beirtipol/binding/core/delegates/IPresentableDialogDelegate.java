package com.beirtipol.binding.core.delegates;

import com.beirtipol.binding.core.binders.IPresenter;

public interface IPresentableDialogDelegate<P extends IPresenter> extends
		IDialogDelegate {
	void setPresenter(P presenter);
}