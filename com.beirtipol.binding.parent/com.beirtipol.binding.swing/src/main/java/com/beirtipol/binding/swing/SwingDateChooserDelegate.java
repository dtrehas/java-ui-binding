package com.beirtipol.binding.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.beirtipol.binding.core.binders.widget.ITextBinder;
import com.beirtipol.binding.swing.core.calendar.JDateChooser;

public class SwingDateChooserDelegate extends SwingTextDelegate {
	private JDateChooser dateChooser;
	private PropertyChangeListener propChangeListener;

	public SwingDateChooserDelegate(final JDateChooser dateChooser) {
		super((JTextField) dateChooser.getDateEditor().getUiComponent());
		this.dateChooser = dateChooser;
	}

	/**
	 * Override to add a "date" property change listener. Of course this is
	 * "crap" as we are casting dateChooser.getDateEditor() i.e. what a code
	 * smell. Issue is that we have an ITextBinder!
	 */
	@Override
	public void addFocusListener(final ITextBinder binder) {
		super.addFocusListener(binder);

		propChangeListener = new PropertyChangeListener() {
			/**
             *
             */
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				binder.setTextIntoModel(((JTextField) dateChooser.getDateEditor()).getText());
			}
		};

		dateChooser.getDateEditor().addPropertyChangeListener("date", propChangeListener);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				dateChooser.setEnabled(enabled);
			}
		});
	}

	@Override
	public void setVisible(final boolean visible) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				dateChooser.setVisible(visible);
			}
		});
	}

	@Override
	public void setToolTip(final String tip) {
		// Need to handle null here as the DateChooser doesn't like it
		if (tip != null) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					text.setToolTipText(tip);
				}
			});
		}
	}

	@Override
	public void free() {
		if (dateChooser != null && dateChooser.getDateEditor() != null && propChangeListener != null) {
			dateChooser.getDateEditor().removePropertyChangeListener("date", propChangeListener);
		}

		super.free();
		dateChooser = null;
	}
}