package com.beirtipol.binding.core.pcs;

import com.beirtipol.binding.core.util.ItemBinder;
import com.beirtipol.binding.core.util.StringStack;

public class FormattedFieldAccessor<T> {
	private final ItemBinder<T> formatter;
	private final FieldAccessor<T> accessor;

	public FormattedFieldAccessor(ItemBinder<T> formatter) {
		this.formatter = formatter;
		this.accessor = new FieldAccessor<T>();
	}

	public ItemBinder<T> getFormatter() {
		return formatter;
	}

	public T getRealValue(Object bom, StringStack path) {
		return accessor.getRealValue(bom, path);
	}

	public String getValue(Object bom, StringStack path) {
		T realValue = getRealValue(bom, path);
		if (realValue == null) {
			return "";
		}
		return formatter.convert(realValue);
	}

	public void setRealValue(Object bom, StringStack path, T value) {
		accessor.setRealValue(bom, path, value);
	}

	public void setValue(Object bom, StringStack path, String value) {
		setRealValue(bom, path, formatter.convert(value));
	}
}