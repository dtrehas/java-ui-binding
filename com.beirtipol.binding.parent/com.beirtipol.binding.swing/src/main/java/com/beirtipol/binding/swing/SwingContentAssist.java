package com.beirtipol.binding.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.BadLocationException;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.beirtipol.binding.core.binders.IContentAssistProvider;

@SuppressWarnings("serial")
public class SwingContentAssist {
	private final JTextField textField;
	private final JPopupMenu assistPopup;
	private final JList list;

	private IContentAssistProvider contentProvider;
	protected int startingCaretPos;

	public SwingContentAssist(JTextField textField) {
		this(textField, null);
	}

	public SwingContentAssist(final JTextField textField, IContentAssistProvider contentProvider) {
		this.textField = textField;
		this.contentProvider = contentProvider;

		// We are rendering a list on the menu as menus are not as flexible when
		// it comes to
		// getting/setting values and stuff
		list = new JList();
		list.setFocusable(false);

		JScrollPane scroll = new JScrollPane(list);
		scroll.setBorder(null);
		scroll.getVerticalScrollBar().setFocusable(false);
		scroll.getHorizontalScrollBar().setFocusable(false);

		assistPopup = new JPopupMenu();
		assistPopup.setFocusable(false);
		assistPopup.setRequestFocusEnabled(false);
		assistPopup.setBackground(Color.WHITE);
		assistPopup.setLightWeightPopupEnabled(false);
		glassPaneWorkAround(assistPopup);
		assistPopup.add(scroll);

		addListeners();
	}

