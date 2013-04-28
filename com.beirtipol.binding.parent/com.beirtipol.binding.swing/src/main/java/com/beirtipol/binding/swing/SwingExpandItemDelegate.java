package com.beirtipol.binding.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXTaskPane;

import com.beirtipol.binding.core.binders.component.IExpandItemBinder;
import com.beirtipol.binding.core.delegates.IExpandItemDelegate;

public class SwingExpandItemDelegate implements IExpandItemDelegate {
	private JXTaskPane control;

	public SwingExpandItemDelegate(JXTaskPane control) {
		this.control = control;
	}

	@Override
	public void addExpandListener(final IExpandItemBinder binder) {
		control.addPropertyChangeListener("collapsed", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				binder.setExpandedIntoModel(!control.isCollapsed());
			}
		});
	}

	@Override
	public void setExpanded(final boolean expanded) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				control.setCollapsed(!expanded);
			}
		});
	}

	public JXTaskPane getControl() {
		return control;
	}

	@Override
	public void setTitle(final String title) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				control.setTitle(title);
				control.setToolTipText(title);
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
	public void free() {
		this.control = null;
	}

	@Override
	public void setToolTipText(String text) {
		control.setToolTipText(text);
	}

	@Override
	public void setSelected(final boolean selected) {
	}
}