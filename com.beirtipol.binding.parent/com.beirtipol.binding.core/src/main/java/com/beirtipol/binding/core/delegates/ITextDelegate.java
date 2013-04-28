package com.beirtipol.binding.core.delegates;

import com.beirtipol.binding.core.binders.widget.ITextBinder;

public interface ITextDelegate extends IWidgetDelegate {
	void setText(String string);

	void addModifyListener(ITextBinder binder);

	void addFocusListener(final ITextBinder binder);

	void addTraverseListener(final ITextBinder binder);

}