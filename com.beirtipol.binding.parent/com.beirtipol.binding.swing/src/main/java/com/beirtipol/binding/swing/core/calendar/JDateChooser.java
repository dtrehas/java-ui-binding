package com.beirtipol.binding.swing.core.calendar;

import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;

import com.beirtipol.binding.swing.util.GraphicsDeviceUtil;

/**
 * A date chooser containing a date editor and a button, that makes a JCalendar
 * visible for choosing a date. If no date editor is specified, a
 * JTextFieldDateEditor is used as default.
 * 
 * @author David Ainslie
 */
public class JDateChooser extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;

	private Date originalDate;
	private IDateEditor dateEditor;
	private JButton calendarButton;
	private JDialog dialog;
	private JCalendar jCalendar;

	private boolean useTableCellLayout = false;

	/**
	 * Creates a new JDateChooser. By default, no date is set and the textfield
	 * is empty.
	 */
	public JDateChooser() {
		this(null, "dd-MMM-yyyy", null, false);
	}

	/**
	 * @param useTableCellLayout
	 */
	public JDateChooser(boolean useTableCellLayout) {
		this(null, "dd-MMM-yyyy", null, useTableCellLayout);
	}

	/**
	 * Creates a new JDateChooser with given IDateEditor.
	 * 
	 * @param dateEditor
	 *            the dateEditor to be used used to display the date. if null, a
	 *            JTextFieldDateEditor is used.
	 */
	public JDateChooser(IDateEditor dateEditor) {
		this(null, null, dateEditor, false);
	}

	/**
	 * Creates a new JDateChooser.
	 * 
	 * @param date
	 *            the date or null
	 */
	public JDateChooser(Date date) {
		this(date, null);
	}

	/**
	 * Creates a new JDateChooser.
	 * 
	 * @param date
	 *            the date or null
	 * @param dateFormatString
	 *            the date format string or null (then MEDIUM SimpleDateFormat
	 *            format is used)
	 */
	public JDateChooser(Date date, String dateFormatString) {
		this(date, dateFormatString, null, false);
	}

	/**
	 * Creates a new JDateChooser. If the JDateChooser is created with this
	 * constructor, the mask will be always visible in the date editor. Please
	 * note that the date pattern and the mask will not be changed if the locale
	 * of the JDateChooser is changed.
	 * 
	 * @param datePattern
	 *            the date pattern, e.g. "MM/dd/yy"
	 * @param maskPattern
	 *            the mask pattern, e.g. "##/##/##"
	 * @param placeholder
	 *            the placeholer charachter, e.g. '_'
	 */
	public JDateChooser(String datePattern, String maskPattern, char placeholder) {
		this(null, datePattern, new JTextFieldDateEditor(datePattern,
				maskPattern, placeholder), false);
	}

	/**
	 * Creates a new JDateChooser.
	 * 
	 * @param date
	 *            the date or null
	 * @param dateFormatString
	 *            the date format string or null (then MEDIUM Date format is
	 *            used)
	 * @param dateEditor
	 *            the dateEditor to be used used to display the date. if null, a
	 *            JTextFieldDateEditor is used.
	 */
	public JDateChooser(Date date, String dateFormatString,
			IDateEditor dateEditor, boolean useTableCellLayout) {
		this.useTableCellLayout = useTableCellLayout;
		setName("JDateChooser");

		if (useTableCellLayout) {
			// BoxLayout instead of MigLayout here as it's provides the correct
			// layout when the
			// component's displayed in a table cell.
			setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		} else {
			setLayout(new MigLayout("gap 0, fillx, hidemode 3"));
		}
		// setBorder(BorderFactory.createEtchedBorder());
		createDateEditor(date, dateFormatString, dateEditor);
		createCalendarButton();
	}

	/**
	 * Listens for a "date" property change or a "day" property change event
	 * from the JCalendar - Updates the date editor.
	 * 
	 * @param propertyChangeEvent
	 */
	@Override
	public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		if (propertyChangeEvent.getPropertyName().equals("enabled")) {
			Boolean newValue = (Boolean) propertyChangeEvent.getNewValue();

			if (newValue) {
				calendarButton.setEnabled(true);
			} else {
				calendarButton.setEnabled(false);
			}
		} else if (propertyChangeEvent.getPropertyName().equals("day")) {
			setDate(jCalendar.getCalendar().getTime());
			commitDate();
		} else if (propertyChangeEvent.getPropertyName().equals("month")) {
			setDate(jCalendar.getCalendar().getTime());
		} else if (propertyChangeEvent.getPropertyName().equals("year")) {
			setDate(jCalendar.getCalendar().getTime());
		} else if (propertyChangeEvent.getPropertyName().equals("date")) {
			if (propertyChangeEvent.getSource() == dateEditor) {
				firePropertyChange("date", propertyChangeEvent.getOldValue(),
						propertyChangeEvent.getNewValue());
			} else {
				setDate((Date) propertyChangeEvent.getNewValue());
			}
		}
	}

	/**
	 * @see javax.swing.JComponent#addNotify()
	 */
	@Override
	public void addNotify() {
		setBackground(getParent().getBackground());
		super.addNotify();
	}

	/**
	 * Enable or disable the JDateChooser.
	 * 
	 * @param enabled
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		if (dateEditor != null) {
			dateEditor.setEnabled(enabled);
			calendarButton.setEnabled(enabled);
		}
	}

	/**
	 * Sets the font of all subcomponents.
	 * 
	 * @param font
	 */
	@Override
	public void setFont(Font font) {
		if (dateEditor != null) {
			dateEditor.getUiComponent().setFont(font);

			if (jCalendar != null) {
				jCalendar.setFont(font);
			}

			super.setFont(font);
		}
	}

	/**
	 * Sets the locale.
	 * 
	 * @param locale
	 */
	@Override
	public void setLocale(Locale locale) {
		super.setLocale(locale);
		dateEditor.setLocale(locale);
		jCalendar.setLocale(locale);
	}

	/**
	 * Sets the date. Fires the property change "date" if date != null.
	 * 
	 * @param date
	 */
	public void setDate(Date date) {
		dateEditor.setDate(date);

		if (getParent() != null) {
			getParent().invalidate();
		}
	}

	/**
	 * Returns the date. If the JDateChooser is started with a null date and no
	 * date was set by the user, null is returned.
	 * 
	 * @return the current date
	 */
	public Date getDate() {
		return dateEditor.getDate();
	}

	/**
	 * Sets the icon of the buuton.
	 * 
	 * @param icon
	 *            the new icon
	 */
	public void setIcon(ImageIcon icon) {
		calendarButton.setIcon(icon);
	}

	/**
	 * Returns the JCalendar component. This is usefull if you want to set some
	 * properties.
	 * 
	 * @return JCalendar
	 */
	public JCalendar getJCalendar() {
		return jCalendar;
	}

	/**
	 * Returns the calendar button.
	 * 
	 * @return JButton
	 */
	public JButton getCalendarButton() {
		return calendarButton;
	}

	/**
	 * Returns the date editor.
	 * 
	 * @return IDateEditor
	 */
	public IDateEditor getDateEditor() {
		return dateEditor;
	}

	/**
	 * Sets a valid date range for selectable dates. If max is before min, the
	 * default range with no limitation is set.
	 * 
	 * @param min
	 *            the minimum selectable date or null (then the minimum date is
	 *            set to 01\01\0001)
	 * @param max
	 *            the maximum selectable date or null (then the maximum date is
	 *            set to 01\01\9999)
	 */
	public void setSelectableDateRange(Date min, Date max) {
		jCalendar.setSelectableDateRange(min, max);
		dateEditor.setSelectableDateRange(jCalendar.getMinSelectableDate(),
				jCalendar.getMaxSelectableDate());
	}

	/**
	 * @param max
	 */
	public void setMaxSelectableDate(Date max) {
		jCalendar.setMaxSelectableDate(max);
		dateEditor.setMaxSelectableDate(max);
	}

	/**
	 * @param min
	 */
	public void setMinSelectableDate(Date min) {
		jCalendar.setMinSelectableDate(min);
		dateEditor.setMinSelectableDate(min);
	}

	/**
	 * Gets the maximum selectable date.
	 * 
	 * @return the maximum selectable date
	 */
	public Date getMaxSelectableDate() {
		return jCalendar.getMaxSelectableDate();
	}

	/**
	 * Gets the minimum selectable date.
	 * 
	 * @return the minimum selectable date
	 */
	public Date getMinSelectableDate() {
		return jCalendar.getMinSelectableDate();
	}

	/**
	 * @return
	 */
	private Calendar createCalendar() {
		Calendar calendar = Calendar.getInstance();

		Date date = getDate();

		if (date != null) {
			calendar.setTime(date);
		}

		return calendar;
	}

	/**
	 * @param date
	 * @param dateFormatString
	 * @param dateEditor
	 */
	private void createDateEditor(Date date, String dateFormatString,
			IDateEditor dateEditor) {
		this.dateEditor = dateEditor;

		if (this.dateEditor == null) {
			this.dateEditor = new JTextFieldDateEditor();
		}

		setDate(date);
		this.dateEditor.addPropertyChangeListener("date", this);
		this.dateEditor.getUiComponent().addPropertyChangeListener(this);
		this.dateEditor.setDateFormatString(dateFormatString);

		if (useTableCellLayout) {
			add(this.dateEditor.getUiComponent());
		} else {
			add(this.dateEditor.getUiComponent(), "growx");
		}
	}

	/**
     *
     */
	@SuppressWarnings("serial")
	private void createCalendarButton() {
		URL iconURL = getClass().getResource("/icons/calendar-date18.gif");
		ImageIcon icon = null;

		if (iconURL != null) {
			icon = new ImageIcon(iconURL);
		} else {
			icon = new ImageIcon("icons/calendar-date18.gif");
		}

		calendarButton = new JButton(new CalendarButtonAction(icon)) {
			/**
			 * Initialisation.
			 */
			{
				setMargin(new Insets(0, 0, 0, 0));
				setFocusPainted(false);
				setIconTextGap(0);
				setContentAreaFilled(false);
				setFocusable(false);
			}
		};

		if (useTableCellLayout) {
			add(calendarButton);
		} else {
			add(calendarButton, "east");
		}
	}

	/**
     *
     */
	@SuppressWarnings("serial")
	private void createDialog() {
		// Calendar.
		jCalendar = new JCalendar(createCalendar());
		jCalendar
				.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

		jCalendar.getDayChooser().addPropertyChangeListener("day", this);
		jCalendar.getMonthChooser().addPropertyChangeListener("month", this);
		jCalendar.getYearChooser().addPropertyChangeListener("year", this);

		jCalendar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke("ESCAPE"), "rollbackDateAction");

		jCalendar.getActionMap().put("rollbackDateAction",
				new AbstractAction() {
					/**
             *
             */
					@Override
					public void actionPerformed(ActionEvent actionEvent) {
						rollbackDate();
					}
				});

		jCalendar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke("control ENTER"), "commitDateAction");

		jCalendar.getActionMap().put("commitDateAction", new AbstractAction() {
			/**
             *
             */
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				commitDate();
			}
		});

		// Dialog (holding calendar).
		JDialog.setDefaultLookAndFeelDecorated(true);

		dialog = new JDialog();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLayout(new MigLayout("insets 0 0 0 0, fill, hidemode 3"));
		dialog.setUndecorated(true);
		dialog.setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);
		dialog.setAlwaysOnTop(true);
		dialog.add(jCalendar);

		dialog.addWindowFocusListener(new WindowAdapter() {
			/**
             *
             */
			@Override
			public void windowLostFocus(WindowEvent windowEvent) {
				rollbackDate();
			}
		});
	}

	/**
     *
     */
	private void commitDate() {
		originalDate = getDate();
		dialog.setVisible(false);
	}

	/**
     *
     */
	private void rollbackDate() {
		setDate(originalDate);
		dialog.setVisible(false);
	}

	/**
	 * @author David Ainslie
	 */
	class CalendarButtonAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		/**
		 * @param icon
		 */
		CalendarButtonAction(Icon icon) {
			super("", icon);

			if (icon == null) {
				putValue(Action.NAME, "D");
			}

			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
			putValue(Action.ACCELERATOR_KEY, "alt C");
		}

		/**
         *
         */
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			originalDate = getDate();

			JComponent source = (JComponent) actionEvent.getSource();

			if (source != null && source.isShowing()) {
				createDialog();
				dialog.setLocationRelativeTo(source);
				dialog.setLocation(
						source.getLocationOnScreen().x + source.getWidth(),
						source.getLocationOnScreen().y + source.getHeight());
				dialog.setVisible(true);

				jCalendar.requestFocus();

				Dimension dialogPreferredSize = new Dimension(
						jCalendar.getPreferredSize().width,
						jCalendar.getPreferredSize().height + 5);
				dialog.setSize(dialogPreferredSize);
				dialog.setPreferredSize(dialogPreferredSize);
				dialog.setMinimumSize(dialogPreferredSize);

				GraphicsDeviceUtil.ensureOnScreen(dialog);
			}
		}
	}
}