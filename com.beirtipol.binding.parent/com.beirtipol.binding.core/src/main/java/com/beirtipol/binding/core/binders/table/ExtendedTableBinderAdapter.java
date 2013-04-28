package com.beirtipol.binding.core.binders.table;

import com.beirtipol.binding.core.binders.IBasicBinder;

public class ExtendedTableBinderAdapter extends AbstractExtendedTableBinder {

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
	public Object getColumnHeader(int col, int row) {
		return null;
	}

	@Override
	public Object getRowHeader(int col, int row) {
		return null;
	}

	@Override
	public IBasicBinder<?> getDataCellBinder(int col, int row) {
		return null;
	}

	@Override
	public void cellSelected(int col, int row) {
	}
}