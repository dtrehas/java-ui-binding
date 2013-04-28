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
 * @param <B>
 */
public class ListeningBinder<D extends IDelegate, B extends AbstractBinder<D>, T>
		extends AbstractBinder<D> implements PropertyChangeListener, IFreeable {
	private PropertyChangeSupport changeSupport;
	protected final IStaticObjectFormattedAccessor<T> accessor;
	private final B binder;

	public ListeningBinder(B binder, IStaticObjectFormattedAccessor<T> accessor) {
		this.binder = binder;
		this.accessor = accessor;
	}

	@Override
	public void updateUI() {
		binder.updateUI();
	}

	@Override
	protected void setupListeners() {
		binder.setupListeners();
	}

	public IStaticObjectFormattedAccessor<T> getAccessor() {
		return accessor;
	}

	public T getRealValue() {
		return accessor.getRealValue();
	}

	public B getBinder() {
		return binder;
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