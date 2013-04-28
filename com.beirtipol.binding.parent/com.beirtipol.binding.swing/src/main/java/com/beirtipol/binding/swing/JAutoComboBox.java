package com.beirtipol.binding.swing;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicComboBoxEditor;

public class JAutoComboBox extends JComboBox {
	public class AutoTextFieldEditor extends BasicComboBoxEditor {

		private JAutoTextField getAutoTextFieldEditor() {
			return (JAutoTextField) editor;
		}

		AutoTextFieldEditor(java.util.List list) {
			editor = new JAutoTextField(list, JAutoComboBox.this);
			editor.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					fireActionEvent();
				}

				@Override
				public void focusGained(FocusEvent e) {
					// TODO Auto-generated method stub

				}
			});
		}
	}

	public JAutoComboBox(java.util.List list) {
		isFired = false;
		autoTextFieldEditor = new AutoTextFieldEditor(list);

		setEditable(true);
		setModel(new DefaultComboBoxModel(list.toArray()) {

			@Override
			protected void fireContentsChanged(Object obj, int i, int j) {
				if (!isFired) {
					super.fireContentsChanged(obj, i, j);
				}
			}

		});
		setEditor(autoTextFieldEditor);
	}

	public Component getEditorCoponent() {
		return autoTextFieldEditor.getEditorComponent();
	}

	public boolean isCaseSensitive() {
		return autoTextFieldEditor.getAutoTextFieldEditor().isCaseSensitive();
	}

	public void setCaseSensitive(boolean flag) {
		autoTextFieldEditor.getAutoTextFieldEditor().setCaseSensitive(flag);
	}

	public boolean isStrict() {
		return autoTextFieldEditor.getAutoTextFieldEditor().isStrict();
	}

	public void setStrict(boolean flag) {
		autoTextFieldEditor.getAutoTextFieldEditor().setStrict(flag);
	}

	public java.util.List getDataList() {
		return autoTextFieldEditor.getAutoTextFieldEditor().getDataList();
	}

	public void setDataList(java.util.List list) {
		autoTextFieldEditor.getAutoTextFieldEditor().setDataList(list);
		setModel(new DefaultComboBoxModel(list.toArray()));
	}

	public AutoTextFieldEditor getThing() {
		return (AutoTextFieldEditor) editor;
	}

	public void setSelectedValue(Object obj) {
		if (isFired) {
			return;
		} else {
			isFired = true;
			setSelectedItem(obj);
			fireItemStateChanged(new ItemEvent(this, 701, selectedItemReminder, 1));
			isFired = false;
			return;
		}
	}

	@Override
	protected void fireActionEvent() {
		if (!isFired) {
			super.fireActionEvent();
		}
	}

	private final AutoTextFieldEditor autoTextFieldEditor;

	private boolean isFired;

}
