package com.beirtipol.binding.core.binders.table;

import java.awt.Point;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.binders.widget.IButtonBinder;
import com.beirtipol.binding.core.binders.widget.ICheckBoxBinder;
import com.beirtipol.binding.core.binders.widget.IComboBinder;
import com.beirtipol.binding.core.binders.widget.ITextBinder;

public abstract class AbstractExtendedTableBinder extends AbstractTableBinder
		implements IExtendedTableBinder {

	@Override
	public void setValue(int col, int row, Object value) {
		IBasicBinder<?> dataCellBinder = getDataCellBinder(col, row);
		if (dataCellBinder instanceof ITextBinder) {
			((ITextBinder) dataCellBinder).setTextIntoModel(ObjectUtils
					.toString(value));
		} else if (dataCellBinder instanceof ICheckBoxBinder) {
			((ICheckBoxBinder) dataCellBinder).setCheckedIntoModel(Boolean
					.parseBoolean(ObjectUtils.toString(value)));
		} else if (dataCellBinder instanceof IButtonBinder) {
			((IButtonBinder) dataCellBinder).pressed();
		} else if (dataCellBinder instanceof IComboBinder) {
			((IComboBinder) dataCellBinder).setSelectedItem(value);
		}
		updateCell(col, row);
	}

	@Override
	public void setValue(List<Point> cells, Object value) {
		for (Point point : cells) {
			setValue(point.x, point.y, value);
		}
	}

	@Override
	public Object getValue(int col, int row) {
		IBasicBinder<?> dataCellBinder = getDataCellBinder(col, row);
		if (dataCellBinder instanceof ITextBinder) {
			return ((ITextBinder) dataCellBinder).getTextFromModel();
		} else if (dataCellBinder instanceof IComboBinder) {
			return ((IComboBinder) dataCellBinder).getSelectedItem();
		} else if (dataCellBinder instanceof ICheckBoxBinder) {
			return ((ICheckBoxBinder) dataCellBinder).getCheckedFromModel();
		} else if (dataCellBinder instanceof IButtonBinder) {
			// TODO: Review this if we start using Toggle Buttons
			return true;
		}
		return "";
	}
}