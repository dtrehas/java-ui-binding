package com.beirtipol.binding.core.binders;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.beirtipol.binding.core.delegates.IDelegate;
import com.beirtipol.binding.core.pcs.IStaticObjectFormattedAccessor;
import com.beirtipol.binding.core.util.IFreeable;

/**
 * Binders will not do any listening on their own. This responsibility is
 * delegated to the presentation model to prevent binders firing multiple times
 * during updates, such as business logic. The PropertyChangeSupport field
 * should be set and held by the presentation model in order that the presenter
 * can fire change when necessary.
 * 
 * @author O041484
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractListeningBinder<T extends IDelegate> extends
		AbstractBinder<T> implements PropertyChangeListener, IFreeable {
	private PropertyChangeSupport changeSupport;
	protected final IStaticObjectFormattedAccessor accessor;

	public AbstractListeningBinder(IStaticObjectFormattedAccessor accessor) {
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

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (accessor.matchesEvent(evt)) {
			updateUI();
		}
	}

	@Override
	public void free() {
		super.free();
		freeChangeSupport();
	}
}