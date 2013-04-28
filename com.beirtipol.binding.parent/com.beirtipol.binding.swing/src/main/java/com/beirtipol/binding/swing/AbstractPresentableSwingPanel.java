package com.beirtipol.binding.swing;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JPanel;

import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.layout.UnitValue;
import net.miginfocom.swing.MigLayout;

import com.beirtipol.binding.core.binders.IPresenter;

public abstract class AbstractPresentableSwingPanel<P extends IPresenter> {
	private JComponent cached;

	public abstract void setPresenter(P presenter);

	protected abstract JComponent createSwingComponent();

	public void dispose() {
		cached = null;
	}

	public JComponent getSwingComponent() {
		if (cached == null) {
			cached = createSwingComponent();
		}
		return cached;
	}

	public JPanel createNewSwingPanel() {
		PlatformDefaults.setDefaultHorizontalUnit(0);
		PlatformDefaults.setDefaultVerticalUnit(0);
		UnitValue defaultValue = new UnitValue((float) 1.0);
		PlatformDefaults.setGridCellGap(defaultValue, defaultValue);
		PlatformDefaults.setUnrelatedGap(defaultValue, defaultValue);
		PlatformDefaults.setRelatedGap(defaultValue, defaultValue);
		PlatformDefaults.setIndentGap(defaultValue, defaultValue);
		PlatformDefaults.setDialogInsets(defaultValue, defaultValue,
				defaultValue, defaultValue);
		JPanel panel = new JPanel();
		// Beirti added hidemode 3 as it's what we use everywhere

		panel.setLayout(new MigLayout(
				"insets 0 0 0 0, hidemode 3, gap 1px 1px, novisualpadding"));
		panel.setBackground(Color.WHITE);
		panel.setDoubleBuffered(true);
		return panel;
	}

}
