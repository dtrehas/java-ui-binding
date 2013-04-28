package com.beirtipol.binding.core.binders;

import java.beans.PropertyChangeSupport;

import com.beirtipol.binding.core.util.StringStack;

public interface IListeningBinder {
	void setChangeSupport(PropertyChangeSupport changeSupport);

	void addDependentEventPath(StringStack path);
}
