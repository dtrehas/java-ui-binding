package com.beirtipol.binding.swing.core.calendar;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.MaskFormatter;

/**
 * JTextFieldDateEditor is the default editor used by JDateChooser. It is a
 * formatted text field, that colores valid dates green/black and invalid dates
 * red. The date format patten and mask can be set manually. If not set, the
 * MEDIUM pattern of a SimpleDateFormat with regards to the actual locale is
 * used.
 * 
 * @author Kai Toedter
 * @version $LastChangedRevision: 97 $
 * @version $LastChangedDate: 2006-05-24 17:30:41 +0200 (Mi, 24 Mai 2006) $
 */
public class JTextFieldDateEditor extends JFormattedTextField implements IDateEditor, CaretListener, FocusListener, ActionListener {
	private static final long serialVersionUID = -8901842591101625304L;

	protected Date date;
	protected SimpleDateFormat dateFormatter;
	protected MaskFormatter maskFormatter;
	protected String datePattern;
	protected String maskPattern;
	protected char placeholder;
	protected Color darkGreen;
	protected DateUtil dateUtil;
	private boolean isMaskVisible;
	private boolean ignoreDatePatternChange;
	private int hours;
	private int minutes;
	private int seconds;
	private int millis;
	private final Calendar calendar;

	public JTextFieldDateEditor() {
		this(false, null, null, ' ');
	}

	public JTextFieldDateEditor(String datePattern, String maskPattern, char placeholder) {
		this(true, datePattern, maskPattern, placeholder);
	}

	public JTextFieldDateEditor(boolean showMask, String datePattern, String maskPattern, char placeholder) {
		dateFormatter = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.MEDIUM);
		dateFormatter.setLenient(false);

		setFont(new Font("Tahoma", Font.PLAIN, 11));

		setDateFormatString(datePattern);
		if (datePattern != null) {
			ignoreDatePatternChange = true;
		}

		this.placeholder = placeholder;

		if (maskPattern == null) {
			this.maskPattern = createMaskFromDatePattern(this.datePattern);
		} else {
			this.maskPattern = maskPattern;
		}

		setToolTipText(this.datePattern);
		setMaskVisible(showMask);

		addCaretListener(this);
		// addFocusListener(this);
		addActionListener(this);
		darkGreen = new Color(0, 150, 0);

		calendar = Calendar.getInstance();

