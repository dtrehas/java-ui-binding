package com.beirtipol.binding.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import com.beirtipol.binding.core.binders.widget.ITextBinder;
import com.beirtipol.binding.core.delegates.ITextDelegate;

public class SwingTextDelegate implements ITextDelegate {
	protected JTextComponent text;

	private FocusAdapter focusAdapter;
	private ActionListener actionListener;

	public SwingTextDelegate(final JTextComponent text) {
		if (text == null) {
			throw new IllegalArgumentException("Text cannot be null");
		}
		this.text = text;
	}

	@Override
	public void addFocusListener(final ITextBinder binder) {
		focusAdapter = new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				text.selectAll();
			}

			@Override
			public void focusLost(FocusEvent e) {
				performListenerAction(binder);
			}
		};

		text.addFocusListener(focusAdapter);
	}

	@Override
	public void addModifyListener(final ITextBinder binder) {
	}

	@Override
	public void addTraverseListener(final ITextBinder binder) {
		/**
		 * TextAreas don't have action listeners, TextFields do
		 */
		if (text instanceof JTextField) {
			JTextField jTextField = (JTextField) text;

			actionListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					binder.setTextIntoModel(getText());
				}
			};

			jTextField.addActionListener(actionListener);
		}
	}

	protected Color getBackground() {
		return text.getBackground();
	}

	protected Color getForeground() {
		return text.getForeground();
	}

	protected String getText() {
		return text.getText();
	}

	protected boolean isEnabled() {
		return text.isEditable();
	}

	@Override
	public void setBackground(final Color color) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (text != null)
					text.setBackground(color);
			}
		});
	}

	@Override
	public void setEnabled(final boolean enabled) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Note: we are using setEditable() not setEnabled() because
				// with the later it's not
				// possible to change the text colour to anything other than
				// grey.
				if (text != null)
					text.setEditable(enabled);
			}
		});
	}

	@Override
	public void setForeground(final Color color) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				text.setForeground(color);
			}
		});
	}

	@Override
	public void setText(final String string) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				text.setText(string);
				text.setCaretPosition(0);
			}
		});
	}

	protected void performListenerAction(final ITextBinder binder) {
		binder.setTextIntoModel(getText());
	}

	@Override
	public void setToolTip(String tip) {
		text.setToolTipText(tip);
	}

	@Override
	protected void finalize() throws Throwable {
		// widgetCount--;
		// System.err.println("DISPOSE TEXT WIDGET (count=" + referenced + ")");
	}

	@Override
	public void setVisible(final boolean visible) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (text != null)
					text.setVisible(visible);
			}
		});
	}

	@Override
	public void free() {
		if (text != null && focusAdapter != null) {
			text.removeFocusListener(focusAdapter);
			focusAdapter = null;
		}

		if (actionListener != null) {
			if (text != null && text instanceof JTextField) {
				JTextField jTextField = (JTextField) text;
				jTextField.removeActionListener(actionListener);
			}
		}

		if (text != null) {
			text.removeAll();
			text = null;
		}
	}
}
