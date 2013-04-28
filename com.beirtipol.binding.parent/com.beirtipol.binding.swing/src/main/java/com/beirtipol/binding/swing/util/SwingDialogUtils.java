package com.beirtipol.binding.swing.util;

import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

public class SwingDialogUtils {
	public static void openDialogNextToParent(final JComponent parentComponent,
			final JDialog dialog) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (parentComponent != null) {
					Point parentLocation = parentComponent
							.getLocationOnScreen();
					parentLocation.x += parentComponent.getWidth();
					dialog.setLocation(parentLocation);
				}
				dialog.setVisible(true);
				dialog.setSize(dialog.getPreferredSize());
			}
		});
	}
}
