package com.beirtipol.binding.swing;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

public class SwingComponentHelper {
	public static void linkEnabled(JComponent watcher,
			final JComponent... actor) {
		watcher.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("enabled")) {
					Boolean newValue = (Boolean) evt.getNewValue();
					if (newValue) {
						for (JComponent j : actor) {
							j.setEnabled(true);
						}
					} else {
						for (JComponent j : actor) {
							j.setEnabled(false);
						}
					}
				}
			}
		});
	}

	public static void linkVisibility(final JComponent watcher,
			final JComponent... actor) {
		watcher.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				for (JComponent j : actor) {
					j.setVisible(true);
					j.updateUI();
				}
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				for (JComponent j : actor) {
					j.setVisible(false);
					j.updateUI();
				}
			}
		});

		// Initialise
		for (JComponent j : actor) {
			j.setVisible(watcher.isVisible());
		}
	}

	public static void linkForeground(final JComponent watcher,
			final JComponent... actor) {
		watcher.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if ("foreground".equals(event.getPropertyName())) {
					for (JComponent j : actor) {
						j.setForeground(watcher.getForeground());
						j.updateUI();
					}
				}
			}
		});

		// Initialise
		for (JComponent j : actor) {
			j.setForeground(watcher.getForeground());
		}
	}
}
