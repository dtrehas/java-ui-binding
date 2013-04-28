package com.beirtipol.binding.swing;

import java.awt.Color;

import javax.swing.SwingUtilities;

import com.beirtipol.binding.core.delegates.ILabelDelegate;

public class SwingGroupDelegate implements ILabelDelegate {
	private JGroupPanel group;

	public SwingGroupDelegate(JGroupPanel group) {
		this.group = group;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				group.setEnabled(enabled);
			}
		});
	}

	@Override
	public void setText(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				group.setTitle(text);
				group.updateUI();
			}
		});
	}

	@Override
	public void setVisible(final boolean visible) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				group.setVisible(visible);
			}
		});
	}

	@Override
	public void setForeground(final Color color) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				group.setForeground(color);
			}
		});
	}

	@Override
	public void setBackground(final Color color) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				group.setBackground(color);
			}
		});
	}

	@Override
	public void setToolTip(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				group.setToolTipText(text);
			}
		});
	}

	@Override
	public void free() {
		this.group = null;
	}

}
