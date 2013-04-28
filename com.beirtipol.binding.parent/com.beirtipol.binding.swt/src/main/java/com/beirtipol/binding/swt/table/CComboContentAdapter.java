package com.beirtipol.binding.swt.table;

import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

/**
 * Copied from ComboContentAdapter to support CCombo because some idiot in
 * eclipse thought it would be a good idea to have CCombo and Combo share no
 * common 'combo' interface.
 * 
 * @author O041484
 */
public class CComboContentAdapter implements IControlContentAdapter {

	@Override
	public String getControlContents(Control control) {
		return ((CCombo) control).getText();
	}

	@Override
	public void setControlContents(Control control, String text,
			int cursorPosition) {
		((CCombo) control).setText(text);
		((CCombo) control).setSelection(new Point(cursorPosition,
				cursorPosition));
	}

	@Override
	public void insertControlContents(Control control, String text,
			int cursorPosition) {
		CCombo combo = (CCombo) control;
		String contents = combo.getText();
		Point selection = combo.getSelection();
		StringBuffer sb = new StringBuffer();
		sb.append(contents.substring(0, selection.x));
		sb.append(text);
		if (selection.y < contents.length()) {
			sb.append(contents.substring(selection.y, contents.length()));
		}
		combo.setText(sb.toString());
		selection.x = selection.x + cursorPosition;
		selection.y = selection.x;
		combo.setSelection(selection);
	}

	@Override
	public int getCursorPosition(Control control) {
		return ((CCombo) control).getSelection().x;
	}

	@Override
	public Rectangle getInsertionBounds(Control control) {
		// This doesn't take horizontal scrolling into affect.
		// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=204599
		CCombo combo = (CCombo) control;
		int position = combo.getSelection().y;
		String contents = combo.getText();
		GC gc = new GC(combo);
		gc.setFont(combo.getFont());
		Point extent = gc.textExtent(contents.substring(0,
				Math.min(position, contents.length())));
		gc.dispose();
		return new Rectangle(extent.x, 0, 1, combo.getSize().y);
	}

	@Override
	public void setCursorPosition(Control control, int index) {
		((CCombo) control).setSelection(new Point(index, index));
	}

	public Point getSelection(Control control) {
		return ((CCombo) control).getSelection();
	}

	public void setSelection(Control control, Point range) {
		((CCombo) control).setSelection(range);
	}

}