package com.beirtipol.binding.core.pcs;

import java.beans.PropertyChangeEvent;

import com.beirtipol.binding.core.util.IFreeable;
import com.beirtipol.binding.core.util.ItemBinder;
import com.beirtipol.binding.core.util.StringStack;

public class StaticObjectFormattedFieldAccessor<T> implements IFreeable,
		IStaticObjectFormattedAccessor<T> {
	private final StaticFormattedFieldAccessor<T> accessor;
	private Object bom;

	public StaticObjectFormattedFieldAccessor(Object bom,
			ItemBinder<T> formatter, StringStack path) {
		accessor = new StaticFormattedFieldAccessor<T>(formatter, path);
		this.bom = bom;
	}

	public StaticObjectFormattedFieldAccessor(Object bom,
			ItemBinder<T> formatter, String... path) {
		accessor = new StaticFormattedFieldAccessor<T>(formatter,
				new StringStack(path));
		this.bom = bom;
	}

	@Override
	public ItemBinder<T> getFormatter() {
		return accessor.getFormatter();
	}

	@Override
	public T getRealValue() {
		return accessor.getRealValue(bom);
	}

	@Override
	public void setRealValue(T value) {
		accessor.setRealValue(bom, value);
	}

	@Override
	public String getValue() {
		return accessor.getValue(bom);
	}

	@Override
	public void setValue(String value) {
		accessor.setValue(bom, value);
	}

	@Override
	public boolean matchesEvent(PropertyChangeEvent event) {
		return bom == event.getSource() && accessor.matchesEvent(event);
	}

	@Override
	public void free() {
		bom = null;
	}
}