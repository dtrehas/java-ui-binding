package com.beirtipol.binding.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.SwingUtilities;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.binders.widget.IButtonBinder;
import com.beirtipol.binding.core.delegates.IButtonDelegate;
import com.beirtipol.binding.core.delegates.IDelegate;

public class SwingButtonDelegate implements IButtonDelegate {
	private AbstractButton button;
	private DelegateActionListener listener;

	public SwingButtonDelegate(AbstractButton button) {
		if (button == null) {
			throw new IllegalArgumentException("Button cannot be null");
		}
		this.button = button;

		button.setBackground(Color.WHITE);
	}

	public void addPressedListener(final IButtonBinder binder) {
		// use DelegateActionListener to record which delegate has been assigned
		// to the binder, it
		// is used to avoid adding multiple listeners to the same object
		IButtonDelegate delegate = binder.getDelegate();
		ActionListener[] actionListeners = button.getActionListeners();
		for (ActionListener listener : actionListeners) {
			if (listener instanceof DelegateActionListener) {
				IDelegate buttonDelegate = ((DelegateActionListener) listener).getDelegate();
				if (buttonDelegate instanceof IButtonDelegate) {
					if (buttonDelegate.equals(delegate)) {
						return;
					}
				}
			}
		}

		listener = new DelegateActionListener(delegate) {
			@Override
			public void actionPerformed(ActionEvent e) {
				button.getModel().getActionCommand();
				binder.pressed();
			}
		};
		button.addActionListener(listener);
	}

	public boolean isEnabled() {
		return button.isEnabled();
	}

	public void setEnabled(final boolean enabled) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				button.setEnabled(enabled);
			}
		});
	}

	@Override
	public void setText(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				button.setText(text);
			}
		});
	}

	@Override
	public void setToolTip(String text) {
		button.setToolTipText(text);
	}

	public void setVisible(final boolean visible) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				button.setVisible(visible);
			}
		});
	}

	public void setBinder(IBasicBinder<? extends IDelegate> binder) {
	}

	@Override
	public void free() {
		if (button != null) {
			button.removeActionListener(listener);
			button.removeAll();
		}

		button = null;
		listener = null;
	}

	@Override
	public void setBackground(Color color) {
		button.setBackground(color);
	}

	@Override
	public void setForeground(Color color) {
		button.setForeground(color);
	}

	@Override
	public void setImagePath(String path) {
		// TODO Auto-generated method stub
	}
}
