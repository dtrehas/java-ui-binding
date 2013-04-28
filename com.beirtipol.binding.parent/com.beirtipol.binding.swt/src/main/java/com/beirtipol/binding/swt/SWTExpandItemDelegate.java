package com.beirtipol.binding.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Listener;

import com.beirtipol.binding.core.binders.component.IExpandItemBinder;
import com.beirtipol.binding.core.delegates.IExpandItemDelegate;

public class SWTExpandItemDelegate implements IExpandItemDelegate {
	private ExpandItem control;

	public SWTExpandItemDelegate(ExpandItem control) {
		this.control = control;
	}

	@Override
	public void addExpandListener(final IExpandItemBinder binder) {
		control.addListener(SWT.Expand, new Listener() {
			@Override
			public void handleEvent(Event event) {
				binder.setExpandedIntoModel(control.getExpanded());
			}
		});
	}

	@Override
	public void setExpanded(boolean expanded) {
		control.setExpanded(expanded);
	}

	@Override
	public void setTitle(String title) {
		control.setText(title);
	}

	@Override
	public void free() {
		if (control != null && !control.isDisposed()) {
			control.getControl().dispose();
			control.setControl(null);
			control.dispose();
		}
		control = null;
	}

	@Override
	public void setToolTipText(String text) {
	}

	@Override
	public void setSelected(boolean selected) {
	}

	@Override
	public void setVisible(boolean visible) {
		// TODO
	}
}