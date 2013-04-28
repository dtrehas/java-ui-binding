package com.beirtipol.binding.swt.table;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import com.beirtipol.binding.core.binders.table.ITableBinder;
import com.beirtipol.binding.core.delegates.ITableDelegate;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableCellSelectionListener;
import de.kupzog.ktable.KTableDefaultModel;
import de.kupzog.ktable.renderers.DefaultCellRenderer;
import de.kupzog.ktable.renderers.FixedCellRenderer;

public class SWTKTableDelegate extends KTableDefaultModel implements
		ITableDelegate {

	private static final Logger LOGGER = Logger
			.getLogger(SWTKTableDelegate.class);

	enum CellType {
		BLANK, COLUMN_HEADER, ROW_HEADER, DATA;
	}

	private final KTable myKTable;

	private DisposeListener disposeListener;

	private final ITableBinder myTableBinder;

	private KTableCellSelectionListener mySelectionListener;

	public SWTKTableDelegate(KTable myTable, ITableBinder myTableBinder) {
		this.myKTable = myTable;
		this.myTableBinder = myTableBinder;
		myTableBinder.setDelegate(this);

		if (myKTable != null && !myKTable.isDisposed()) {
			myKTable.setModel(SWTKTableDelegate.this);

			initDisposeListener();
			initSelectionListener();
		}
	}

	protected void initSelectionListener() {
		mySelectionListener = new KTableCellSelectionListener() {
			@Override
			public void fixedCellSelected(int col, int row, int statemask) {
				// TODO Auto-generated method stub
			}

			@Override
			public void cellSelected(int col, int row, int statemask) {
				CellType cellType = getCellType(col, row);
				if (cellType == CellType.DATA) {
					Point rel = createRelativePointFromAbs(cellType, col, row);
					myTableBinder.cellSelected(rel.x, rel.y);
				}
			}
		};
		myKTable.addCellSelectionListener(mySelectionListener);
	}

	protected void initDisposeListener() {
		disposeListener = new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				free();
			}
		};
		myKTable.addDisposeListener(disposeListener);
	}

	@Override
	public void setRedraw(boolean redraw) {
		if (myKTable != null && !myKTable.isDisposed()) {
			myKTable.setRedraw(redraw);
		}
	}

	@Override
	public void updateCell(final int col, final int row) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (myKTable.isCellFullyVisible(col, row)) {
					myKTable.updateCell(col, row);
				}
			}
		});
	}

	@Override
	public void redrawTable() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (myKTable != null && !myKTable.isDisposed()) {
					myKTable.redraw();
				}
			}
		});
	}

	public CellType getCellType(int col, int row) {
		if (col < getFixedColumnCount()) {
			if (row < getFixedRowCount()) {
				return CellType.BLANK;
			}
			return CellType.ROW_HEADER;
		}

		if (row < getFixedRowCount()) {
			return CellType.COLUMN_HEADER;
		}

		return CellType.DATA;
	}

	public Point createRelativePointFromAbs(CellType cellType, int col, int row) {
		if (cellType != CellType.BLANK) {
			if (cellType == CellType.DATA) {
				row -= getFixedRowCount();
				col -= getFixedColumnCount();
			}
			if (cellType == CellType.ROW_HEADER) {
				row -= getFixedRowCount();
			}
			if (cellType == CellType.COLUMN_HEADER) {
				col -= getFixedColumnCount();
			}
		}
		Point result = new Point(col, row);
		return result;
	}

	public Point createAbosolutePointFromRelative(CellType cellType, int col,
			int row) {
		if (cellType != CellType.BLANK) {
			if (cellType == CellType.DATA) {
				row += getFixedRowCount();
				col += getFixedColumnCount();
			}
			if (cellType == CellType.ROW_HEADER) {
				row += getFixedRowCount();
			}
			if (cellType == CellType.COLUMN_HEADER) {
				col += getFixedColumnCount();
			}
		}
		Point result = new Point(col, row);
		return result;
	}

	@Override
	public void free() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (myKTable != null && !myKTable.isDisposed()) {
					myKTable.setModel(null);
					if (disposeListener != null) {
						myKTable.removeDisposeListener(disposeListener);
						disposeListener = null;
					}
					if (mySelectionListener != null) {
						myKTable.removeCellSelectionListener(mySelectionListener);
						mySelectionListener = null;
					}
				}
			}
		});
	}

	@Override
	public int getFixedHeaderColumnCount() {
		return myTableBinder.getFixedHeaderColumnCount();
	}

	@Override
	public int getFixedHeaderRowCount() {
		return myTableBinder.getFixedHeaderRowCount();
	}

	@Override
	public int getFixedSelectableColumnCount() {
		return myTableBinder.getFixedSelectableColumnCount();
	}

	@Override
	public int getFixedSelectableRowCount() {
		return myTableBinder.getFixedSelectableRowCount();
	}

	@Override
	public int getRowHeightMinimum() {
		return 0;
	}

	@Override
	public boolean isColumnResizable(int col) {
		return true;
	}

	@Override
	public boolean isRowResizable(int row) {
		return true;
	}

	@Override
	public KTableCellEditor doGetCellEditor(int col, int row) {
		return null;
	}

	@Override
	public KTableCellRenderer doGetCellRenderer(int col, int row) {
		return new FixedCellRenderer(DefaultCellRenderer.STYLE_FLAT
				| DefaultCellRenderer.INDICATION_FOCUS
				| DefaultCellRenderer.INDICATION_SORT);
	}

	@Override
	public int doGetColumnCount() {
		int columnCount = 0;
		columnCount += getFixedColumnCount();
		columnCount += myTableBinder.getDataColumnCount();
		return columnCount;
	}

	@Override
	public int doGetRowCount() {
		int rowCount = 0;
		rowCount += getFixedRowCount();
		rowCount += myTableBinder.getDataRowCount();
		return rowCount;
	}

	@Override
	public void doSetContentAt(int col, int row, Object value) {
		CellType cellType = getCellType(col, row);
		if (cellType == CellType.DATA) {
			Point rel = createRelativePointFromAbs(cellType, col, row);
			myTableBinder.setValue(rel.x, rel.y, value);
		}
	}

	@Override
	public Object doGetContentAt(int col, int row) {
		Object content = null;

		try {
			CellType cellType = getCellType(col, row);
			if (cellType == CellType.DATA) {
				Point rel = createRelativePointFromAbs(cellType, col, row);
				content = myTableBinder.getValue(rel.x, rel.y);
			} else if (cellType == CellType.COLUMN_HEADER) {
				content = myTableBinder.getColumnHeader(col, row);
			} else if (cellType == CellType.ROW_HEADER) {
				content = myTableBinder.getRowHeader(col, row);
			}
		} catch (RuntimeException e) {
			// Unhandled Exceptions do not play nicely in KTable.
			LOGGER.error("Error in table\n" + e.getMessage(), e);
		}

		// Cell Editors do not like nulls.
		if (content == null) {
			content = "";
		}

		return content;
	}

	@Override
	public int getInitialColumnWidth(int col) {
		return myTableBinder.getInitialColumnWidth(col);
	}

	@Override
	public int getInitialRowHeight(int row) {
		return myTableBinder.getInitialRowHeight(row);
	}

}