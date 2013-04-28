package com.beirtipol.binding.core.delegates;

import java.awt.Color;

public interface IWidgetDelegate extends IDelegate {
	void setBackground(Color color);

	void setForeground(Color color);

	void setEnabled(boolean enabled);

	void setVisible(boolean visible);

	void setToolTip(String text);
}