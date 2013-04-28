package com.beirtipol.binding.swt.util;

import org.eclipse.swt.widgets.Shell;

public abstract class SWT_SwingBridge {
	/**
	 * @param shell
	 *            The created shell to use as a parent for your SWT components.
	 */
	public abstract void drawSWTComponent(Shell shell);
}
