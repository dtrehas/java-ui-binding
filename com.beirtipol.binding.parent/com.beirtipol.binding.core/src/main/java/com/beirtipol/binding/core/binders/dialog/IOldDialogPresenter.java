package com.beirtipol.binding.core.binders.dialog;

import com.beirtipol.binding.core.binders.IPresenter;

public interface IOldDialogPresenter<T> extends IPresenter {
	public T getReturnObject();

	public IValidator getValidator();
}