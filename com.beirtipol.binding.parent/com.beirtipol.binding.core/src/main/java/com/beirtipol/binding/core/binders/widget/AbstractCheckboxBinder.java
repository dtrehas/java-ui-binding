package com.beirtipol.binding.core.binders.widget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beirtipol.binding.core.delegates.ICheckBoxDelegate;

public abstract class AbstractCheckboxBinder extends
		AbstractWidgetBinder<ICheckBoxDelegate> implements ICheckBoxBinder {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractCheckboxBinder.class);
	private final String initialText;

	public AbstractCheckboxBinder() {
		this("");
	}

	public AbstractCheckboxBinder(String initialText) {
		this.initialText = initialText;
	}

	@Override
	public String getText() {
		return initialText;
	}

	@Override
	public void updateUI() {
		final ICheckBoxDelegate delegate = getDelegate();
		if (delegate != null) {
			try {
				delegate.setVisible(isVisible());
				if (!isVisible()) {
					return;
				}
				delegate.setChecked(getCheckedFromModel());
				delegate.setEnabled(isEnabled());
				delegate.setText(getText());
				delegate.setToolTip(getToolTip());
			} catch (Exception e) {
				LOGGER.error("Error occurred updating delegate", e);
			}
		}
	}

	@Override
	protected void setupListeners() {
		ICheckBoxDelegate delegate = getDelegate();
		if (delegate != null) {
			delegate.addPressedListener(this);
		}
	}
}
