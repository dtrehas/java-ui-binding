package com.beirtipol.binding.core.delegates;

import com.beirtipol.binding.core.binders.dialog.DirectoryDialogBinder;

/**
 * This is simply a tag interface to set the correct Binder type and provide
 * easier implementation.
 * 
 * @author O041484
 * 
 */
public interface IDirectoryDialogDelegate extends IFileBasedDialogDelegate<DirectoryDialogBinder> {
}
