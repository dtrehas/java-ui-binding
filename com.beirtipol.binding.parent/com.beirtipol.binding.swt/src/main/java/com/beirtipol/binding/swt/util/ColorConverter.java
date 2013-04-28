package com.beirtipol.binding.swt.util;

public class ColorConverter {
	public static java.awt.Color convert(org.eclipse.swt.graphics.Color swtColor) {
		if (swtColor == null) {
			return null;
		}
		java.awt.Color awtColor = new java.awt.Color(swtColor.getRed(),
				swtColor.getGreen(), swtColor.getBlue());
		return awtColor;
	}

	public static org.eclipse.swt.graphics.Color convert(java.awt.Color awtColor) {
		if (awtColor == null) {
			return null;
		}
		return SWTResourceManager.getColor(awtColor.getRed(),
				awtColor.getGreen(), awtColor.getBlue());
	}
}