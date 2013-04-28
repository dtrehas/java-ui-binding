package com.beirtipol.binding.core.binders.dialog;

public class ValidatorChain implements IValidator {
	private final IValidator[] validators;

	public ValidatorChain(IValidator... validators) {
		this.validators = validators;
	}

	@Override
	public boolean validate() throws ValidationException {
		boolean result = true;
		for (IValidator validator : validators) {
			result &= validator.validate();
		}
		return result;
	}

}
