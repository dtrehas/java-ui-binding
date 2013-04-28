package com.beirtipol.binding.swt;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

import com.beirtipol.binding.core.binders.component.ITabItemBinder;
import com.beirtipol.binding.core.delegates.ITabItemDelegate;

public class SWTTabItemDelegate implements ITabItemDelegate {
	private CTabItem control;

	public SWTTabItemDelegate(CTabItem control) {
		this.control = control;
	}

	@Override
	public void addCloseListener(final ITabItemBinder abstractTabItemBinder) {
		control.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				abstractTabItemBinder.onClose();
			}
		});
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
}