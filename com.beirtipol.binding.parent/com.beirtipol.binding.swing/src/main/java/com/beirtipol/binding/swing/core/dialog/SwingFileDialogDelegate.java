package com.beirtipol.binding.swing.core.dialog;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import com.beirtipol.binding.core.binders.dialog.FileDialogBinder;
import com.beirtipol.binding.core.delegates.IFileDialogDelegate;

public class SwingFileDialogDelegate implements IFileDialogDelegate {

	private JFileChooser dialog;
	private FileDialogBinder binder;
	private JPanel parent;

	public SwingFileDialogDelegate(JFileChooser dialog, JPanel parent) {
		this.dialog = dialog;
		this.parent = parent;
	}

	@Override
	public void setFilters(Map<String, String> filters) {
		FileFilter[] existingFilters = dialog.getChoosableFileFilters();
		for (FileFilter existingFilter : existingFilters) {
			dialog.removeChoosableFileFilter(existingFilter);
		}
		for (final Entry<String, String> entry : filters.entrySet()) {
			if ("*.*".equals(entry.getValue())) {
				dialog.addChoosableFileFilter(dialog.getAcceptAllFileFilter());
			} else {
				dialog.addChoosableFileFilter(new FileFilter() {
					@Override
					public String getDescription() {
						return entry.getKey();
					}

					@Override
					public boolean accept(File f) {
						// Not happy with this at all. But seems the easiest way
						// to make it compatible with SWT's version, which seems
						// like a more sensible standard.
						return f.isDirectory() || f.getName().endsWith(entry.getValue().substring(1));
					}
				});
			}
		}

	}

	@Override
	public void setFilterPath(String filterPath) {
		dialog.setCurrentDirectory(new File(filterPath));
	}

	@Override
	public void setFileName(String fileName) {
		dialog.setSelectedFile(new File(fileName));
	}

	@Override
	public void openDialog() {
		int result = dialog.showDialog(parent, "Select");
		File file = dialog.getSelectedFile();
		if (binder != null && file != null && result == JFileChooser.APPROVE_OPTION) {
			binder.onClose(file);
		}
	}

	@Override
	public void setBinder(FileDialogBinder binder) {
		this.binder = binder;
	}

	@Override
	public void free() {
		this.dialog = null;
		this.binder = null;
		this.parent = null;
	}
}