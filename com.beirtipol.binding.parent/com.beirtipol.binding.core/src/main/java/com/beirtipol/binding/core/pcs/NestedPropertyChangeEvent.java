package com.beirtipol.binding.core.pcs;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import com.beirtipol.binding.core.util.StringStack;

@SuppressWarnings("serial")
public class NestedPropertyChangeEvent extends PropertyChangeEvent {
	private final PropertyChangeEvent sourceEvent;

	public NestedPropertyChangeEvent(Object source, String propertyName, Object oldValue, Object newValue, PropertyChangeEvent sourceEvent) {
		super(source, propertyName, oldValue, newValue);
		this.sourceEvent = sourceEvent;
	}
	
	public NestedPropertyChangeEvent(PropertyChangeEvent event){
		this(event.getSource(), event.getPropertyName(), event.getOldValue(), event.getNewValue(), null);
	}

	public PropertyChangeEvent getSourceEvent() {
		return sourceEvent;
	}

	public PropertyChangeEvent getRootEvent() {
		PropertyChangeEvent rootEvent = this;
		while (rootEvent instanceof NestedPropertyChangeEvent) {
			PropertyChangeEvent innerSourceEvent = ((NestedPropertyChangeEvent) rootEvent).getSourceEvent();
			if (innerSourceEvent == null) {
				break;
			}
			rootEvent = innerSourceEvent;
		}
		return rootEvent;
	}

	public StringStack getPropertyPath() {
		List<String> path = new ArrayList<String>();
		path.add(getPropertyName());
		PropertyChangeEvent rootEvent = this;
		while (rootEvent instanceof NestedPropertyChangeEvent) {
			PropertyChangeEvent innerSourceEvent = ((NestedPropertyChangeEvent) rootEvent).getSourceEvent();
			if (innerSourceEvent == null) {
				break;
			}
			rootEvent = innerSourceEvent;
			path.add(rootEvent.getPropertyName());
		}

		return new StringStack(path);
	}
}