package com.beirtipol.binding.swt.table;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellEditor;

public class ButtonCellEditor extends KTableCellEditor {
	/**
	 * Activates the editor at the given position. Instantly closes the editor
	 * and switch the boolean content value.
	 * 
	 * @param row
	 * @param col
	 * @param rect
	 */
	@Override
	public void open(KTable table, int col, int row, Rectangle rect) {
		m_Table = table;
		m_Model = table.getModel();
		m_Rect = rect;
		m_Row = row;
		m_Col = col;

		close(true);

		GC gc = new GC(m_Table);
		m_Table.updateCell(m_Col, m_Row);
		gc.dispose();
	}

	@Override
	public void close(boolean save) {
		if (save) {
			m_Model.setContentAt(m_Col, m_Row, null);
		}
		super.close(save);
	}

	@Override
	protected Control createControl() {
		return null;
	}

	@Override
	public void setContent(Object content) {
	}

	@Override
	public int getActivationSignals() {
		return SINGLECLICK | KEY_RETURN_AND_SPACE;
	}
}