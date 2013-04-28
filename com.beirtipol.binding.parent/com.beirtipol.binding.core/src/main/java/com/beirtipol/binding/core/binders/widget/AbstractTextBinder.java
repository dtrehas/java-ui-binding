package com.beirtipol.binding.core.binders.widget;

import java.awt.Color;

import com.beirtipol.binding.core.delegates.ITextDelegate;

public abstract class AbstractTextBinder extends AbstractWidgetBinder<ITextDelegate> implements ITextBinder {

	@Override
	public void updateUI() {
		final ITextDelegate delegate = getDelegate();
		if (delegate != null) {
			try {
				boolean isVisible = isVisible();
				delegate.setVisible(isVisible);
				if (!isVisible) {
					return;
				}
				String textFromModel = getTextFromModel();
				textFromModel = textFromModel == null ? "" : textFromModel;
				if (textFromModel != null) {
					delegate.setText(textFromModel);
				}

				delegate.setEnabled(isEnabled());
				delegate.setBackground(getBackground());
				delegate.setForeground(getForeground());
				delegate.setToolTip(getToolTip());
			} catch (Exception e) {
				handleException(e);
			}
		}
	}

	protected void handleException(Exception e) {
		e.printStackTrace();
		ITextDelegate delegate = getDelegate();
		if (delegate == null) {
			return;
		}
		delegate.setBackground(Color.RED);
		if (e.getMessage() != null) {
			delegate.setText(e.getMessage());
		} else {
			delegate.setText(e.getClass().getCanonicalName());
		}
	}

	@Override
	protected void setupListeners() {
		ITextDelegate delegate = getDelegate();
		if (delegate != null) {
			delegate.addFocusListener(this);
			delegate.addTraverseListener(this);
		}
	}
}