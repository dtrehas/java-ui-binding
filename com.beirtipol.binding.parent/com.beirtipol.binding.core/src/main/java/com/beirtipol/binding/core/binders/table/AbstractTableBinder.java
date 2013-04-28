package com.beirtipol.binding.core.binders.table;

import java.awt.Point;
import java.util.List;

import com.beirtipol.binding.core.binders.AbstractBinder;
import com.beirtipol.binding.core.delegates.ITableDelegate;

public abstract class AbstractTableBinder extends
		AbstractBinder<ITableDelegate> implements ITableBinder {
	@Override
	public void updateUI() {
		ITableDelegate delegate = getDelegate();
		if (delegate != null) {
			delegate.redrawTable();
		}
	}

	@Override
	public void updateCell(int col, int row) {
		ITableDelegate delegate = getDelegate();
		if (delegate != null) {
			delegate.updateCell(col, row);
			updateUI();
		}
	}

	@Override
	public void updateCells(List<Point> cells) {
		ITableDelegate delegate = getDelegate();
		if (delegate != null) {
			try {
				delegate.setRedraw(false);
				for (Point point : cells) {
					delegate.updateCell(point.x, point.y);
				}
			} finally {
				delegate.setRedraw(true);
			}
			updateUI();
		}
	}

	/**
	 * Standard Width
	 */
	@Override
	public int getInitialColumnWidth(int col) {
		return 60;
	}

	/**
	 * Standard Height
	 */
	@Override
	public int getInitialRowHeight(int row) {
		return 18;
	}

	@Override
	protected void setupListeners() {
		// TODO Auto-generated method stub

	}

}
