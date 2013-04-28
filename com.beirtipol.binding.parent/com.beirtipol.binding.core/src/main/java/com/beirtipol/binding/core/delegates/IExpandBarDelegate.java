package com.beirtipol.binding.core.delegates;

import java.util.Collection;

import com.beirtipol.binding.core.binders.component.IExpandItemBinder;

public interface IExpandBarDelegate extends IDelegate {
	void setExpandItemBinders(Collection<IExpandItemBinder> binders);
}