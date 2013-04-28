package com.beirtipol.binding.core.delegates;

import java.util.Collection;

import com.beirtipol.binding.core.binders.component.ITabItemBinder;

public interface ITabFolderDelegate extends IDelegate {
	void setTabItemBinders(Collection<ITabItemBinder> binders);
}
