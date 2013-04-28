package com.beirtipol.binding.swt;

import java.awt.Color;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.beirtipol.binding.core.delegates.ILabelDelegate;
import com.beirtipol.binding.swt.util.ColorConverter;

public class SWTLabelDelegate implements ILabelDelegate {
	private Label label;

	public SWTLabelDelegate(Label label) {
		this.label = label;
	}

	@Override
	public void setBackground(final Color color) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				label.setBackground(ColorConverter.convert(color));
			}
		});
	}

	@Override
	public void setForeground(final Color color) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				label.setForeground(ColorConverter.convert(color));
			}
		});
	}

	@Override
	public void setEnabled(final boolean enabled) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				label.setEnabled(enabled);
			}
		});
	}

	@Override
	public void setVisible(final boolean visible) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				label.setVisible(visible);
			}
		});
	}

	@Override
	public void setToolTip(final String text) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				label.setToolTipText(text);
			}
		});
	}

	@Override
	public void free() {
		this.label = null;
	}

	@Override
	public void setText(final String text) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				label.setText(text);
			}
		});
	}

}
