package com.beirtipol.binding.swing.core.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class JAbstractTitleDialog extends JDialog {
	private static final Color GRADIENT_COLOR = new Color(224, 231, 248);
	private static final KeyStroke escapeStroke = KeyStroke.getKeyStroke(
			KeyEvent.VK_ESCAPE, 0);
	public static final String dispatchWindowClosingActionMapKey = "com.spodding.tackline.dispatch:WINDOW_CLOSING";
	private int returnCode = -1;

	public static final int OK = 0;
	public static final int CANCEL = 1;

	private JPanel titleArea;

	/**
	 * This is a bit crap - can't seem to change the icon on the label.
	 */
	// protected static final ImageIcon INFO_ICON =
	// SWTResourceManager.getSwingImage("information.gif");
	// protected static final ImageIcon WARNING_ICON =
	// SWTResourceManager.getSwingImage("warning.gif");
	// protected static final ImageIcon ERROR_ICON =
	// SWTResourceManager.getSwingImage("error.gif");
	// protected JLabel infoLabel = new JLabel(INFO_ICON);
	// protected JLabel warningLabel = new JLabel(WARNING_ICON);
	// protected JLabel errorLabel = new JLabel(ERROR_ICON);

	private String errorMessage;
	private String warningMessage;

	private final JTextArea titleTextArea = new JTextArea();
	private final JTextArea messageTextArea = new JTextArea();

	protected static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 12);
	protected static final Font MESSAGE_FONT = new Font("Arial", Font.PLAIN, 10);

	private String message;

	private boolean created;

	private JPanel buttonBar;

	private final Map<JButton, Integer> buttonReturnCodes = new HashMap<JButton, Integer>();

	protected boolean relativeLocationSet;
	private WindowAdapter windowListener;

	private final Component relativeToComponent;

	/**
	 * Modeless by default.
	 */
	public JAbstractTitleDialog() {
		this(null, false);
	}

	/**
	 * @param modal
	 */
	public JAbstractTitleDialog(boolean modal) {
		this(null, modal);
	}

	/**
	 * @param modal
	 */
	public JAbstractTitleDialog(final Component relativeToComponent,
			boolean modal) {
		this.relativeToComponent = relativeToComponent;

		setModal(modal);
		addCloseBehaviour();
		if (relativeToComponent != null) {
			setLocationRelativeTo(relativeToComponent);
		}
		// This causes a LOT of flickering.
		// if (relativeToComponent != null)
		// {
		// if (relativeToComponent.isShowing())
		// {
		// setLocationRelativeTo(relativeToComponent);
		// }
		// else
		// {
		// addWindowListener(new WindowAdapter()
		// {
		// @Override
		// public void windowActivated(WindowEvent e)
		// {
		// if (relativeLocationSet)
		// {
		// return;
		// }
		// // this may be too late but it's pointless setting it if the
		// component is
		// // not shown.
		// setLocationRelativeTo(relativeToComponent);
		// relativeLocationSet = true;
		// }
		// });
		// }
		// }
	}

	@Override
	public void setTitle(String title) {
		titleTextArea.setText(title);
	}

	public void setMessage(String message) {
		this.message = message;
		messageTextArea.setText(message);
	}

	private void createControls() {
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new MigLayout("fill, ins 0, gap 0"));
		myPanel.add(createTitleArea(), "cell 0 0, hmin 60, growx");
		myPanel.add(new JSeparator(JSeparator.HORIZONTAL), "cell 0 1, growx");
		myPanel.add(createDialogArea(), "cell 0 2, push, growx, growy");
		myPanel.add(new JSeparator(JSeparator.HORIZONTAL), "cell 0 3, growx");

		buttonBar = new JPanel();
		buttonBar.setLayout(new MigLayout("alignx trailing"));
		createButtonsForButtonBar();
		myPanel.add(buttonBar, "cell 0 4, growx");

		pack();
		getContentPane().add(myPanel);
		created = true;
	}

	protected Dimension getInitialSize() {
		// This logic seems a bit better at getting a reasonable size. Dunno
		// what I was thinking
		// with the previous code! Did I even write it? Who knows..
		Dimension contentPaneSize = getContentPane().getPreferredSize();
		Dimension titleAreaSize = titleTextArea.getPreferredSize();
		Dimension messageAreaSize = messageTextArea.getPreferredSize();
		Dimension buttonBarSize = buttonBar.getPreferredSize();

		Dimension result = contentPaneSize;
		result.height += titleAreaSize.height;
		result.height += messageAreaSize.height;
		result.height += buttonBarSize.height;

		result.width = Math.max(result.width, titleAreaSize.width);
		result.width = Math.max(result.width, messageAreaSize.width);
		result.width = Math.max(result.width, buttonBarSize.width);
		result.width += 10;
		return result;
	}

	private Dimension addDimension(Dimension a, Dimension b) {
		return new Dimension(a.width + b.width, a.height + b.height);
	}

	private Component createTitleArea() {
		titleArea = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);

				GradientPaint mainGradient = new GradientPaint(0, 0,
						Color.WHITE, this.getWidth(), this.getHeight(),
						GRADIENT_COLOR);
				g2d.setPaint(mainGradient);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
			}
		};
		titleArea.setDoubleBuffered(true);
		titleArea.setBackground(Color.WHITE);
		titleArea.setLayout(new MigLayout("fill"));

		titleTextArea.setFont(TITLE_FONT);
		titleTextArea.setEditable(false);
		titleTextArea.setOpaque(false);
		titleArea.add(titleTextArea, "grow, cell 0 0, spanx 2");

		// titleArea.add(infoLabel, "cell 0 1, hidemode 3");
		// titleArea.add(warningLabel, "cell 0 1, hidemode 3");
		// titleArea.add(errorLabel, "cell 0 1, hidemode 3");
		//
		// infoLabel.setVisible(true);
		// errorLabel.setVisible(false);
		// warningLabel.setVisible(false);

		messageTextArea.setFont(MESSAGE_FONT);
		messageTextArea.setEditable(false);
		messageTextArea.setOpaque(false);
		titleArea.add(messageTextArea, "cell 1 1, push, alignx leading");

		return titleArea;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		updateMessage();
	}

	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
		updateMessage();
	}

	private void updateMessage() {
		if (errorMessage != null) {
			messageTextArea.setText("Error: " + errorMessage);
			messageTextArea.setForeground(Color.RED);
			// infoLabel.setVisible(false);
			// warningLabel.setVisible(false);
			// errorLabel.setVisible(true);
		} else if (warningMessage != null) {
			messageTextArea.setText("Warning: " + warningMessage);
			messageTextArea.setForeground(Color.RED);
			// infoLabel.setVisible(false);
			// warningLabel.setVisible(true);
			// errorLabel.setVisible(false);
		} else {
			messageTextArea.setText(message);
			messageTextArea.setForeground(null);
			// infoLabel.setVisible(true);
			// warningLabel.setVisible(false);
			// errorLabel.setVisible(false);
		}
	}

	//
	// protected void setIcon(ImageIcon icon)
	// {
	// infoLabel.setIcon(icon);
	// infoLabel.repaint();
	// }

	protected abstract JComponent createDialogArea();

	protected void createButtonsForButtonBar() {
		createButton(OK, "Ok", true);
		createButton(CANCEL, "Cancel", false);
	}

	/**
	 * @param id
	 * @param label
	 * @param defaultButton
	 * @return
	 */
	protected JButton createButton(int id, String label, boolean defaultButton) {
		final JButton button = new JButton(label);
		buttonReturnCodes.put(button, id);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonPressed(button);
			}
		});
		buttonBar.add(button, "right");
		if (defaultButton) {
			getRootPane().setDefaultButton(button);
		}
		return button;
	}

	private void buttonPressed(JButton button) {
		Integer id = buttonReturnCodes.get(button);
		setReturnCode(id);
		switch (id) {
		case OK:
			okPressed();
			break;
		case CANCEL:
			cancelPressed();
			break;
		default:
			break;
		}
	}

	public int open() {
		if (!created) {
			createControls();
			pack();
			setSize(getInitialSize());
		}
		adjustLocation();
		setVisible(true);
		return returnCode;
	}

	private void adjustLocation() {
		// EXHYBQTRD-11402 : add check to prevent IllegalComponentStateException
		// in
		// Component.getLocationOnScreen_NoTreeLock()
		if (relativeToComponent != null && relativeToComponent.isShowing()) {
			int x = 0;
			int y = 0;

			Point topLeft = relativeToComponent.getLocationOnScreen();
			Dimension parentSize = relativeToComponent.getSize();
			Dimension mySize = getSize();

			if (parentSize.width > mySize.width) {
				x = ((parentSize.width - mySize.width) / 2) + topLeft.x;
			} else {
				x = topLeft.x;
			}

			if (parentSize.height > mySize.height) {
				y = ((parentSize.height - mySize.height) / 2) + topLeft.y;
			} else {
				y = topLeft.y;
			}

			setLocation(x, y);
		}
	}

	public void close() {
		setVisible(false);
	}

	private boolean validateInput() {
		return errorMessage == null && warningMessage == null;
	}

	protected void okPressed() {
		if (validateInput()) {
			close();
		}
	}

	protected void cancelPressed() {
		close();
	}

	public int getReturnCode() {
		return returnCode;
	}

	private void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	private void addCloseBehaviour() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		windowListener = new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				cancelPressed();
			}
		};

		addWindowListener(windowListener);

		Action dispatchClosing = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent event) {
				cancelPressed();
			}
		};
		JRootPane root = getRootPane();
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke,
				dispatchWindowClosingActionMapKey);
		root.getActionMap().put(dispatchWindowClosingActionMapKey,
				dispatchClosing);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		JAbstractTitleDialog dialog = new JAbstractTitleDialog() {
			@Override
			protected Dimension getInitialSize() {
				return new Dimension(500, 500);
			}

			@Override
			protected JComponent createDialogArea() {
				return new JPanel();
			}
		};
		dialog.open();
	}

	@Override
	public void dispose() {
		JRootPane root = getRootPane();
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.remove(escapeStroke);
		root.getActionMap().remove(dispatchWindowClosingActionMapKey);
		if (windowListener != null) {
			removeWindowListener(windowListener);
		}
	}
}
