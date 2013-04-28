package com.beirtipol.binding.core.binders;

import com.beirtipol.binding.core.delegates.IDelegate;

/**
 * This class provides the linkage between the IBinder and the UI Delegate. The
 * 'getDelegate' method is provided only for usage within this package, but you
 * cannot designate package access for methods within interfaces in java.
 * 
 * @see IBinder
 * @author O041484
 * @param <Delegate>
 */
public interface IBasicBinder<Delegate extends IDelegate> extends IBinder {
	Delegate getDelegate();

	void setDelegate(Delegate updater);

	boolean isVisible();

	boolean isEnabled();

	String getToolTip();
}