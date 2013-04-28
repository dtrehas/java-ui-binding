package com.beirtipol.binding.swing;

import com.beirtipol.binding.core.binders.IPresenter;
import com.beirtipol.binding.core.delegates.IPresentableComponentDelegate;

public class SwingPresentableComponentDelegate<T extends IPresenter> extends SwingComponentDelegate implements IPresentableComponentDelegate<T> {
	private AbstractPresentableSwingPanel<T> component;

	public SwingPresentableComponentDelegate(AbstractPresentableSwingPanel<T> component) {
		super(component.getSwingComponent());
		this.component = component;
	}

	public AbstractPresentableSwingPanel<T> getComponent() {
		return component;
	}

	@Override
	public void setPresenter(T presenter) {
		component.setPresenter(presenter);
	}

	@Override
	public void free() {
		super.free();
		if (component != null) {
			component.dispose();
		}

		component = null;
	}
}