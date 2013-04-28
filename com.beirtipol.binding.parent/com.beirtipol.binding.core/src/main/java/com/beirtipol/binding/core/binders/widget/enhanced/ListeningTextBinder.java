package com.beirtipol.binding.core.binders.widget.enhanced;

import java.beans.PropertyChangeSupport;

import com.beirtipol.binding.core.binders.IListeningBinder;
import com.beirtipol.binding.core.binders.widget.AbstractTextBinder;
import com.beirtipol.binding.core.pcs.IStaticObjectFormattedAccessor;
import com.beirtipol.binding.core.util.BinderChangeSupport;
import com.beirtipol.binding.core.util.IFreeable;
import com.beirtipol.binding.core.util.StringStack;

public class ListeningTextBinder<T> extends AbstractTextBinder implements
		IFreeable, IListeningBinder {
	private final IStaticObjectFormattedAccessor<T> accessor;
	private final BinderChangeSupport binderChangeSupport;

	public ListeningTextBinder(IStaticObjectFormattedAccessor<T> accessor) {
		this.accessor = accessor;
		binderChangeSupport = new BinderChangeSupport(this, accessor);
	}

	@Override
	public String getTextFromModel() {
		return accessor.getValue();
	}

	@Override
	public void setTextIntoModel(String text) {
		try {
			accessor.setValue(text);
		} catch (Exception e) {
			handleException(e);
		}
	}

	public IStaticObjectFormattedAccessor<T> getAccessor() {
		return accessor;
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