package com.beirtipol.binding.swing;

import javax.swing.JComponent;

import com.beirtipol.binding.core.delegates.IComponentDelegate;

public class SwingComponentDelegate implements IComponentDelegate {
	private JComponent component;

	public SwingComponentDelegate(JComponent component) {
		this.component = component;
	}

	@Override
	public void setEnabled(boolean enabled) {
		component.setEnabled(enabled);
	}

	@Override
	public void setVisible(boolean visible) {
		component.setVisible(visible);
	}

	@Override
	public void free() {
		component = null;
	}
}