package com.beirtipol.binding.swt;

import com.beirtipol.binding.core.binders.IPresenter;
import com.beirtipol.binding.core.delegates.IPresentableComponentDelegate;

public class SWTPresentableComponentDelegate<T extends IPresenter> extends SWTComponentDelegate implements IPresentableComponentDelegate<T> {
	private final SWTAbstractPresentableComposite<T> component;

	public SWTPresentableComponentDelegate(SWTAbstractPresentableComposite<T> component) {
		super(component);
		this.component = component;
	}

	@Override
	public void setPresenter(T presenter) {
		component.setPresenter(presenter);
	}

	public SWTAbstractPresentableComposite<T> getComponent() {
		return component;
	}

	@Override
	public void free() {
		component.dispose();
	}
}