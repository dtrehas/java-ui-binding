package com.beirtipol.binding.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;

import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.binders.widget.IComboBinder;
import com.beirtipol.binding.core.delegates.IComboDelegate;
import com.beirtipol.binding.core.delegates.IDelegate;

public class SwingComboDelegate implements IComboDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(SwingComboDelegate.class);
	protected JComboBox combo;

	private final boolean editable;

	private IBasicBinder<? extends IDelegate> basicBinder;
	private boolean ignoreEvents = false;
	private Component myEditorComponent;
	private String valueFromText = "";

	private MyEditorComponentKeyListener myKeyListener;

	private MyFocusListener myFocusAdapter;
	private PropertyChangeListener propListener;
	private PopupMenuListener popupListener;
	private KeyAdapter keyAdapter;
	private ActionListener selectionListener;

	public SwingComboDelegate(final JComboBox combo, boolean editable) {
		this.combo = combo;
		this.editable = editable;

		if (combo == null) {
			throw new IllegalArgumentException("Combo cannot be null");
		}

		if (editable) {
			setComboToAutoComplete(combo);
		}

		/**
		 * Allow correct rendering under the glass pane
		 */
		combo.setLightWeightPopupEnabled(false);
		try {
			Class<?> cls = Class.forName("javax.swing.PopupFactory");
			Field field = cls.getDeclaredField("forceHeavyWeightPopupKey");
			field.setAccessible(true);
			combo.putClientProperty(field.get(null), Boolean.TRUE);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		/**
		 * Change the size of the popup list to fit in all the values. Dirty
		 * hack courtesy of
		 * http://forums.java.net/jive/message.jspa?messageID=61267
		 */

		popupListener = new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				JComboBox box = (JComboBox) e.getSource();
				Object comp = box.getUI().getAccessibleChild(box, 0);

				if (!(comp instanceof JPopupMenu)) {
					return;
				}

				JComponent scrollPane = (JComponent) ((JPopupMenu) comp).getComponent(0);
				Dimension size = new Dimension();
				size.width = Math.max(box.getPreferredSize().width, box.getSize().width - 2);
				size.height = scrollPane.getPreferredSize().height;
				scrollPane.setPreferredSize(size);
				scrollPane.setMaximumSize(size);

				if (basicBinder instanceof IComboBinder) {
					IComboBinder binder = (IComboBinder) basicBinder;

					if (combo.getItemCount() != binder.getAvailableItems().length) {
						checkListCurrent(binder);
						combo.setPopupVisible(true);
					}
				}
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
			}
		};

		combo.addPopupMenuListener(popupListener);

		/**
		 * When embedding Swing controls within SWT, SWT periodically calls
		 * updateUI for some unknown reason. This eventually tells the combo to
		 * updateUI which recreates the editorComponent on the combo, thus
		 * killing the listeners.
		 */

		propListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (combo.getEditor() != null && myEditorComponent != combo.getEditor().getEditorComponent()) {
					/*
					 * The Key/Focus listeners will only be added to the
					 * editorComponent if it is editable, otherwise the
					 * focusListener is added to the combo itself.
					 */
					if (SwingComboDelegate.this.editable) {
						if (myEditorComponent != null) {
							myEditorComponent.removeKeyListener(myKeyListener);
							myEditorComponent.removeFocusListener(myFocusAdapter);
						}
						LOGGER.info("The editor has changed on a SwingComboDelegate for combo: " + combo.hashCode() + ". Adding the keyListener and focusListener again.");
						myEditorComponent = combo.getEditor().getEditorComponent();
						myEditorComponent.addKeyListener(myKeyListener);
						myEditorComponent.addFocusListener(myFocusAdapter);
					}
				}
			}
		};

		combo.addPropertyChangeListener("editor", propListener);
	}

	public SwingComboDelegate(final JComboBox combo) {
		this(combo, true);
	}

	private void setComboToAutoComplete(final JComboBox combo) {
		myEditorComponent = combo.getEditor().getEditorComponent();
		combo.setEditable(true);

		myKeyListener = new MyEditorComponentKeyListener();
		myEditorComponent.addKeyListener(myKeyListener);
	}

	private String find(String textToFind, Object[] allowedValues) {
		String parsedValue = null;

		if (textToFind != null && textToFind.trim().length() > 0 && allowedValues != null) {
			textToFind = textToFind.toUpperCase();

			for (Object allowedValue : allowedValues) {
				String asString = ObjectUtils.toString(allowedValue);
				String allowedValueUpperCase = asString.toUpperCase();

				if (allowedValueUpperCase.startsWith(textToFind)) {
					parsedValue = asString;
					break;
				}
			}
		}

		return parsedValue;
	}

	private void checkListCurrent(final IComboBinder binder) {
		if (ignoreEvents) {
			return;
		}

		try {
			ignoreEvents = true;
			final Object oldValue = combo.getSelectedItem();
			Object[] items = binder.getAvailableItems();

			if (items != null) {
				combo.removeAllItems();

				for (Object i : items) {
					combo.addItem(i);
				}

				combo.setSelectedItem(oldValue);
				combo.setMaximumRowCount(Math.min(10, items.length));
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			ignoreEvents = false;
		}
	}

	@Override
	public void setBackground(final Color background) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				/**
				 * Calling combo.setBackground() doesn't do anything - you have
				 * to set the background of one of the components within the
				 * combo.
				 */
				ComboBoxEditor comboEditor = combo.getEditor();
				if (comboEditor == null) {
					return;
				}
				Component comp = comboEditor.getEditorComponent();
				if (comp instanceof JComponent) {
					JComponent jcomp = (JComponent) comp;
					/**
					 * The combo needs opacity set as well as the colour. Here,
					 * ensure that default-coloured combos are not unnecessarily
					 * set to opaque
					 */
					boolean hasNonDefaultBackground = (background != null && background != Color.WHITE);
					jcomp.setOpaque(hasNonDefaultBackground);

					/**
					 * Setting the background to null seems to be fine - Swing
					 * is treating null as a system default.
					 */
					jcomp.setBackground(background);
				}
			}
		});
	}

	@Override
	public void setForeground(final Color textColour) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				/**
				 * As with setBackground(), we have to set the foreground colour
				 * of an inner component, rather than of the combo itself
				 */
				ComboBoxEditor comboEditor = combo.getEditor();
				if (comboEditor == null) {
					return;
				}
				Component comp = comboEditor.getEditorComponent();
				comp.setForeground(textColour);
			}
		});
	}

	@Override
	public void setToolTip(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				combo.setToolTipText(text);
			}
		});
	}

	@Override
	public void setSelected(final Object o) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (ObjectUtils.equals(combo.getSelectedItem(), o)) {
					return;
				}
				try {
					ignoreEvents = true;
					int item = -1;
					for (item = 0; item != combo.getItemCount(); item++) {
						if (combo.getItemAt(item) != null && ((String) combo.getItemAt(item)).equals(o)) {
							break;
						}
					}

					if (item != -1 && item < combo.getItemCount()) {
						combo.setSelectedIndex(item);
					} else {
						combo.addItem(o);
						combo.setSelectedItem(o);
					}
				} finally {
					ignoreEvents = false;
				}
			}
		});
	}

	@Override
	public void setBinder(IComboBinder comboBinder) {
		basicBinder = comboBinder;
	}

	protected void performListenerAction(final IComboBinder binder) {
		Object selectedItem = combo.getSelectedItem();
		String value = (String) selectedItem;

		if (!ObjectUtils.equals(binder.getSelectedItem(), selectedItem)) {
			binder.setSelectedItem(selectedItem);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		combo.setEnabled(enabled);
	}

	@Override
	public void setVisible(boolean visible) {
		combo.setVisible(visible);
	}

	@Override
	public void addTraverseListener(final IComboBinder binder) {
		keyAdapter = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_TAB:
				case KeyEvent.VK_ENTER:
					performListenerAction(binder);
				}
			}
		};

		combo.addKeyListener(keyAdapter);
	}

	@Override
	public void addSelectionListener(final IComboBinder binder) {
		selectionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ignoreEvents) {
					return;
				}
				performListenerAction(binder);
			}
		};

		combo.addActionListener(selectionListener);
	}

	/**
	 * TODO: When not on a release branch, this and the keylistener code needs
	 * to be safely refactored to handle all this crap in one place.
	 */
	@Override
	public void addFocusListener(final IComboBinder binder) {
		myFocusAdapter = new MyFocusListener();
		if (editable) {
			combo.getEditor().getEditorComponent().addFocusListener(myFocusAdapter);
		} else {
			combo.addFocusListener(myFocusAdapter);
		}
	}

	private class MyFocusListener extends FocusAdapter {
		@Override
		public void focusGained(FocusEvent e) {
			checkListCurrent((IComboBinder) basicBinder);
		}

		@Override
		public void focusLost(FocusEvent e) {
			super.focusLost(e);
		}
	}

	private class MyEditorComponentKeyListener extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() != KeyEvent.VK_ENTER && e.getKeyCode() != KeyEvent.VK_DELETE && e.getKeyCode() != KeyEvent.VK_ESCAPE) {
				JTextField editor = ((JTextField) e.getSource());

				String selectedText = editor.getSelectedText();
				String textEnteredSoFar = editor.getText();

				/*
				 * This covers some mad crap when the user types really really
				 * fast and somehow no text ends up being selected because
				 * textEntedSoFar is the entire string. No idea....
				 */
				if (StringUtils.isNotBlank(selectedText)) {
					int lengthOfSelected = selectedText.length();
					int potentialEntireLength = textEnteredSoFar.length();
					textEnteredSoFar = textEnteredSoFar.substring(0, potentialEntireLength - lengthOfSelected);
				}

				String value = "";
				if (basicBinder != null && basicBinder instanceof IComboBinder) {
					value = find(textEnteredSoFar, ((IComboBinder) basicBinder).getAvailableItems());

					if (value == null && !valueFromText.equals("")) {
						value = valueFromText;
						textEnteredSoFar = value;
					}

					if (value != null && value.trim().length() > 0) {
						valueFromText = value;
						editor.setText(value);

						int startOfSelection = textEnteredSoFar.length();

						// If backspace was pressed move test selection back by
						// one
						if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
							startOfSelection--;
						}

						editor.setSelectionStart(startOfSelection);
						editor.setSelectionEnd(value.length());
					}
				}
			}
		}
	}

	@Override
	public void free() {
		if (combo != null) {
			combo.getEditor().getEditorComponent().removeFocusListener(myFocusAdapter);

			if (propListener != null) {
				combo.removePropertyChangeListener("editor", propListener);
				propListener = null;
			}

			if (popupListener != null) {
				combo.removePopupMenuListener(popupListener);
				popupListener = null;
			}

			if (keyAdapter != null) {
				combo.removeKeyListener(keyAdapter);
				keyAdapter = null;
			}

			if (selectionListener != null) {
				combo.removeActionListener(selectionListener);
			}

			if (myEditorComponent != null) {
				myEditorComponent.removeFocusListener(myFocusAdapter);
				myEditorComponent.removeKeyListener(myKeyListener);
			}
			combo.removeAll();
		}

		combo = null;
		basicBinder.setDelegate(null);
		basicBinder = null;
		myEditorComponent = null;
		myKeyListener = null;
		myFocusAdapter = null;
		selectionListener = null;
	};
}
