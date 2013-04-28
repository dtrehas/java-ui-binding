package com.beirtipol.binding.core.pcs;

import java.beans.PropertyChangeEvent;

import com.beirtipol.binding.core.util.ItemBinder;

public interface IStaticObjectFormattedAccessor<T> {

	public T getRealValue();

	public void setRealValue(T value);

	public String getValue();

	public void setValue(String value);

	public boolean matchesEvent(PropertyChangeEvent event);

	public ItemBinder<T> getFormatter();

}