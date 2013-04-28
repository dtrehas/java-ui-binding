package com.beirtipol.binding.swing;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.binders.widget.ICheckBoxBinder;
import com.beirtipol.binding.core.delegates.ICheckBoxDelegate;
import com.beirtipol.binding.core.delegates.IDelegate;

public class SwingCheckBoxDelegate implements ICheckBoxDelegate {
	private JCheckBox cb;
	private List<ActionListener> actionListeners = new ArrayList<ActionListener>();

	public SwingCheckBoxDelegate(JCheckBox cb) {
		this.cb = cb;
		if (cb == null) {
			throw new IllegalArgumentException("CheckBox cannot be null");
		}
		if (cb.getParent() != null) {
			cb.setBackground(cb.getParent().getBackground());
		} else {
			cb.setBackground(Color.WHITE);
		}

		cb.setMargin(new Insets(0, -2, 0, 0));
		cb.setIconTextGap(10);
	}

	public void addPressedListener(final ICheckBoxBinder binder) {
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				binder.setCheckedIntoModel(cb.isSelected());
			}
		};

		cb.addActionListener(listener);
		actionListeners.add(listener);
	}

	public void setChecked(final boolean checked) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				cb.setSelected(checked);
			}
		});
	}

	public void setText(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				cb.setText(text);
			}
		});
	}

	public void setBinder(IBasicBinder<? extends IDelegate> binder) {
	}

	public void setEnabled(final boolean enabled) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				cb.setEnabled(enabled);
			}
		});
	}

	public void setVisible(final boolean visible) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				cb.setVisible(visible);
			}
		});
	}

	@Override
	public void setToolTip(String tip) {
		cb.setToolTipText(tip);
	}

	@Override
	public void free() {
		if (cb != null) {
			for (ActionListener l : actionListeners) {
				cb.removeActionListener(l);
			}

			actionListeners.clear();
			actionListeners = null;
			cb.removeAll();
			cb = null;
		}
	}

	@Override
	public void setBackground(Color color) {
		cb.setBackground(color);
	}

	@Override
	public void setForeground(Color color) {
		cb.setForeground(color);
	}
}
