package com.beirtipol.binding.core.delegates;

import com.beirtipol.binding.core.binders.dialog.IOldDialogPresenter;
import com.beirtipol.binding.core.binders.dialog.IOldPresentableDialogBinder;
import com.beirtipol.binding.core.binders.dialog.IValidator;

@SuppressWarnings({ "rawtypes" })
public interface IOldPresentableDialogDelegate<T extends IOldDialogPresenter>
		extends IDialogDelegate {
	public void setTitle(String title);

	public void setMessage(String message);

	public void setPresenter(T presenter);

	public void setValidator(IValidator validator);

	public void addCloseListener(IOldPresentableDialogBinder listener);

	public void addOpenListener(IOldPresentableDialogBinder listener);

}