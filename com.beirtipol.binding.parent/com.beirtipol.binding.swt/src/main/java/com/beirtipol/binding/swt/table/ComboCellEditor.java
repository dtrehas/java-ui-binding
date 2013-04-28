package com.beirtipol.binding.swt.table;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang.ObjectUtils;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.beirtipol.binding.core.binders.widget.IComboBinder;
import com.beirtipol.binding.swt.util.SWTSelectionUtils;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellEditor;

public class ComboCellEditor extends KTableCellEditor {
	private CCombo m_Combo;
	private final Cursor m_ArrowCursor = new Cursor(Display.getDefault(),
			SWT.CURSOR_ARROW);

	private final IComboBinder binder;

	public ComboCellEditor(IComboBinder binder) {
		this.binder = binder;
	}

	private final KeyAdapter keyListener = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			try {
				onKeyPressed(e);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};

	private final TraverseListener travListener = new TraverseListener() {
		@Override
		public void keyTraversed(TraverseEvent e) {
			onTraverse(e);
		}
	};
	private ComboViewer myViewer;
	private AutoCompleteField m_autoCompleteField;

	@Override
	public void open(KTable table, int col, int row, Rectangle rect) {
		super.open(table, col, row, rect);
		m_Combo.setFocus();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void close(boolean save) {
		if (save) {
			/**
			 * Sometimes I amaze even myself with the depths of hackery I sink
			 * in to. Our problem here is that the CCombo steals the 'enter'
			 * event which causes the proposal popup to close without telling
			 * the combo what the user has chosen. Look away now and pretend you
			 * saw nothing here.
			 */
			Object content = null;
			try {
				Field adapterField = AutoCompleteField.class
						.getDeclaredField("adapter");
				adapterField.setAccessible(true);
				ContentProposalAdapter adapter = (ContentProposalAdapter) adapterField
						.get(m_autoCompleteField);
				Field popupField = adapter.getClass().getDeclaredField("popup");
				popupField.setAccessible(true);
				Object popup = popupField.get(adapter);
				if (popup != null) {
					Method method = popup.getClass().getDeclaredMethod(
							"getSelectedProposal", new Class[] {});
					method.setAccessible(true);
					IContentProposal result = (IContentProposal) method.invoke(
							popup, new Object[] {});
					for (Object item : binder.getAvailableItems()) {
						if (binder.getItemBinder().convert(item)
								.equalsIgnoreCase(result.getContent())) {
							content = item;
							break;
						}
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			/**
			 * Now, if we haven't gotten the selected proposal from the dropdown
			 * table, we need to see if the user has selected anything from the
			 * combo. This will already be a <T> so will not need to be parsed
			 * by the binder.
			 */
			if (content == null) {
				content = SWTSelectionUtils.getSingleSelection(myViewer
						.getSelection());
			}
			/**
			 * Since nothing selected and nothing proposed, just use the text
			 * from the combo and parse it to the correct object using the item
			 * binder. We do this here because the ComboViewer only talks to the
			 * binder using <T>.
			 */

			// TODO: Do we really need this?
			// if (content == null) {
			// content = binder.getItemBinder().parse(m_Combo.getText());
			// }

			m_Model.setContentAt(m_Col, m_Row, content);
		}
		m_Combo.removeKeyListener(keyListener);
		m_Combo.removeTraverseListener(travListener);
		super.close(save);
		m_Combo = null;
		m_ArrowCursor.dispose();
		myViewer = null;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Control createControl() {
		m_Combo = new CCombo(m_Table, SWT.NONE);
		m_Combo.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_LIST_BACKGROUND));
		m_Combo.addKeyListener(keyListener);
		m_Combo.addTraverseListener(travListener);
		m_Combo.setCursor(m_ArrowCursor);

		myViewer = new ComboViewer(m_Combo);
		myViewer.setContentProvider(new ArrayContentProvider());
		m_Combo.setVisibleItemCount(25);
		if (binder.getAvailableItems() != null) {
			myViewer.setInput(binder.getAvailableItems());
		}
		String[] proposals = new String[binder.getAvailableItems().length];
		for (int i = 0; i < binder.getAvailableItems().length; i++) {
			proposals[i] = binder.getItemBinder().convert(
					binder.getAvailableItems()[i]);
		}
		m_autoCompleteField = new AutoCompleteField(m_Combo,
				new CComboContentAdapter(), proposals);

		return m_Combo;
	}

	@Override
	public void setBounds(Rectangle rect) {
		super.setBounds(new Rectangle(rect.x, rect.y + 1, rect.width,
				rect.height - 2));
	}

	@Override
	protected void onTraverse(TraverseEvent e) {
		// set selection to the appropriate next element:
		switch (e.keyCode) {
		case SWT.ARROW_UP: // Go to previous item
		case SWT.ARROW_DOWN: // Go to next item
		{
			// Just don't treat the event
			break;
		}
		default: {
			super.onTraverse(e);
			break;
		}
		}
	}

	@Override
	public void setContent(Object content) {
		String asString = ObjectUtils.toString(content);
		m_Combo.setText(asString);
		// The combo has the text set but the cursor remains at position 0 by
		// default. i.e. typing
		// 'r' leaves you with "|r".
		m_Combo.setSelection(new Point(asString.length(), asString.length()));
	}
}
