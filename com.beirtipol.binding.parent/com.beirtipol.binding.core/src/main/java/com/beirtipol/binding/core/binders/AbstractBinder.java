package com.beirtipol.binding.core.binders;

import org.apache.commons.lang.ObjectUtils;

import com.beirtipol.binding.core.delegates.IDelegate;

/**
 * Basic implementation of a Binder, providing no support for listening to a
 * domain object. It is important to call updateGUI() whenever the represented
 * data changes.
 * 
 * @author O041484
 * @param <Delegate>
 */
public abstract class AbstractBinder<Delegate extends IDelegate> implements
		IBasicBinder<Delegate> {
	protected Delegate delegate;

	protected IBooleanModel visibleModel;
	protected IBooleanModel enabledModel;
	protected IToolTipModel toolTipModel;

	@Override
	public Delegate getDelegate() {
		return delegate;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		if (ObjectUtils.equals(this.delegate, delegate)) {
			return;
		}
		if (this.delegate != null) {
			this.delegate.free();
		}

		this.delegate = delegate;
		setupListeners();
	}

	public IBooleanModel getVisibleModel() {
		return visibleModel;
	}

	public void setVisibleModel(IBooleanModel visibleModel) {
		this.visibleModel = visibleModel;
	}

	public IBooleanModel getEnabledModel() {
		return enabledModel;
	}

	public void setEnabledModel(IBooleanModel enabledModel) {
		this.enabledModel = enabledModel;
	}

	public IToolTipModel getToolTipModel() {
		return toolTipModel;
	}

	public void setToolTipModel(IToolTipModel toolTipModel) {
		this.toolTipModel = toolTipModel;
	}

	/**
	 * It is the responsibility of the subclass to set up any listening required
	 * on the delegate.
	 */
	abstract protected void setupListeners();

	/**
	 * Default implementation is always enabled.
	 */
	@Override
	public boolean isEnabled() {
		if (enabledModel != null) {
			return enabledModel.isTrue();
		}
		return true;
	}

	/**
	 * Default implementation is always visible.
	 */
	@Override
	public boolean isVisible() {
		if (visibleModel != null) {
			return visibleModel.isTrue();
		}
		return true;
	}

	/**
	 * Default implementation is blank.
	 */
	@Override
	public String getToolTip() {
		if (toolTipModel != null) {
			return toolTipModel.getToolTip();
		}
		return null;
	}

	/**
	 * 
	 */
	@Override
	public void free() {
		if (delegate != null) {
			delegate.free();
			delegate = null;
		}
	}
}