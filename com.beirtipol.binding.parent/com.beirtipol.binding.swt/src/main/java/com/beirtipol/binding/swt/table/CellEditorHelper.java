package com.beirtipol.binding.swt.table;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableModel;

public class CellEditorHelper {
	public static void moveToNextCell(KTable m_Table, boolean save) {
		Point[] cellSelection;

		if (m_Table.isRowSelectMode()) {
			moveToNextRow(m_Table, save, 0);
		} else {
			cellSelection = m_Table.getCellSelection();
			moveToNextCell(m_Table, save, cellSelection);
		}
	}

	public static void moveToNextRow(KTable m_Table, boolean save, int colToEdit) {
		Point[] cellSelection;

		if (m_Table.isRowSelectMode()) {
			cellSelection = getCellSelectionFromRowMode(m_Table, colToEdit);
		} else {
			cellSelection = m_Table.getCellSelection();
		}

		moveToNextCell(m_Table, save, cellSelection);
	}

	/**
	 * Move to the next editable cell
	 * 
	 * @param m_Table
	 * @param save
	 * @param cellSelection
	 */
	public static void moveToNextCell(KTable m_Table, boolean save, Point[] cellSelection) {
		// If user hits Esc we don't want to move to the next cell
		if (save == false) {
			return;
		}

		if (cellSelection.length == 1) {
			KTableModel m_Model = m_Table.getModel();

			KTableCellEditor nextCellEditor;
			int nextRow = cellSelection[0].y;
			do {
				nextRow++;
				nextCellEditor = m_Model.getCellEditor(cellSelection[0].x, nextRow);
			} while (nextCellEditor == null && m_Table.getSize().y > nextRow);

			m_Table.setSelection(cellSelection[0].x, nextRow, true);
		}
	}

	/**
	 * Tab across to the next editable cell
	 * 
	 * @param mTable
	 */
	public static void tabToNextCell(KTable m_Table, boolean save) {
		// If user hits Esc we don't want to tab to the next cell
		if (save == false) {
			return;
		}

		Point[] cellSelection = m_Table.getCellSelection();

		if (cellSelection.length == 1) {
			KTableModel m_Model = m_Table.getModel();

			KTableCellEditor nextCellEditor;
			int nextCol = cellSelection[0].x;
			do {
				nextCol++;
				nextCellEditor = m_Model.getCellEditor(nextCol, cellSelection[0].y);
			} while (nextCellEditor == null && m_Table.getSize().x > nextCol);

			m_Table.setSelection(nextCol, cellSelection[0].y, true);
		}
	}

	public static Point[] getCellSelectionFromRowMode(KTable table, int colToEdit) {
		int[] rowSelections = table.getRowSelection();

		List<Point> cellSelectionList = new ArrayList<Point>();

		for (int rowSelection : rowSelections) {
			cellSelectionList.add(new Point(colToEdit, rowSelection));
		}

		return cellSelectionList.toArray(new Point[] {});
	}

	/**
	 * Ignore enter key because we want enter to move us to the next cell
	 * 
	 * @param keyInput
	 * @return
	 */
	public static boolean isApplicable(String keyInput) {
		return !Character.toString(SWT.CR).equals(keyInput);
	}

	/**
	 * @param e
	 * @return True Ctrl+Enter been pressed
	 */
	public static boolean applyValueToSelectedCells(KeyEvent e) {
		return '\r' == e.character && ((SWT.CTRL == e.stateMask) || (e.stateMask == (SWT.CTRL | SWT.SHIFT)));
	}
}
