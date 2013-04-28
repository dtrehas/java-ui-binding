package com.beirtipol.binding.swing;

import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import com.beirtipol.binding.core.delegates.IDialogDelegate;

public class SwingOldDialogDelegate implements IDialogDelegate {
	private final JDialog dialog;
	protected JComponent parentComponent;

	public SwingOldDialogDelegate(JDialog dialog) {
		this(dialog, null);
	}

	/**
	 * @param dialog
	 * @param parentComponent
	 *            - The dialog will be rendered next to its parent. If the
	 *            parent is null the dialog will be rendered in the top left
	 *            corner of the left hand monitor.
	 */
	public SwingOldDialogDelegate(JDialog dialog, JComponent parentComponent) {
		this.dialog = dialog;
		this.parentComponent = parentComponent;
	}

	@Override
	public void openDialog() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (parentComponent != null) {
					Point parentLocation = parentComponent.getLocationOnScreen();
					parentLocation.x += parentComponent.getWidth();
					dialog.setLocation(parentLocation);
				}

				dialog.setVisible(true);

				dialog.setSize(dialog.getPreferredSize());
			}
		});
	}

	@Override
	public void free() {
		dialog.dispose();
		parentComponent = null;
	}
}