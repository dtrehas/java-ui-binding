package com.beirtipol.binding.core.binders;

public interface IContentAssistProvider {
	char[] getActivationKeys();

	/**
	 * @param proposal
	 *            - text typed into field
	 * @param position
	 *            - caret pop within the proposal text
	 * @return
	 */
	String[] getSuggestions(String proposal, int position);
}
