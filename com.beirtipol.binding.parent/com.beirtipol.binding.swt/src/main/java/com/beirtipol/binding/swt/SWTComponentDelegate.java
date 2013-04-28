package com.beirtipol.binding.swt;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.beirtipol.binding.core.delegates.IComponentDelegate;

public class SWTComponentDelegate implements IComponentDelegate {
	private Composite component;

	public SWTComponentDelegate(Composite component) {
		this.component = component;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (component != null && !component.isDisposed()) {
					component.setEnabled(enabled);
				}
			}
		});
	}

	@Override
	public void setVisible(final boolean visible) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (component == null || component.isDisposed()) {
					return;
				}

				if (component.getParent().getLayout() instanceof GridLayout) {
					// Using exclude so that the composite does not take up any
					// space on the parent
					// when it is invisible.
					GridData excludeLayoutData = null;

					Object layoutData = component.getLayoutData();
					if (layoutData instanceof GridData) {
						excludeLayoutData = ((GridData) layoutData);
					} else {
						excludeLayoutData = new GridData();
					}

					excludeLayoutData.exclude = !visible;
					component.setLayoutData(excludeLayoutData);
				}
				component.setVisible(visible);
			}
		});
	}

	@Override
	public void free() {
		if (!component.isDisposed()) {
			component.dispose();
		}
		component = null;
	}
}