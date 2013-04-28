package com.beirtipol.binding.swt;

import org.eclipse.swt.widgets.Control;

import com.beirtipol.binding.core.delegates.IDelegate;

/**
 * NOTE: It is very important that you use the SWT thread when setting any
 * values on the SWT controls else you may get SWTException(Invalid Thread
 * Access). If you need to access the value of the control, you're doing
 * something wrong. The control is only supposed to be a view on the value
 * provided by the Binder. That is the reason that this Interface is PACKAGE
 * access only. It's still possible to expose the method, just please don't. @See
 * IBinder
 * 
 * @author O041484
 */
interface ISWTDelegate extends IDelegate {
	public Control getControl();
}