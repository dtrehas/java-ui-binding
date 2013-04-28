package com.beirtipol.binding.swing;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.beirtipol.binding.core.delegates.ILabelDelegate;

public class SwingLabelDelegate implements ILabelDelegate {
	private JLabel control;

	public SwingLabelDelegate(JLabel group) {
		this.control = group;
	}

	@Override
	public void setText(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				control.setText(text);
				control.updateUI();
			}
		});
	}

	@Override
	public void setVisible(final boolean visible) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				control.setVisible(visible);
			}
		});
	}

	@Override
	public void setForeground(final Color color) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				control.setForeground(color);
			}
		});
	}

	@Override
	public void setBackground(final Color color) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				control.setBackground(color);
			}
		});
	}

	@Override
	public void setToolTip(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				control.setToolTipText(text);
			}
		});
	}

	@Override
	public void free() {
		this.control = null;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				control.setEnabled(enabled);
			}
		});
	}
}