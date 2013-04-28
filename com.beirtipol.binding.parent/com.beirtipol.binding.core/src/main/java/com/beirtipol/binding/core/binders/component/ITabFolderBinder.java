package com.beirtipol.binding.core.binders.component;

import java.util.List;

import com.beirtipol.binding.core.binders.IBasicBinder;
import com.beirtipol.binding.core.delegates.ITabFolderDelegate;

public interface ITabFolderBinder extends IBasicBinder<ITabFolderDelegate> {
	List<ITabItemBinder> getTabItemBinders();
}
