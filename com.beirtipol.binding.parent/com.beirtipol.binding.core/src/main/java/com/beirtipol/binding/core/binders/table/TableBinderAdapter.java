package com.beirtipol.binding.core.binders.table;

import java.awt.Point;
import java.util.List;

public class TableBinderAdapter extends AbstractTableBinder {

	@Override
	public int getDataRowCount() {
		return 0;
	}

	@Override
	public int getDataColumnCount() {
		return 0;
	}

	@Override
	public int getFixedHeaderColumnCount() {
		return 0;
	}

	@Override
	public int getFixedHeaderRowCount() {
		return 0;
	}

	@Override
	public int getFixedSelectableColumnCount() {
		return 0;
	}

	@Override
	public int getFixedSelectableRowCount() {
		return 0;
	}

	@Override
	public void setValue(int col, int row, Object value) {
	}

	@Override
	public void setValue(List<Point> cells, Object value) {
	}

	@Override
	public Object getValue(int col, int row) {
		return null;
	}

	@Override
	public Object getColumnHeader(int col, int row) {
		return null;
	}

	@Override
	public Object getRowHeader(int col, int row) {
		return null;
	}

	@Override
	public void cellSelected(int col, int row) {

	}
}