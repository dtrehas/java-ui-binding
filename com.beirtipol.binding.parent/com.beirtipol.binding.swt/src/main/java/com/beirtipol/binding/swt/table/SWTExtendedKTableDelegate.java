package com.beirtipol.binding.swt.table;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.binders.table.IExtendedTableBinder;
import com.beirtipol.binding.core.binders.widget.IButtonBinder;
import com.beirtipol.binding.core.binders.widget.ICheckBoxBinder;
import com.beirtipol.binding.core.binders.widget.IComboBinder;
import com.beirtipol.binding.core.binders.widget.ITextBinder;
import com.beirtipol.binding.swt.util.ColorConverter;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.editors.KTableCellEditorCheckbox;
import de.kupzog.ktable.editors.KTableCellEditorText2;
import de.kupzog.ktable.renderers.CheckableCellRenderer;
import de.kupzog.ktable.renderers.DefaultCellRenderer;
import de.kupzog.ktable.renderers.FixedCellRenderer;
import de.kupzog.ktable.renderers.TextCellRenderer;

public class SWTExtendedKTableDelegate extends SWTKTableDelegate {

	private final IExtendedTableBinder myExtendedTableBinder;

	public SWTExtendedKTableDelegate(KTable myTable,
			IExtendedTableBinder myTableBinder) {
		super(myTable, myTableBinder);
		myExtendedTableBinder = myTableBinder;
	}

	@Override
	public KTableCellEditor doGetCellEditor(int col, int row) {
		CellType cellType = getCellType(col, row);
		if (cellType == CellType.DATA) {
			Point point = createRelativePointFromAbs(cellType, col, row);
			final IBasicBinder<?> binder = myExtendedTableBinder
					.getDataCellBinder(point.x, point.y);
			if (binder instanceof ITextBinder) {
				if (((ITextBinder) binder).isEnabled()) {
					return new KTableCellEditorText2() {
						@Override
						public void open(KTable table, int col, int row,
								Rectangle rect) {
							super.open(table, col, row, rect);
							m_Text.setText(((ITextBinder) binder)
									.getTextFromModel());
							m_Text.selectAll();
							m_Text.setVisible(true);
							m_Text.setFocus();
						}
					};
				}
			} else if (binder instanceof ICheckBoxBinder) {
				return new KTableCellEditorCheckbox();
			} else if (binder instanceof IButtonBinder) {
				return new ButtonCellEditor();
			} else if (binder instanceof IComboBinder) {
				return new ComboCellEditor((IComboBinder) binder);
			}
		}
		return super.doGetCellEditor(col, row);
	}

	@Override
	public KTableCellRenderer doGetCellRenderer(int col, int row) {
		// Table can begin to render before the constructor has finished while
		// running in RAP mode.
		if (myExtendedTableBinder != null) {
			CellType cellType = getCellType(col, row);
			if (cellType == CellType.DATA) {
				Point point = createRelativePointFromAbs(cellType, col, row);
				final IBasicBinder<?> binder = myExtendedTableBinder
						.getDataCellBinder(point.x, point.y);
				if (binder instanceof ICheckBoxBinder) {
					return new CheckableCellRenderer(
							DefaultCellRenderer.INDICATION_CLICKED
									| DefaultCellRenderer.INDICATION_FOCUS);
				} else if (binder instanceof ITextBinder) {
					if (((ITextBinder) binder).isEnabled()) {
						TextCellRenderer result = new TextCellRenderer(
								DefaultCellRenderer.STYLE_FLAT
										| DefaultCellRenderer.INDICATION_FOCUS);
						result.setBackground(ColorConverter
								.convert(((ITextBinder) binder).getBackground()));
						result.setForeground(ColorConverter
								.convert(((ITextBinder) binder).getForeground()));
						return result;
					} else {
						return new FixedCellRenderer(
								DefaultCellRenderer.STYLE_FLAT
										| DefaultCellRenderer.INDICATION_FOCUS);
					}
				} else if (binder instanceof IComboBinder) {
					if (((IComboBinder) binder).isEnabled()) {
						TextCellRenderer result = new TextCellRenderer(
								DefaultCellRenderer.STYLE_FLAT
										| DefaultCellRenderer.INDICATION_FOCUS);
						result.setBackground(ColorConverter
								.convert(((IComboBinder) binder)
										.getBackground()));
						result.setForeground(ColorConverter
								.convert(((IComboBinder) binder)
										.getForeground()));
						return result;
					} else {
						return new FixedCellRenderer(
								DefaultCellRenderer.STYLE_FLAT
										| DefaultCellRenderer.INDICATION_FOCUS);
					}
				} else if (binder instanceof IButtonBinder) {
					ButtonCellRenderer result = new ButtonCellRenderer(
							binder.isEnabled());
					return result;
				}
			}
		}
		return super.doGetCellRenderer(col, row);
	}

}
