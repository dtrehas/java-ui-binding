package com.beirtipol.binding.swt.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellResizeListener;
import de.kupzog.ktable.KTableModel;
import de.kupzog.ktable.SWTX;

/**
 * KTable is good, the purpose of this class is only to add some default
 * functionality that ALL instances of the table will use. In other words, don't
 * be making this in to the original QTable monster....
 * 
 * @author O041484
 */
public class QKTable extends KTable {
	protected int m_SecondaryFocusRow = m_MainFocusRow;
	protected int m_SecondaryFocusCol = m_MainFocusCol;
	boolean alreadyScrollAddedListener = false;
	private final MouseMoveListener mouseMoveListener;

	/**
	 * Construct with some default style parameters to give us normal
	 * cursor/scroll functionality.
	 * 
	 * @param parent
	 */
	public QKTable(Composite parent) {
		this(parent, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWTX.EDIT_ON_KEY
				| SWTX.MARK_FOCUS_HEADERS);
	}

	/**
	 * Basic override of the super constructor.
	 * 
	 * @param parent
	 * @param style
	 */
	public QKTable(Composite parent, int style) {
		super(parent, style);

		Image crossCursor = SWTX.loadImageResource(getDisplay(),
				"/icons/cross_win32.gif");
		Image row_resizeCursor = SWTX.loadImageResource(getDisplay(),
				"/icons/row_resize_win32.gif");
		Image column_resizeCursor = SWTX.loadImageResource(getDisplay(),
				"/icons/column_resize_win32.gif");

		Rectangle crossBound = crossCursor.getBounds();
		Rectangle rowresizeBound = row_resizeCursor.getBounds();
		Rectangle columnresizeBound = column_resizeCursor.getBounds();

		Point crossSize = new Point(crossBound.width / 2, crossBound.height / 2);
		Point rowresizeSize = new Point(rowresizeBound.width / 2,
				rowresizeBound.height / 2);
		Point columnresizeSize = new Point(columnresizeBound.width / 2,
				columnresizeBound.height / 2);

		setDefaultCursor(new Cursor(getDisplay(), crossCursor.getImageData(),
				crossSize.x, crossSize.y), crossSize);
		setDefaultRowResizeCursor(new Cursor(getDisplay(),
				row_resizeCursor.getImageData(), rowresizeSize.x,
				rowresizeSize.y));
		setDefaultColumnResizeCursor(new Cursor(getDisplay(),
				column_resizeCursor.getImageData(), columnresizeSize.x,
				columnresizeSize.y));

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (m_MainFocusRow > m_Model.getRowCount() - 1
						&& arg0.stateMask != SWT.CTRL
						&& arg0.stateMask != SWT.SHIFT) {
					m_FocusRow = m_Model.getRowCount() - 1;
					m_MainFocusRow = m_Model.getRowCount() - 1;
					focusCell(m_MainFocusCol, m_Model.getRowCount() - 1, 0);
				}

				if (arg0.stateMask == SWT.CTRL) {
					if (arg0.keyCode == SWT.ARROW_RIGHT) {
						clearSelection();
						setSelection(m_Model.getColumnCount() - 1,
								m_MainFocusRow, true);
						focusCell(m_Model.getColumnCount() - 1, m_MainFocusRow,
								0);
					}
					if (arg0.keyCode == SWT.ARROW_LEFT) {
						clearSelection();
						int headerColCount = m_Model
								.getFixedHeaderColumnCount()
								+ m_Model.getFixedSelectableColumnCount();
						setSelection(headerColCount, m_MainFocusRow, true);
						focusCell(headerColCount, m_MainFocusRow, 0);
					}
					if (arg0.keyCode == SWT.ARROW_DOWN) {
						clearSelection();
						setSelection(m_MainFocusCol, m_Model.getRowCount() - 1,
								true);
						focusCell(m_MainFocusCol, m_Model.getRowCount() - 1, 0);
					}
					if (arg0.keyCode == SWT.ARROW_UP) {
						clearSelection();
						int rowHeaderCount = m_Model.getFixedHeaderRowCount()
								+ m_Model.getFixedSelectableRowCount();
						setSelection(m_MainFocusCol, rowHeaderCount, true);
						focusCell(m_MainFocusCol, rowHeaderCount, 0);
					}
				}

				if (arg0.character == ' ') {
					if (arg0.stateMask == SWT.CTRL) {
						int lastX = m_Model.getRowCount();
						List<Point> selection = new ArrayList<Point>();
						int start = Math.min(m_MainFocusCol, m_FocusCol);
						int end = Math.max(m_MainFocusCol, m_FocusCol);
						for (int i = 0; i < lastX; i++) {

							for (int j = start; j <= end; j++) {
								selection.add(new Point(j, i));
							}
						}
						clearSelection();
						setSelection(selection.toArray(new Point[0]), true);
						m_FocusRow = m_MainFocusRow = m_Model
								.getFixedHeaderRowCount()
								+ m_Model.getFixedSelectableRowCount();
						m_MainFocusCol = start;
						m_FocusCol = end;
					}

					if (arg0.stateMask == SWT.SHIFT) {
						int lastX = m_Model.getColumnCount();
						List<Point> selection = new ArrayList<Point>();
						int start = Math.min(m_MainFocusRow, m_FocusRow);
						int end = Math.max(m_MainFocusRow, m_FocusRow);

						for (int i = 0; i < lastX; i++) {
							for (int j = start; j <= end; j++) {
								selection.add(new Point(i, j));
							}
						}
						clearSelection();
						setSelection(selection.toArray(new Point[0]), true);
						m_FocusCol = m_MainFocusCol = m_Model
								.getFixedHeaderColumnCount()
								+ m_Model.getFixedSelectableColumnCount();
						m_MainFocusRow = start;
						m_FocusRow = end;
					}
					if (arg0.stateMask == (SWT.CTRL | SWT.SHIFT)) {
						List<Point> selection = new ArrayList<Point>();
						for (int i = 0; i < m_Model.getColumnCount(); i++) {
							for (int j = 0; j <= m_Model.getRowCount() - 1; j++) {
								selection.add(new Point(i, j));
							}
						}
						clearSelection();
						setSelection(selection.toArray(new Point[0]), true);
						m_MainFocusRow = m_Model.getFixedHeaderRowCount()
								+ m_Model.getFixedSelectableRowCount();
						m_MainFocusCol = m_Model.getFixedHeaderColumnCount()
								+ m_Model.getFixedSelectableColumnCount();
						m_FocusRow = m_Model.getRowCount() - 1;
						m_FocusCol = m_Model.getColumnCount() - 1;
					}
				}

				if (arg0.stateMask == (SWT.CTRL | SWT.SHIFT)) {
					if (arg0.keyCode == SWT.ARROW_RIGHT) {
						int columnCount = m_Model.getColumnCount();
						List<Point> selection = new ArrayList<Point>();
						for (int i = m_MainFocusCol; i < columnCount; i++) {
							int startRowIdx = Math.min(m_MainFocusRow,
									m_FocusRow);
							int endRowIdx = Math
									.max(m_MainFocusRow, m_FocusRow);

							for (int j = startRowIdx; j <= endRowIdx; j++) {
								selection.add(new Point(i, j));
							}
						}
						int tempFocusRow = m_FocusRow;
						clearSelection();
						setSelection(selection.toArray(new Point[0]), true);
						m_FocusCol = columnCount - 1;
						m_FocusRow = tempFocusRow;
					} else if (arg0.keyCode == SWT.ARROW_LEFT) {
						int firstDataCol = m_Model.getFixedHeaderColumnCount()
								+ m_Model.getFixedSelectableColumnCount();
						List<Point> selection = new ArrayList<Point>();
						for (int i = m_MainFocusCol; i >= firstDataCol; i--) {
							int startRowIdx = Math.min(m_MainFocusRow,
									m_FocusRow);
							int endRowIdx = Math
									.max(m_MainFocusRow, m_FocusRow);

							for (int j = startRowIdx; j <= endRowIdx; j++) {
								selection.add(new Point(i, j));
							}
						}
						int tempFocusRow = m_FocusRow;
						clearSelection();
						setSelection(selection.toArray(new Point[0]), true);
						m_FocusCol = firstDataCol;
						m_FocusRow = tempFocusRow;
					} else if (arg0.keyCode == SWT.ARROW_DOWN) {
						int rowCount = m_Model.getRowCount();
						List<Point> selection = new ArrayList<Point>();
						for (int i = m_MainFocusRow; i < rowCount; i++) {
							int startColIdx = Math.min(m_MainFocusCol,
									m_FocusCol);
							int endColIdx = Math
									.max(m_MainFocusCol, m_FocusCol);

							for (int j = startColIdx; j <= endColIdx; j++) {
								selection.add(new Point(j, i));
							}
						}
						int tempFocusCol = m_FocusCol;
						clearSelection();
						setSelection(selection.toArray(new Point[0]), true);
						m_FocusRow = rowCount - 1;
						m_FocusCol = tempFocusCol;
					} else if (arg0.keyCode == SWT.ARROW_UP) {
						int firstDataRow = m_Model.getFixedHeaderRowCount()
								+ m_Model.getFixedSelectableRowCount();
						List<Point> selection = new ArrayList<Point>();
						for (int i = m_MainFocusRow; i >= firstDataRow; i--) {
							int startColIdx = Math.min(m_MainFocusCol,
									m_FocusCol);
							int endColIdx = Math
									.max(m_MainFocusCol, m_FocusCol);

							for (int j = startColIdx; j <= endColIdx; j++) {
								selection.add(new Point(j, i));
							}
						}
						int tempFocusCol = m_FocusCol;
						clearSelection();
						setSelection(selection.toArray(new Point[0]), true);
						m_FocusRow = firstDataRow;
						m_FocusCol = tempFocusCol;
					}
				}

			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				QKTable.super.setFocus();
			}
		});

		mouseMoveListener = new MouseMoveListener() {
			private volatile MouseEvent e;

			@Override
			public void mouseMove(final MouseEvent e) {
				this.e = e;
				doit();
			}

			private void doit() {
				/**
				 * Only on selection
				 */
				if (m_Capture) {
					Point point = calcNonSpanColumnNum(e.x, e.y);

					Rectangle bounds = getBounds();
					Point tableSize = new Point(bounds.width, bounds.height);
					if (getVerticalBar() != null) {
						tableSize.x -= getVerticalBar().getSize().x;
					}
					if (getHorizontalBar() != null) {
						tableSize.y -= getHorizontalBar().getSize().y;
					}

					if ((point.x < 0 && point.y < 0)
							|| (!isCellFullyVisible(point.x, point.y) && e.x >= tableSize.x)) {
						// Mouse has moved outside the cells, time to find out
						// where we are
						int columnCount = getModel().getColumnCount();
						if (e.x >= tableSize.x && m_LeftColumn < columnCount) {
							int i = getVisibleCells().x
									+ getVisibleCells().width;
							if (i == columnCount) {
								redraw();
								return;
							}
							m_LeftColumn++;
						}
						if (e.y >= tableSize.y
								&& m_TopRow < getModel().getRowCount()) {
							m_TopRow++;
						}

						redraw();

						Display.getCurrent().asyncExec(new Runnable() {
							@Override
							public void run() {
								mouseMove(e);
							}
						});
					} else if (isHeaderCell(point.x, point.y)
							|| isFixedCell(point.x, point.y)) {
						KTableModel model = getModel();
						int headerRowCount = model.getFixedHeaderRowCount()
								+ model.getFixedSelectableRowCount();
						int headerColCount = model.getFixedHeaderColumnCount()
								+ model.getFixedSelectableColumnCount();

						if (point.y < headerRowCount
								&& m_TopRow >= model.getRowCount()) {
							m_TopRow--;
						}
						if (point.x < headerColCount
								&& m_LeftColumn >= model.getColumnCount()) {
							m_LeftColumn--;
						}
						redraw();

						Display.getCurrent().asyncExec(new Runnable() {
							@Override
							public void run() {
								mouseMove(e);
							}
						});
					}
				}
			}
		};

		// addMouseMoveListener(mouseMoveListener);
	}

	public void syncTable(final QKTable tableToSync, final int style) {
		if ((style & SWT.HORIZONTAL) == SWT.HORIZONTAL) {
			tableToSync.getHorizontalBar().addSelectionListener(
					new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							syncHorizontal(tableToSync);
						}
					});

			tableToSync.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent arg0) {
					if (arg0.keyCode == SWT.ARROW_LEFT
							|| arg0.keyCode == SWT.ARROW_RIGHT) {
						syncHorizontal(tableToSync);
					}
				}
			});
		}

		if ((style & SWT.VERTICAL) == SWT.VERTICAL) {
			tableToSync.getVerticalBar().addSelectionListener(
					new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							syncVertical(tableToSync);
						}
					});

			tableToSync.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent arg0) {
					if (arg0.keyCode == SWT.ARROW_UP
							|| arg0.keyCode == SWT.ARROW_DOWN) {
						syncVertical(tableToSync);
					}
				}
			});
		}

		tableToSync.addCellResizeListener(new KTableCellResizeListener() {
			@Override
			public void columnResized(int col, int newWidth) {
				m_Model.setColumnWidth(col, newWidth);
				redraw();
			}

			@Override
			public void rowResized(int row, int newHeight) {
				m_Model.setRowHeight(row, newHeight);
				redraw();
			}
		});

		// tableToSync.addMouseListener(new MouseMoveListener() {
		// public void mouseMove(MouseEvent e) {
		// if (e.stateMask == 524288) {
		// if ((style & SWT.HORIZONTAL) == SWT.HORIZONTAL) {
		// m_LeftColumn = tableToSync.m_LeftColumn;
		// }
		// if ((style & SWT.VERTICAL) == SWT.VERTICAL) {
		// m_TopRow = tableToSync.m_TopRow;
		// }
		// redraw();
		// }
		// }
		// });
	}

	private void syncHorizontal(final QKTable tableToSync) {
		m_LeftColumn = tableToSync.m_LeftColumn;
		redraw();
	}

	private void syncVertical(final QKTable tableToSync) {
		m_TopRow = tableToSync.m_TopRow;
		redraw();
	}

	@Override
	public void setModel(KTableModel model) {
		super.setModel(model);

		// TODO: This doesn't work because the data isn't available and the
		// columns resize to 0.
		// for (int i = 0; i < model.getColumnCount(); i++)
		// {
		// resizeColumnOptimal(i);
		// }
	}

	@Override
	protected void onKeyDown(KeyEvent e) {
		if (e.character == '-') {
			e.keyCode = 45;
		}
		if (e.character == '.') {
			e.keyCode = 46;
		}
		if (e.character == '/') {
			e.keyCode = 47;
		}
		if (e.character == '*') {
			e.keyCode = 56;
		}
		if (e.character == '+') {
			e.keyCode = 61;
		}
		if (e.keyCode == SWT.F2) {
			e.character = ' ';
		}
		if (e.character == ' '
				&& ((e.stateMask & SWT.CTRL) == SWT.CTRL || (e.stateMask & SWT.SHIFT) == SWT.SHIFT)) {
			return;
		}
		super.onKeyDown(e);

		if (e.character == SWT.CR) {
			CellEditorHelper.moveToNextCell(this, true);
		}
	}

	@Override
	protected void onPaint(PaintEvent event) {
		super.onPaint(event);

		ScrollBar sb = getHorizontalBar();

		if (sb != null && m_Model != null) {
			sb.setMinimum(getFixedColumnCount());
			sb.setMaximum(m_Model.getColumnCount());
			sb.setThumb(m_ColumnsFullyVisible);
			sb.setSelection(m_LeftColumn);
		}
	}

	@Override
	public void setSelection(int col, int row, boolean scroll) {
		checkWidget();

		if (col < m_Model.getColumnCount()
				&& col >= m_Model.getFixedHeaderColumnCount()
				&& row < m_Model.getRowCount()
				&& row >= m_Model.getFixedHeaderRowCount()) {
			focusCell(col, row, 0);
			if (scroll) {
				scrollToFocus();
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void focusCell(int col, int row, int stateMask) {
		// assure it is a valid cell:
		Point orig = new Point(col, row);
		Point valid = getValidCell(col, row);
		if (valid != null) {
			col = valid.x;
			row = valid.y;
		}

		GC gc = new GC(this);

		// close cell editor if active
		if (m_CellEditor != null) {
			m_CellEditor.close(true);
		}

		/*
		 * Special rule: in row selection mode the selection if a fixed cell in
		 * a non-fixed row is allowed and handled as a selection of a non-fixed
		 * cell.
		 */
		if (row >= m_Model.getFixedHeaderRowCount()
				&& (col >= m_Model.getFixedHeaderColumnCount() || isRowSelectMode())) {

			if ((stateMask & SWT.CTRL) == 0 && (stateMask & SWT.SHIFT) == 0) {
				// case: no modifier key
				boolean redrawAll = (m_Selection.size() > 1);
				int oldFocusRow = m_FocusRow;
				int oldFocusCol = m_FocusCol;

				clearSelectionWithoutRedraw();
				addToSelectionWithoutRedraw(col, row);
				m_FocusRow = row;
				m_FocusCol = col;
				m_MainFocusRow = row;
				m_MainFocusCol = col;

				if (redrawAll) {
					redraw();
				} else if (isRowSelectMode()) {
					if (oldFocusRow != -1) {
						drawRow(gc, oldFocusRow);
					}
					drawRow(gc, m_FocusRow);
				} else {
					Rectangle origClipping = null;
					if (!isFixedCell(oldFocusCol, oldFocusRow)) {
						origClipping = setContentAreaClipping(gc);
					}
					drawCell(gc, oldFocusCol, oldFocusRow);
					if (origClipping != null) {
						gc.setClipping(origClipping);
					}

					if (!isFixedCell(m_FocusCol, m_FocusRow)) {
						origClipping = setContentAreaClipping(gc);
					}
					drawCell(gc, m_FocusCol, m_FocusRow);
				}
				// notify non-fixed cell listeners
				fireCellSelection(orig.x, orig.y, stateMask);
			}

			else if ((stateMask & SWT.CTRL) != 0) {
				// case: CTRL key pressed
				boolean success = toggleSelection(col, row);
				if (success) {
					m_FocusCol = col;
					m_FocusRow = row;
				}

				if (isRowSelectMode()) {
					drawRow(gc, row);
				} else {
					drawCell(gc, col, row);
				}
				// notify non-fixed cell listeners
				if (success) {
					fireCellSelection(m_FocusCol, m_FocusRow, stateMask);
				}
			}

			else if ((stateMask & SWT.SHIFT) != 0) {
				// Ignore when not a multi-selection table.
				if (!isMultiSelectMode()) {
					if (isRowSelectMode()) {
						drawRow(gc, row);
					} else {
						drawCell(gc, col, row);
					}
					return;
				}
				// case: SHIFT key pressed
				if (isRowSelectMode()) {
					HashMap oldSelection = new HashMap(m_Selection);
					if (row < m_FocusRow) {
						// backword selection
						while (row != m_FocusRow) {
							addToSelectionWithoutRedraw(0, --m_FocusRow);
						}
					} else {
						// foreward selection
						while (row != m_FocusRow) {
							addToSelectionWithoutRedraw(0, ++m_FocusRow);
						}
					}
					if (!oldSelection.equals(m_Selection)) {
						oldSelection.putAll(m_Selection);
						Iterator rowIt = oldSelection.entrySet().iterator();
						int min = 0, max = 0;
						if (rowIt.hasNext()) {
							min = ((Integer) ((Entry) rowIt.next()).getValue())
									.intValue();
							max = min;
						}
						while (rowIt.hasNext()) {
							int r = ((Integer) ((Entry) rowIt.next())
									.getValue()).intValue();
							if (r < min) {
								min = r;
							}
							if (r > max) {
								max = r;
							}
						}
						redraw(0, min, m_Model.getColumnCount(), max - min + 1);

						// notify non-fixed cell listeners
						fireCellSelection(orig.x, orig.y, stateMask);
					}
				} else {// cell selection mode

					int currentSelCol = col;

					if (currentSelCol < col) {
						addToSelectionWithoutRedraw(col - 1, row);
					}

					Point[] sel = getCellSelection();
					Point min = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
					Point max = new Point(-1, -1);
					boolean containsCell = false;
					for (int i = 0; i < sel.length; i++) {
						if (sel[i].x > max.x) {
							max.x = sel[i].x;
						}
						if (sel[i].y > max.y) {
							max.y = sel[i].y;
						}
						if (sel[i].x < min.x) {
							min.x = sel[i].x;
						}
						if (sel[i].y < min.y) {
							min.y = sel[i].y;
						}
						if (!containsCell && sel[i].x == col && sel[i].y == row) {
							containsCell = true;
						}
					}

					if (col < m_MainFocusCol && max.x > m_MainFocusCol) {
						min.x = col;
						max.x = m_MainFocusCol;
					} else if (col > m_MainFocusCol && min.x < m_MainFocusCol) {
						min.x = m_MainFocusCol;
						max.x = col;
					}
					if (row < m_MainFocusRow && max.y > m_MainFocusRow) {
						min.y = row;
						max.y = m_MainFocusRow;
					} else if (row > m_MainFocusRow && min.y < m_MainFocusRow) {
						min.y = m_MainFocusRow;
						max.y = row;
					}

					HashMap oldSelection = new HashMap(m_Selection);
					if (containsCell) {
						clearSelectionWithoutRedraw();

						if (max.x == m_FocusCol) {
							max.x = col;
						}
						if (max.y == m_FocusRow) {
							max.y = row;
						}
						if (min.x == m_FocusCol) {
							min.x = col;
						}
						if (min.y == m_FocusRow) {
							min.y = row;
						}

						// set selection:
						for (int r = min.y; r <= max.y; r++) {
							for (int c = min.x; c <= max.x; c++) {
								addToSelectionWithoutRedraw(c, r);
							}
						}
						if (!oldSelection.equals(m_Selection)) {
							redraw();
							// notify non-fixed cell listeners
							fireCellSelection(orig.x, orig.y, stateMask);
						}
					} else {

						if (col > max.x) {
							max.x = col;
						}
						if (row > max.y) {
							max.y = row;
						}
						if (col < min.x) {
							min.x = col;
						}
						if (row < min.y) {
							min.y = row;
						}

						for (int r = min.y; r <= max.y; r++) {

							for (int c = min.x; c <= max.x; c++) {
								addToSelectionWithoutRedraw(c, r);
							}

						}

						// If this is multi selection mode and the highlight
						// selection header style bit is set then we want
						// to force a redraw of the fixed rows and columns.
						// This makes sure that the highlighting of those
						// fixed rows and columns happens.

						if (isMultiSelectMode()
								&& isHighlightSelectionInHeader()) {
							redraw(-1, row, 1, 1);
							redraw(col, -1, 1, 1);
						}

						if (!oldSelection.equals(m_Selection)) {
							redraw(m_LeftColumn, min.y, max.x - m_LeftColumn
									+ 1, max.y - min.y + 1);
							// notify non-fixed cell listeners
							fireCellSelection(orig.x, orig.y, stateMask);
						}
					}

					m_FocusRow = row;
					m_FocusCol = col;

				}
			}

		} else {
			// a fixed cell was focused
			drawCell(gc, col, row);
			// notify fixed cell listeners
			fireFixedCellSelection(orig.x, orig.y, stateMask);
		}

		gc.dispose();
	}

	private Rectangle setContentAreaClipping(GC gc) {
		Rectangle oldClipping = gc.getClipping();
		Rectangle contentClip = getClientArea();

		contentClip.x = 1;
		contentClip.y = 1;
		contentClip.width -= 1;
		contentClip.height -= 1;

		for (int i = 0; i < getFixedColumnCount(); i++) {
			int width = getCellRectIgnoreSpan(i, 0).width;
			contentClip.x += width + 1;
			contentClip.width -= width + 1;
		}

		for (int i = 0; i < getFixedRowCount(); i++) {
			int height = getCellRectIgnoreSpan(0, i).height;
			contentClip.y += height + 1;
			contentClip.height -= height + 1;
		}

		contentClip.intersect(oldClipping);
		gc.setClipping(contentClip);
		return oldClipping;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (mouseMoveListener != null) {
			removeMouseMoveListener(mouseMoveListener);
		}
	}
}
