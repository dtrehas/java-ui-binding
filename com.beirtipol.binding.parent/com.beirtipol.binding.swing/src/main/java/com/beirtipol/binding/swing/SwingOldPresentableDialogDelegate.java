package com.beirtipol.binding.swing;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.beirtipol.binding.core.binders.dialog.IOldDialogPresenter;
import com.beirtipol.binding.core.binders.dialog.IOldPresentableDialogBinder;
import com.beirtipol.binding.core.binders.dialog.IValidator;
import com.beirtipol.binding.core.delegates.IOldPresentableDialogDelegate;
import com.beirtipol.binding.swing.core.dialog.JOldAbstractPresentableTitleDialog;

@SuppressWarnings({ "rawtypes" })
public class SwingOldPresentableDialogDelegate<T extends IOldDialogPresenter>
		extends SwingOldDialogDelegate implements
		IOldPresentableDialogDelegate<T> {
	private JOldAbstractPresentableTitleDialog<T> dialog;
	private ComponentAdapter openComponentAdaptor;
	private ComponentAdapter closedComponentAdaptor;

	public SwingOldPresentableDialogDelegate(
			JOldAbstractPresentableTitleDialog<T> dialog) {
		this(dialog, null);
	}

	public SwingOldPresentableDialogDelegate(
			JOldAbstractPresentableTitleDialog<T> dialog,
			JComponent parentComponent) {
		super(dialog, parentComponent);
		this.dialog = dialog;
	}

	@Override
	public void setPresenter(T presenter) {
		dialog.setPresenter(presenter);
	}

	@Override
	public void setValidator(IValidator validator) {
		dialog.setValidator(validator);
	}

	@Override
	public void openDialog() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (parentComponent != null) {
					// BEWARE, THERE BE DRAGONS HERE!

					/*
					 * Funny bit of code this, not quite perfect but better than
					 * existing. Issue is that when the parentComponent is on
					 * the far right of the screen, the majority of the dialog
					 * can appear offScreen. Try to calculate this and readjust
					 * the position.
					 */

					/*
					 * Ideal solution would be to calculate the full dimensions
					 * of all monitors (using the gc.getBounds() below) but some
					 * users have an L-shaped monitor configuration so this
					 * might still lead to off-screen layout. Once my hangover
					 * is gone and I have the energy, I'll have a think how to
					 * do this better.
					 */

					Point parentLocation;

					try {
						parentLocation = parentComponent.getLocationOnScreen();
					} catch (IllegalComponentStateException e) {
						Dimension dim = Toolkit.getDefaultToolkit()
								.getScreenSize();
						int x = (dim.width) / 2;
						int y = (dim.height) / 2;
						dialog.setLocation(new Point(x, y));
						dialog.open();

						return;
					}

					GraphicsEnvironment ge = GraphicsEnvironment
							.getLocalGraphicsEnvironment();
					GraphicsDevice[] gd = ge.getScreenDevices();
					GraphicsConfiguration currentMonitorGC = null;
					for (int i = 0; i < gd.length; i++) {
						GraphicsConfiguration gc = gd[i]
								.getDefaultConfiguration();
						Rectangle r = gc.getBounds();
						if (r.contains(parentLocation)) {
							currentMonitorGC = gc;
							break;
						}
					}

					Point proposedLocation = parentLocation;

					if (currentMonitorGC != null) {
						Rectangle currentMonitorBounds = currentMonitorGC
								.getBounds();

						if (dialog.getSize().width == 0) {

							int x = (int) currentMonitorBounds.getBounds()
									.getWidth() / 2;
							int y = (int) currentMonitorBounds.getBounds()
									.getHeight() / 2;
							dialog.setLocation(new Point(x, y));
							dialog.open();
							return;
						}

						Rectangle proposedDialogBounds = new Rectangle(
								parentLocation, dialog.getSize());

						if (!currentMonitorBounds
								.contains(proposedDialogBounds)) {
							proposedLocation.x = currentMonitorBounds.x
									+ currentMonitorBounds.width
									- proposedDialogBounds.width;
						} else {
							proposedLocation.x += parentComponent.getWidth();
						}
					}
					dialog.setLocation(proposedLocation);
				}
				dialog.open();
			}
		});
	}

	@Override
	public void addOpenListener(final IOldPresentableDialogBinder listener) {
		openComponentAdaptor = new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				if (dialog.isVisible()) {
					listener.onOpen();
				}
			}
		};

		dialog.addComponentListener(openComponentAdaptor);
	}

	@Override
	public void addCloseListener(final IOldPresentableDialogBinder listener) {
		closedComponentAdaptor = new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				if (!dialog.isVisible()) {
					listener.onClose(dialog.getReturnCode());
				}
			}
		};

		dialog.addComponentListener(closedComponentAdaptor);
	}

	@Override
	public void setMessage(String message) {
		dialog.setMessage(message);
	}

	@Override
	public void setTitle(String title) {
		dialog.setTitle(title);
	}

	@Override
	public void free() {
		super.free();
		if (dialog != null) {
			dialog.removeComponentListener(openComponentAdaptor);
			dialog.removeComponentListener(closedComponentAdaptor);
			dialog.setValidator(null);
			openComponentAdaptor = null;
			closedComponentAdaptor = null;
			dialog.dispose();
			dialog = null;
		}
	}
}