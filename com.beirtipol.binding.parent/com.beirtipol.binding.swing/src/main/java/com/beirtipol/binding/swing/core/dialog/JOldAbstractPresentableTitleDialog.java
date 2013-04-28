package com.beirtipol.binding.swing.core.dialog;

import java.awt.Component;
import java.awt.Point;

import javax.swing.JComponent;

import com.beirtipol.binding.core.binders.dialog.IOldDialogPresenter;
import com.beirtipol.binding.core.binders.dialog.IValidator;
import com.beirtipol.binding.core.binders.dialog.ValidationErrorException;
import com.beirtipol.binding.core.binders.dialog.ValidationException;
import com.beirtipol.binding.core.binders.dialog.ValidationWarningException;

@SuppressWarnings({ "serial" })
public abstract class JOldAbstractPresentableTitleDialog<T extends IOldDialogPresenter> extends JAbstractTitleDialog {
	private IValidator validator;

	/**
	 * Modeless by default.
	 */
	public JOldAbstractPresentableTitleDialog() {
		super();
	}

	/**
	 * @param modal
	 */
	public JOldAbstractPresentableTitleDialog(boolean modal) {
		super(modal);
	}

	@Override
	protected JComponent createDialogArea() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLocation(Point p) {
		relativeLocationSet = true;
		super.setLocation(p);
	}

	/**
	 * @param relativeToComponent
	 * @param modal
	 */
	public JOldAbstractPresentableTitleDialog(Component relativeToComponent, boolean modal) {
		super(relativeToComponent, modal);
	}

	public abstract void setPresenter(T presenter);

	public void setValidator(IValidator validator) {
		this.validator = validator;
	}

	@Override
	protected void okPressed() {
		if (handleValidation()) {
			super.okPressed();
		}
	}

	protected boolean handleValidation() {
		boolean result = true;
		if (validator != null) {
			try {
				result = validator.validate();
				setErrorMessage(null);
				setWarningMessage(null);
			} catch (ValidationException e) {
				result = false;
				if (e instanceof ValidationErrorException) {
					setErrorMessage(e.getMessage());
				} else if (e instanceof ValidationWarningException) {
					setWarningMessage(e.getMessage());
				}
			}
		}
		return result;
	}
}