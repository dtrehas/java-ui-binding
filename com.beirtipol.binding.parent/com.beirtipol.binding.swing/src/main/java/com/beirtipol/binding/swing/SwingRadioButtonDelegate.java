package com.beirtipol.binding.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import com.beirtipol.binding.core.binders.widget.ICheckBoxBinder;
import com.beirtipol.binding.core.delegates.ICheckBoxDelegate;

public class SwingRadioButtonDelegate implements ICheckBoxDelegate {
	private JRadioButton button;

	public SwingRadioButtonDelegate(JRadioButton cb) {
		this.button = cb;
		if (cb == null) {
			throw new IllegalArgumentException("RadioButton cannot be null");
		}
	}

	@Override
	public void addPressedListener(final ICheckBoxBinder binder) {
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				binder.setCheckedIntoModel(button.isSelected());
			}
		});
	}

	@Override
	public void setChecked(final boolean checked) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				button.setSelected(checked);
			}
		});
	}

	@Override
	public void setEnabled(final boolean enabled) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				button.setEnabled(enabled);
			}
		});
	}

	@Override
	public void setVisible(final boolean visible) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				button.setVisible(visible);
			}
		});
	}

	@Override
	public void setBackground(final Color color) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				button.setBackground(color);
			}
		});
	}

	@Override
	public void setForeground(final Color color) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				button.setForeground(color);
			}
		});
	}

	@Override
	public void setToolTip(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				button.setToolTipText(text);
			}
		});
	}

	@Override
	public void setText(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				button.setText(text);
			}
		});
	}

	@Override
	public void free() {
		this.button = null;
	}
}
