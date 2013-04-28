package com.beirtipol.binding.swing;

import java.awt.event.ActionListener;

import com.beirtipol.binding.core.delegates.IDelegate;

public abstract class DelegateActionListener implements ActionListener {
	private final IDelegate delegate;

	public DelegateActionListener(IDelegate delegate) {
		this.delegate = delegate;
	}

	public IDelegate getDelegate() {
		return delegate;
	}
}
