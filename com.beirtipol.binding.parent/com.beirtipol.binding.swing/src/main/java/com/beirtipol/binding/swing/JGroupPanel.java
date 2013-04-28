package com.beirtipol.binding.swing;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class JGroupPanel extends JPanel {
	public JGroupPanel() {
		this("");
	}

	public JGroupPanel(String title) {
		super();
		setBackground(Color.WHITE);
		setDoubleBuffered(true);
		setBorder(BorderFactory.createTitledBorder(null, title,
				TitledBorder.LEFT,
				javax.swing.border.TitledBorder.DEFAULT_POSITION));
	}

	public void setTitle(String title) {
		TitledBorder border = (TitledBorder) getBorder();
		border.setTitle(title);
	}
}