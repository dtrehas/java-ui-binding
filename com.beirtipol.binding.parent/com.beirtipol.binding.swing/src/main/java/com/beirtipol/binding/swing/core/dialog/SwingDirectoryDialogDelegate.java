package com.beirtipol.binding.swing.core.dialog;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import com.beirtipol.binding.core.binders.dialog.DirectoryDialogBinder;
import com.beirtipol.binding.core.delegates.IDirectoryDialogDelegate;

public class SwingDirectoryDialogDelegate implements IDirectoryDialogDelegate {

	private JFileChooser dialog;
	private DirectoryDialogBinder binder;
	private JPanel parent;

	public SwingDirectoryDialogDelegate(JFileChooser dialog, JPanel parent) {
		this.dialog = dialog;
		this.parent = parent;
		this.dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}

	@Override
	public void setFilterPath(String filterPath) {
		dialog.setCurrentDirectory(new File(filterPath));
	}

	@Override
	public void openDialog() {
		int result = dialog.showDialog(parent, "Select");
		File file = dialog.getSelectedFile();
		if (binder != null && file != null
				&& result == JFileChooser.APPROVE_OPTION) {
			binder.onClose(file);
		}
	}

	@Override
	public void setBinder(DirectoryDialogBinder binder) {
		this.binder = binder;
	}

	@Override
	public void free() {
		this.dialog = null;
		this.binder = null;
		this.parent = null;
	}
}