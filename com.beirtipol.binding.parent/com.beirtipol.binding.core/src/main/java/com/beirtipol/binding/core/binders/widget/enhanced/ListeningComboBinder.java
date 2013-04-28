package com.beirtipol.binding.core.binders.widget.enhanced;

import java.beans.PropertyChangeSupport;

import com.beirtipol.binding.core.binders.IListeningBinder;
import com.beirtipol.binding.core.binders.widget.AbstractComboBinder;
import com.beirtipol.binding.core.pcs.IStaticObjectFormattedAccessor;
import com.beirtipol.binding.core.util.BinderChangeSupport;
import com.beirtipol.binding.core.util.ItemBinder;
import com.beirtipol.binding.core.util.StringStack;

@SuppressWarnings("rawtypes")
public abstract class ListeningComboBinder extends AbstractComboBinder implements IListeningBinder {
	private final IStaticObjectFormattedAccessor accessor;
	private final BinderChangeSupport binderChangeSupport;

	public ListeningComboBinder(IStaticObjectFormattedAccessor accessor) {
		this.accessor = accessor;
		binderChangeSupport = new BinderChangeSupport(this, accessor);
	}

	@Override
	public Object getSelectedItem() {
		return accessor.getRealValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setSelectedItem(Object o) {
		accessor.setRealValue(o);
	}

	@Override
	public ItemBinder getItemBinder() {
		return accessor.getFormatter();
	}

	@Override
	public void setChangeSupport(PropertyChangeSupport changeSupport) {
		binderChangeSupport.setChangeSupport(changeSupport);
	}

	@Override
	public void addDependentEventPath(StringStack path) {
		binderChangeSupport.addDependentEventPaths(path);
	}
}