package com.beirtipol.binding.swing;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.binders.widget.ITextAreaBinder;
import com.beirtipol.binding.core.delegates.IDelegate;
import com.beirtipol.binding.core.delegates.ITextAreaDelegate;

public class SwingTextAreaDelegate implements ITextAreaDelegate {
	private JTextArea text;

	public SwingTextAreaDelegate(final JTextArea text) {
		if (text == null) {
			throw new IllegalArgumentException("Text cannot be null");
		}
		this.text = text;
	}

	@Override
	public void addFocusListener(final ITextAreaBinder binder) {
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				performListenerAction(binder);
			}
		});
	}

	@Override
	public void addModifyListener(final ITextAreaBinder binder) {
	}

	@Override
	public void addTraverseListener(ITextAreaBinder binder) {
		addModifyListener(binder);
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
		return text.isEnabled();
	}

	@Override
	public void setBackground(final Color color) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				text.setBackground(color);
			}
		});
	}

	@Override
	public void setEnabled(final boolean enabled) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				text.setEnabled(enabled);
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
			}
		});
	}

	public void setBinder(IBasicBinder<? extends IDelegate> binder) {
	}

	protected void performListenerAction(final ITextAreaBinder binder) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				String uiText = getText();
				String modelText = binder.getTextFromModel();
				if (!uiText.equals(modelText)) {
					binder.setTextIntoModel(uiText);
				}
				binder.updateUI();
			}
		});
	}

	public String getToolTip() {
		// TODO
		return "hello";
	}

	@Override
	public void setToolTip(String tip) {
		text.setToolTipText(tip);
	}

	@Override
	public void setVisible(final boolean visible) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				text.setVisible(visible);
			}
		});
	}

	@Override
	public void free() {
		text = null;
	}
}
