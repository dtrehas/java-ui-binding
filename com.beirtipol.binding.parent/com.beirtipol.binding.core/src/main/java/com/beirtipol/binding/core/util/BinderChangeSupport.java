package com.beirtipol.binding.core.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Set;

import com.beirtipol.binding.core.binders.IBinder;
import com.beirtipol.binding.core.pcs.IStaticObjectFormattedAccessor;
import com.beirtipol.binding.core.pcs.StaticFormattedFieldAccessor;

public class BinderChangeSupport implements PropertyChangeListener {
	private PropertyChangeSupport changeSupport;
	private final IStaticObjectFormattedAccessor<?> accessor;
	private final IBinder binder;
	private final Set<StringStack> dependentEventPaths = new HashSet<StringStack>();

	public BinderChangeSupport(IBinder binder, IStaticObjectFormattedAccessor<?> accessor) {
		this.binder = binder;
		this.accessor = accessor;
	}

	public PropertyChangeSupport getChangeSupport() {
		return changeSupport;
	}

	public void setChangeSupport(PropertyChangeSupport changeSupport) {
		freeChangeSupport();
		this.changeSupport = changeSupport;
		changeSupport.addPropertyChangeListener(this);
	}

	private void freeChangeSupport() {
		if (changeSupport != null) {
			changeSupport.removePropertyChangeListener(this);
		}
	}

	public boolean addDependentEventPaths(StringStack e) {
		return dependentEventPaths.add(e);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		boolean update = false;
		if (accessor.matchesEvent(evt)) {
			update = true;
		} else {
			for (StringStack path : dependentEventPaths) {
				if (StaticFormattedFieldAccessor.matchesEvent(path, evt)) {
					update = true;
					break;
				}
			}
		}
		if (update) {
			binder.updateUI();
		}
	}
}