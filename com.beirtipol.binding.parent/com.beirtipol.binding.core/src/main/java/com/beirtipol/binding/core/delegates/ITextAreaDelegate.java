package com.beirtipol.binding.core.delegates;

import java.awt.Color;

import com.beirtipol.binding.core.binders.widget.ITextAreaBinder;

public interface ITextAreaDelegate extends IDelegate {
	public void setText(String string);

	public void setEnabled(boolean enabled);

	public void addModifyListener(ITextAreaBinder binder);

	public void addFocusListener(final ITextAreaBinder binder);

	public void addTraverseListener(final ITextAreaBinder binder);

	public void setBackground(Color color);

	public void setForeground(Color color);

	public void setToolTip(String tip);

	public void setVisible(boolean visible);
}