package com.beirtipol.binding.core.delegates;

/**
 * A simple wrapper interface for dialogs that simply need to be opened with no
 * action taken afterwards.
 * 
 * @author O041484
 * 
 */
public interface IDialogDelegate extends IDelegate {
	void openDialog();
}