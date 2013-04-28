package com.beirtipol.binding.core.delegates;

public interface ITableDelegate extends IDelegate {
	void updateCell(int col, int row);

	void setRedraw(boolean redraw);

	void redrawTable();
}
