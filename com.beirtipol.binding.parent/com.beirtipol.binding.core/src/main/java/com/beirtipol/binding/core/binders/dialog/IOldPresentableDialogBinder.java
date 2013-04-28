package com.beirtipol.binding.core.binders.dialog;

import com.beirtipol.binding.core.delegates.IOldPresentableDialogDelegate;

/**
 * @author O041484
 * @param <T>
 *            The Presenter of the dialog
 */
@SuppressWarnings({ "rawtypes" })
public interface IOldPresentableDialogBinder<T extends IOldDialogPresenter> {
	public static final int OK_PRESSED = 0;
	public static final int CANCEL_PRESSED = 1;

	public T getPresenter();

	public void open();

	public void onOpen();

	public void onClose(int returnCode);

	public String getTitle();

	public String getMessage();

	public void setDelegate(IOldPresentableDialogDelegate<T> dialogDelegate);
}
