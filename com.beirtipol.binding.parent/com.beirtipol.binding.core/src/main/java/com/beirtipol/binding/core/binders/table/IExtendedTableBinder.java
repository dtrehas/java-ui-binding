package com.beirtipol.binding.core.binders.table;

import com.beirtipol.binding.core.binders.IBasicBinder;

/**
 * Advanced implementation of the TableBinder, providing support for using
 * PresentationModels to represent rows/columns in a table.
 * 
 * @author O041484
 */
public interface IExtendedTableBinder extends ITableBinder {
	IBasicBinder<?> getDataCellBinder(int col, int row);
}
