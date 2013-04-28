package com.beirtipol.binding.swt.util;

import java.awt.Canvas;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SWTComponentHelper {
	/**
	 * Helper method for drawing SWT components on Swing successfully.
	 * 
	 * @param canvasParent
	 *            Required to update the UI correctly and layout the SWT
	 *            component.
	 * @param canvas
	 *            The canvas on which to create the SWT shell
	 * @param runnable
	 *            Override the drawSWTComponent method to create your SWT
	 *            component
	 * @see SWT_SwingBridge
	 */
	public static void drawSWTComponent(final JComponent canvasParent,
			final Canvas canvas, final SWT_SwingBridge runnable) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				final Shell shell = SWT_AWT.new_Shell(Display.getDefault(),
						canvas);
				shell.setLayout(new FillLayout());
				runnable.drawSWTComponent(shell);
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						canvasParent.updateUI();
					}
				});
			}
		});
	}
}
