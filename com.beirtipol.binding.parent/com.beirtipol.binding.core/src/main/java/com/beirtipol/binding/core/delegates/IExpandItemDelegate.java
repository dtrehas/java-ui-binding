package com.beirtipol.binding.core.delegates;

import com.beirtipol.binding.core.binders.component.IExpandItemBinder;

public interface IExpandItemDelegate extends IDelegate {
	void setExpanded(boolean expanded);

	void setTitle(String title);

	void addExpandListener(IExpandItemBinder binder);

	void setToolTipText(String text);

	void setSelected(boolean selected);

	void setVisible(boolean visible);
}
