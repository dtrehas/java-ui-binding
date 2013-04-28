package com.beirtipol.binding.core.binders.table;

import java.util.Map;

import com.beirtipol.binding.core.binders.IBasicBinder;

public interface ITabularPresenter {
	public Map<String, IBasicBinder<?>> getVisibleCellBinders();
}
