package com.beirtipol.binding.core.binders.widget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beirtipol.binding.core.delegates.IButtonDelegate;

public abstract class AbstractButtonBinder extends AbstractWidgetBinder<IButtonDelegate> implements IButtonBinder {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractButtonBinder.class);
	private final String initialText;

	public AbstractButtonBinder() {
		this("");
	}

	public AbstractButtonBinder(String initialText) {
		this.initialText = initialText;
	}

	@Override
	public String getText() {
		return initialText;
	}

	@Override
	public void addPressedListener() {
		getDelegate().addPressedListener(this);
	}

	@Override
	public String getImagePath() {
		return null;
	}

	@Override
	public void pressed() {
		if (isEnabled()) {
			try {
				handlePressed();
			} catch (Exception e) {
				LOGGER.error("Error occurred handling pressed()", e);
			}
			updateUI();
		} else {
			updateUI();
		}
	}

	public abstract void handlePressed();

	@Override
	public void updateUI() {
		final IButtonDelegate delegate = getDelegate();
		if (delegate != null) {
			try {
				delegate.setVisible(isVisible());
				if (!isVisible()) {
					return;
				}
				delegate.setEnabled(isEnabled());
				delegate.setText(getText());
				delegate.setToolTip(getToolTip());
				delegate.setImagePath(getImagePath());
			} catch (Exception e) {
				LOGGER.error("Error occurred updating delegate", e);
			}
		}
	}

	@Override
	protected void setupListeners() {
		IButtonDelegate delegate = getDelegate();
		if (delegate != null) {
			delegate.addPressedListener(this);
		}
	}
}
