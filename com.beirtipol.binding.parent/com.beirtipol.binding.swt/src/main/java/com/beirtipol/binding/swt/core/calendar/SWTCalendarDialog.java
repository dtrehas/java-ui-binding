package com.beirtipol.binding.swt.core.calendar;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SWTCalendarDialog {
	private final Shell shell;
	private final SWTCalendar swtcal;
	private final Display display;

	public SWTCalendarDialog(Display display) {
		this.display = display;
		shell = new Shell(display, SWT.APPLICATION_MODAL | SWT.CLOSE);
		shell.setLayout(new RowLayout());
		swtcal = new SWTCalendar(shell);
	}

	public void open() {
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public Calendar getCalendar() {
		return swtcal.getCalendar();
	}

	public void setDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		swtcal.setCalendar(calendar);
	}

	public void addDateChangedListener(SWTCalendarListener listener) {
		swtcal.addSWTCalendarListener(listener);
	}

	public static void main(String[] args) {
		Display display = new Display();
		new SWTCalendarDialog(display).open();

		display.dispose();
	}
}