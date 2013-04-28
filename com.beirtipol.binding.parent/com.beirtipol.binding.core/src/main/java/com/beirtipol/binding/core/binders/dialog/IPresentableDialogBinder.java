package com.beirtipol.binding.core.binders.dialog;

import java.awt.Color;

import com.beirtipol.binding.core.binders.IPresenter;
import com.beirtipol.binding.core.delegates.IPresentableDialogDelegate;

/**
 * An extension of IDialogBinder to allow hooking a Presenter in to a component
 * contained within a dialog. Just think of it as a PresentableComponent which
 * you can 'open'
 * 
 * @author O041484
 * 
 * @param <P>
 *            The type of presenter to be set on the dialog.
 */
public interface IPresentableDialogBinder<P extends IPresenter> extends IDialogBinder<IPresentableDialogDelegate<P>> {
	public Color getForeground();

	public Color getBackground();

	public P getPresenter();
}