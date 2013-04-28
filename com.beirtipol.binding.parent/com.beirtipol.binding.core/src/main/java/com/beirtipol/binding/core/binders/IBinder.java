package com.beirtipol.binding.core.binders;

import com.beirtipol.binding.core.util.IFreeable;

/**
 * The IBinder implementation is the only way you should attempt to communicate
 * with the UI from the presentation layer. You should never implement any way
 * of 'querying' the UI as UI implementations run on a separate thread and
 * usually require you to be on the GUI thread to access them. Since you cannot
 * safely access widgets synchronously without deadlocking, don't attempt to do
 * this. IBinder should push to the IDelegate and IDelegate should notify/push
 * to the binder through direct access/events. IBasicBinder is the richer
 * interface, containing the IDelegate reference.
 * 
 * @see IBasicBinder
 * @author O041484
 */
public interface IBinder extends IFreeable {
	/**
	 * You should never attempt to update a delegate directly. This is the only
	 * recommended way to update the UI. Calling this will have the binder
	 * question itself for all properties of its bound delegate, such as
	 * visibility, enabled state, etc.
	 */
	void updateUI();
}