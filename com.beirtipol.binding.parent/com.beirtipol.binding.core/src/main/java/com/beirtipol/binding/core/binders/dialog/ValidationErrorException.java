package com.beirtipol.binding.core.binders.dialog;

@SuppressWarnings("serial")
public class ValidationErrorException extends ValidationException {
	public ValidationErrorException(String string) {
		super(string);
	}
}