		dateUtil = new DateUtil();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.toedter.calendar.IDateEditor#getDate()
	 */
	@Override
	public Date getDate() {
		try {
			calendar.setTime(dateFormatter.parse(getText()));
			calendar.set(Calendar.HOUR_OF_DAY, hours);
			calendar.set(Calendar.MINUTE, minutes);
			calendar.set(Calendar.SECOND, seconds);
			calendar.set(Calendar.MILLISECOND, millis);
			date = calendar.getTime();
		} catch (ParseException e) {
			date = null;
		}
		return date;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.toedter.calendar.IDateEditor#setDate(java.util.Date)
	 */
	@Override
	public void setDate(Date date) {
		setDate(date, true);
	}

	/**
	 * Sets the date.
	 * 
	 * @param date
	 *            the date
	 * @param firePropertyChange
	 *            true, if the date property should be fired.
	 */
	protected void setDate(Date date, boolean firePropertyChange) {
		Date oldDate = this.date;
		this.date = date;

		if (date == null) {
			// setText("");
		}

		else {
			calendar.setTime(date);
			hours = calendar.get(Calendar.HOUR_OF_DAY);
			minutes = calendar.get(Calendar.MINUTE);
			seconds = calendar.get(Calendar.SECOND);
			millis = calendar.get(Calendar.MILLISECOND);

			String formattedDate = dateFormatter.format(date);
			try {
				setText(formattedDate);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		if (date != null && dateUtil.checkDate(date)) {
			setForeground(Color.BLACK);

		}

		if (firePropertyChange) {
			firePropertyChange("date", oldDate, date);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.toedter.calendar.IDateEditor#setDateFormatString(java.lang.String)
	 */
	@Override
	public void setDateFormatString(String dateFormatString) {
		if (ignoreDatePatternChange) {
			return;
		}

		try {
			dateFormatter.applyPattern(dateFormatString);
		} catch (RuntimeException e) {
			dateFormatter = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.MEDIUM);
			dateFormatter.setLenient(false);
		}
		this.datePattern = dateFormatter.toPattern();
		setToolTipText(this.datePattern);
		setDate(date, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.toedter.calendar.IDateEditor#getDateFormatString()
	 */
	@Override
	public String getDateFormatString() {
		return datePattern;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.toedter.calendar.IDateEditor#getUiComponent()
	 */
	@Override
	public JComponent getUiComponent() {
		return this;
	}

	/**
	 * After any user input, the value of the textfield is proofed. Depending on
	 * being a valid date, the value is colored green or red.
	 * 
	 * @param event
	 *            the caret event
	 */
	@Override
	public void caretUpdate(CaretEvent event) {
		// String text = getText().trim();
		// String emptyMask = maskPattern.replace('#', placeholder);
		//
		// if (text.length() == 0 || text.equals(emptyMask))
		// {
		// setForeground(Color.BLACK);
		// return;
		// }
		//
		// try
		// {
		// Date date = DtDate.parse(text);
		// // Date date = dateFormatter.parse(getText());
		// if (dateUtil.checkDate(date))
		// {
		// setForeground(darkGreen);
		// }
		// else
		// {
		// setForeground(Color.RED);
		// }
		// }
		// catch(Exception e)
		// {
		// setForeground(Color.RED);
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent focusEvent) {
		checkText();
	}

	private void checkText() {
		//
		// String dateStr = getText();
		//
		// if (ModelFunctions.isFunction(getText()))
		// {
		// dateStr = (String) ModelFunctions.executeFunction(dateStr);
		// }
		//
		// try
		// {
		// setForeground(Color.BLACK);
		// // Date date = dateFormatter.parse(getText());
		// Date date = DtDate.parse(dateStr);
		// setDate(date, true);
		// }
		// catch(Exception e)
		// {
		// setForeground(Color.RED);
		// setDate(null, true);
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(Locale locale) {
		if (locale == getLocale() || ignoreDatePatternChange) {
			return;
		}

		super.setLocale(locale);
		dateFormatter = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
		setToolTipText(dateFormatter.toPattern());

		setDate(date, false);
		doLayout();
	}

	/**
	 * Creates a mask from a date pattern. This is a very simple (and
	 * incomplete) implementation thet works only with numbers. A date pattern
	 * of "MM/dd/yy" will result in the mask "##/##/##". Probably you want to
	 * override this method if it does not fit your needs.
	 * 
	 * @param datePattern
	 *            the date pattern
	 * @return the mask
	 */
	public String createMaskFromDatePattern(String datePattern) {
		String symbols = "GyMdkHmsSEDFwWahKzZ";
		String mask = "";
		for (int i = 0; i < datePattern.length(); i++) {
			char ch = datePattern.charAt(i);
			boolean symbolFound = false;
			for (int n = 0; n < symbols.length(); n++) {
				if (symbols.charAt(n) == ch) {
					mask += "#";
					symbolFound = true;
					break;
				}
			}
			if (!symbolFound) {
				mask += ch;
			}
		}
		return mask;
	}

	/**
	 * Returns true, if the mask is visible.
	 * 
	 * @return true, if the mask is visible
	 */
	public boolean isMaskVisible() {
		return isMaskVisible;
	}

	/**
	 * Sets the mask visible.
	 * 
	 * @param isMaskVisible
	 *            true, if the mask should be visible
	 */
	public void setMaskVisible(boolean isMaskVisible) {
		this.isMaskVisible = isMaskVisible;
		if (isMaskVisible) {
			if (maskFormatter == null) {
				try {
					maskFormatter = new MaskFormatter(createMaskFromDatePattern(datePattern));
					maskFormatter.setPlaceholderCharacter(this.placeholder);
					maskFormatter.install(this);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Validates the typed date and sets it (only if it is valid).
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		checkText();
	}

	/**
	 * Enables and disabled the compoment. It also fixes the background bug
	 * 4991597 and sets the background explicitely to a
	 * TextField.inactiveBackground.
	 */
	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		if (!b) {
			super.setBackground(UIManager.getColor("TextField.inactiveBackground"));
		} else {
			super.setBackground(UIManager.getColor("TextField.background"));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.toedter.calendar.IDateEditor#getMaxSelectableDate()
	 */
	@Override
	public Date getMaxSelectableDate() {
		return dateUtil.getMaxSelectableDate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.toedter.calendar.IDateEditor#getMinSelectableDate()
	 */
	@Override
	public Date getMinSelectableDate() {
		return dateUtil.getMinSelectableDate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.toedter.calendar.IDateEditor#setMaxSelectableDate(java.util.Date)
	 */
	@Override
	public void setMaxSelectableDate(Date max) {
		dateUtil.setMaxSelectableDate(max);
		checkText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.toedter.calendar.IDateEditor#setMinSelectableDate(java.util.Date)
	 */
	@Override
	public void setMinSelectableDate(Date min) {
		dateUtil.setMinSelectableDate(min);
		checkText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.toedter.calendar.IDateEditor#setSelectableDateRange(java.util.Date,
	 * java.util.Date)
	 */
	@Override
	public void setSelectableDateRange(Date min, Date max) {
		dateUtil.setSelectableDateRange(min, max);
		checkText();
	}

	/**
	 * Creates a JFrame with a JCalendar inside and can be used for testing.
	 * 
	 * @param s
	 *            The command line arguments
	 * @throws UnsupportedLookAndFeelException
	 */
	public static void main(String[] s) throws UnsupportedLookAndFeelException {
		JFrame frame = new JFrame("JTextFieldDateEditor");
		JTextFieldDateEditor jTextFieldDateEditor = new JTextFieldDateEditor();
		jTextFieldDateEditor.setDate(new Date());
		frame.getContentPane().add(jTextFieldDateEditor);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * DA. Have had to (ridiculously) override this method for the QTrade
	 * design. Issues are: Binders can cause exceptions to be thrown on the EDT.
	 * Binders may not have a reference to the source of an exception so how do
	 * we gracefully handle an EDT exception. This (hack) of overriding an
	 * "EDT run method" can do something, but it is almost guessing as to what
	 * to do. Exceptions can be thrown more than once in a row as binder methods
	 * can be for some reason called more than once in a row.
	 */
	@Override
	protected void processEvent(AWTEvent awtEvent) {
		try {
			super.processEvent(awtEvent);
		} catch (Exception e) {
			e.printStackTrace();
			checkText();
		}
	}
}