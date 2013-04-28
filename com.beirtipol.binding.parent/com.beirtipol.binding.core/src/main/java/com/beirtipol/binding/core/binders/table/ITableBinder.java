package com.beirtipol.binding.core.binders.table;

import java.awt.Point;
import java.util.List;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.delegates.ITableDelegate;

/**
 * Table binding is a tricky situation as it does not follow the same paradigm
 * as other, more simple, widgets. The Binder and the Delegate need to reference
 * each other continuously as, in the case of KTable and JTable, the Delegate is
 * actually the datamodel of the table, not the widget itself. It is very easy
 * to slip up and allow the Binder to query the delegate for state such as
 * selection but it is very important not to let this happen, as UI thread
 * issues come to play very easily.
 * 
 * @author O041484
 */
public interface ITableBinder extends IBasicBinder<ITableDelegate> {
	void updateCell(int col, int row);

	void updateCells(final List<Point> cells);

	// Accessors
	void setValue(int col, int row, Object value);

	void setValue(List<Point> cells, Object value);

	Object getValue(int col, int row);

	Object getColumnHeader(int col, int row);

	Object getRowHeader(int col, int row);

	// Dimensions
	int getDataRowCount();

	int getDataColumnCount();

	int getFixedHeaderColumnCount();

	int getFixedHeaderRowCount();

	int getFixedSelectableColumnCount();

	int getFixedSelectableRowCount();

	int getInitialColumnWidth(int col);

	int getInitialRowHeight(int row);

	/**
	 * Callback from the Delegate to inform the binder that selection has
	 * occurred
	 * 
	 * @param x
	 * @param y
	 */
	void cellSelected(int col, int row);

}