	/**
	 * Allow correct rendering under the glass pane
	 */
	@SuppressWarnings("rawtypes")
	private void glassPaneWorkAround(JComponent component) {
		try {
			Class cls = Class.forName("javax.swing.PopupFactory");
			Field field = cls.getDeclaredField("forceHeavyWeightPopupKey");
			field.setAccessible(true);
			component.putClientProperty(field.get(null), Boolean.TRUE);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void addListeners() {
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// Ignore key events if the list is being edited
				if (assistPopup.isVisible()) {
					if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
						return;
					}

					populateAndFilter();
				} else {
					if (((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) == KeyEvent.CTRL_DOWN_MASK) && e.getKeyCode() == KeyEvent.VK_SPACE) {
						showMenu();
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						showMenu();
					} else {
						if (contentProvider == null) {
							return;
						}

						char[] activationKeys = contentProvider.getActivationKeys();
						for (char key : activationKeys) {
							if (key == e.getKeyChar()) {
								showMenu();
								return;
							}
						}
					}
				}
			}
		});

		assistPopup.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				startingCaretPos = getCurrentCaretPos();

				textField.registerKeyboardAction(acceptAction, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);
				textField.registerKeyboardAction(hidePopupAction, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_FOCUSED);
				textField.registerKeyboardAction(downAction, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), JComponent.WHEN_FOCUSED);
				textField.registerKeyboardAction(upAction, KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), JComponent.WHEN_FOCUSED);
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				startingCaretPos = -1;

				textField.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
				textField.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
				textField.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
				textField.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				// When Esc is pressed make sure the caret is still in the right
				// place
				moveCaretToEnd();
			}
		});

		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String selectedValue = ObjectUtils.toString(list.getSelectedValue());
				acceptValue(selectedValue);
			}
		});
	}

	private void populateAndFilter() {
		if (contentProvider == null) {
			return;
		}

		String[] allowedValues = contentProvider.getSuggestions(textField.getText(), textField.getCaretPosition());

		list.setListData(allowedValues);

		if (allowedValues.length > 0) {
			list.setSelectedIndex(0);
		} else {
			assistPopup.setVisible(false);
		}
	}

	private void showMenu() {
		populateAndFilter();

		if (list.getComponentCount() == 0) {
			return;
		}

		int xPos = 0;
		try {
			int pos = Math.min(textField.getCaret().getDot(), getCurrentCaretPos());
			xPos = textField.getUI().modelToView(textField, pos).x;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		int yPos = textField.getSize().height;
		assistPopup.show(textField, xPos, yPos);

		textField.requestFocus();
	}

	private void moveCaretToEnd() {
		textField.setCaretPosition(textField.getText().length());
	}

	public void setContentProvider(IContentAssistProvider contentProvider) {
		this.contentProvider = contentProvider;
	}

	private void insertText(String textToAppend) {
		if (StringUtils.isBlank(textToAppend)) {
			return;
		}

		StringBuilder str = new StringBuilder(textField.getText());

		int selectionStart = textField.getSelectionStart();
		int selectionEnd = textField.getSelectionEnd();
		int currentCaretPos = getCurrentCaretPos();
		int caretEndingPos = 0;

		// Replace any text that was typed while the popup was shown
		if (startingCaretPos >= 0) {
			if (startingCaretPos < currentCaretPos) {
				for (int i = startingCaretPos; i < currentCaretPos; i++) {
					char typedChar = str.charAt(i);
					for (char activationKey : contentProvider.getActivationKeys()) {
						if (activationKey == typedChar) {
							startingCaretPos = i + 1;
							break;
						}
					}
				}

				str.delete(startingCaretPos, currentCaretPos);
			} else if (startingCaretPos > currentCaretPos) {
				str.delete(currentCaretPos, startingCaretPos);
				startingCaretPos = currentCaretPos;
			}
		}

		// Replace any text that was selected by the user
		if (selectionStart != selectionEnd) {
			str.delete(selectionStart, selectionEnd);
			startingCaretPos = selectionStart;
		}

		// Replace any text before the insertion point that matches the
		// selection (i.e. typing 'usd'
		// then showing the menu and selecting selecting 'USD_Swap_3M' should
		// result in
		// 'USD_Swap_3M' not 'usdUSD_Swap_3M'. (Note: this case it not the same
		// as the case where
		// the popup is already show before the user starts typing a value)
		boolean foundTextToReplaceBeforeTheStartingPoint = false;
		int indexOfLastCharBeforeInsertionPoint = startingCaretPos - 1;
		if (indexOfLastCharBeforeInsertionPoint > 0) {
			char lastCharBeforeInsertionPoint = str.toString().toUpperCase().charAt(indexOfLastCharBeforeInsertionPoint);
			String upperCaseTextToAppend = textToAppend.toUpperCase();
			int indexOfLastCharInInsertionText = upperCaseTextToAppend.indexOf(lastCharBeforeInsertionPoint);
			if (indexOfLastCharInInsertionText != -1) {
				int startOfMatchingString = startingCaretPos - 1 - indexOfLastCharInInsertionText;
				String partOfTextThatMatchedTheNewInput = str.substring(startOfMatchingString, startingCaretPos);
				if (upperCaseTextToAppend.startsWith(partOfTextThatMatchedTheNewInput.toUpperCase())) {
					str.replace(startOfMatchingString, startingCaretPos, textToAppend);
					caretEndingPos = startOfMatchingString + textToAppend.length();
					foundTextToReplaceBeforeTheStartingPoint = true;
				}
			}
		}

		if (!foundTextToReplaceBeforeTheStartingPoint) {
			str.insert(startingCaretPos, textToAppend);
			caretEndingPos = startingCaretPos + textToAppend.length();
		}

		textField.setText(str.toString());

		// Set the Caret to the end of the inserted text
		textField.setCaretPosition(caretEndingPos);
	}

	private int getCurrentCaretPos() {
		return textField.getCaret().getMark();
	}

	private void acceptValue(String selectedValue) {
		insertText(selectedValue);
		assistPopup.setVisible(false);
	}

	private final Action acceptAction = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String selectedValue = ObjectUtils.toString(list.getSelectedValue());
			acceptValue(selectedValue);
		}
	};

	private final Action downAction = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int selected = list.getSelectedIndex();
			int newSelection = selected + 1;
			newSelection = Math.min(list.getModel().getSize(), newSelection);

			if (selected != newSelection) {
				list.setSelectedIndex(newSelection);
				assistPopup.invalidate();
				assistPopup.repaint();
			}
		}
	};

	private final Action upAction = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int selected = list.getSelectedIndex();
			int newSelection = selected - 1;
			newSelection = Math.max(0, newSelection);

			if (selected != newSelection) {
				list.setSelectedIndex(newSelection);
				assistPopup.invalidate();
				assistPopup.repaint();
			}
		}
	};

	private final Action hidePopupAction = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			assistPopup.setVisible(false);
		}
	};
}
