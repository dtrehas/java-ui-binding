package com.beirtipol.binding.core.pcs;

import java.beans.PropertyChangeEvent;

import com.beirtipol.binding.core.util.ItemBinder;
import com.beirtipol.binding.core.util.StringStack;

public class StaticFormattedFieldAccessor<T> {
	private final FormattedFieldAccessor<T> fieldAccessor;
	private final StringStack path;

	public StaticFormattedFieldAccessor(ItemBinder<T> formatter, StringStack path) {
		this.path = path;
		fieldAccessor = new FormattedFieldAccessor<T>(formatter);
	}

	public ItemBinder<T> getFormatter() {
		return fieldAccessor.getFormatter();
	}

	public StringStack getPath() {
		return path;
	}

	public boolean matchesEvent(PropertyChangeEvent event) {
		return matchesEvent(path, event);
	}

	public static boolean matchesEvent(StringStack path, PropertyChangeEvent event) {
		if (event instanceof NestedPropertyChangeEvent) {
			return path.equals(((NestedPropertyChangeEvent) event).getPropertyPath());
		} else {
			return path.toString().equals(event.getPropertyName());
		}
	}

	public T getRealValue(Object bom) {
		return fieldAccessor.getRealValue(bom, path);
	}

	public String getValue(Object bom) {
		return fieldAccessor.getValue(bom, path);
	}

	public void setRealValue(Object bom, T value) {
		fieldAccessor.setRealValue(bom, path, value);
	}

	public void setValue(Object bom, String value) {
		fieldAccessor.setValue(bom, path, value);
	}
}