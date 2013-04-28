package com.beirtipol.binding.core.binders.widget;

import java.awt.Color;

import com.beirtipol.binding.core.binders.AbstractBinder;
import com.beirtipol.binding.core.binders.IColorModel;
import com.beirtipol.binding.core.delegates.IDelegate;

public abstract class AbstractWidgetBinder<Delegate extends IDelegate> extends
		AbstractBinder<Delegate> {
	protected IColorModel colorModel;

	public IColorModel getColorModel() {
		return colorModel;
	}

	public void setColorModel(IColorModel colorModel) {
		this.colorModel = colorModel;
	}

	public Color getForeground() {
		if (colorModel != null) {
			return colorModel.getForeground();
		}
		return null;
	}

	public Color getBackground() {
		if (colorModel != null) {
			return colorModel.getBackground();
		}
		return null;
	}
}
