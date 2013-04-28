package com.beirtipol.binding.core.pcs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.IdentityHashMap;

@SuppressWarnings("serial")
public class NestedPropertyChangeSupport extends PropertyChangeSupport {
	private final Object sourceBean;
	private IdentityHashMap<Object, PropertyChangeListener> fieldListenerMap = new IdentityHashMap<Object, PropertyChangeListener>();

	public NestedPropertyChangeSupport(Object sourceBean) {
		super(sourceBean);
		this.sourceBean = sourceBean;
	}

	@Override
	public void firePropertyChange(PropertyChangeEvent event) {
		unbindOldValue(event.getOldValue());
		bindNewValue(event.getNewValue(), event.getPropertyName());
		super.firePropertyChange(event);
	}

	private void bindNewValue(Object newValue, String propertyName) {
		if (newValue == null) {
			return;
		}
		PropertyChangeSupport changeSupport = getPropertyChangeSupport(newValue);
		if (changeSupport != null) {
			FieldChangeListener listener = new FieldChangeListener(propertyName);
			fieldListenerMap.put(newValue, listener);
			changeSupport.addPropertyChangeListener(listener);
		}
	}

	private void unbindOldValue(Object oldValue) {
		if (oldValue == null) {
			return;
		}
		PropertyChangeSupport changeSupport = getPropertyChangeSupport(oldValue);
		if (changeSupport != null) {
			changeSupport.removePropertyChangeListener(fieldListenerMap.get(oldValue));
			fieldListenerMap.remove(oldValue);
		}
	}

	private PropertyChangeSupport getPropertyChangeSupport(Object obj) {
		if (obj instanceof IBindable) {
			return ((IBindable) obj).changeSupport();
		} else {
			return null;
		}
	}

	private class FieldChangeListener implements PropertyChangeListener {
		private final String fieldName;

		public FieldChangeListener(String fieldName) {
			this.fieldName = fieldName;
		}

		@Override
		public void propertyChange(PropertyChangeEvent sourceEvent) {
			NestedPropertyChangeEvent nestedEvent = new NestedPropertyChangeEvent(sourceBean, fieldName, null, sourceBean, sourceEvent);
			NestedPropertyChangeSupport.super.firePropertyChange(nestedEvent);
		}
	}
}