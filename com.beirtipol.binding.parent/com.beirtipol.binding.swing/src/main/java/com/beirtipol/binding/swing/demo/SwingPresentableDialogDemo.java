package com.beirtipol.binding.swing.demo;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import com.beirtipol.binding.core.binders.AbstractPresenter;
import com.beirtipol.binding.core.binders.dialog.AbstractPresentableDialogBinder;
import com.beirtipol.binding.core.binders.widget.AbstractButtonBinder;
import com.beirtipol.binding.core.binders.widget.IButtonBinder;
import com.beirtipol.binding.swing.AbstractPresentableSwingPanel;
import com.beirtipol.binding.swing.SwingButtonDelegate;
import com.beirtipol.binding.swing.core.dialog.SwingPresentableDialogDelegate;

public class SwingPresentableDialogDemo extends AbstractPresentableSwingPanel<SwingPresentableDialogDemo.Presenter> {
	private JButton btnOpenDialog;
	private SwingButtonDelegate openDialogDelegate;
	private SwingPresentableDialogDelegate<SwingButtonBinderDemo.Presenter> dialogDelegate;
	private JDialog dialog;
	private SwingButtonBinderDemo swingButtonBinderDemo;

	@Override
	protected JComponent createSwingComponent() {
		JPanel result = new JPanel();
		result.setLayout(new MigLayout("insets 0 0 0 0"));

		// Widgets
		{
			btnOpenDialog = new JButton();
			result.add(btnOpenDialog);

			dialog = new JDialog();
			dialog.setLayout(new MigLayout("insets 0 0 0 0"));
			swingButtonBinderDemo = new SwingButtonBinderDemo();
			dialog.add(swingButtonBinderDemo.getSwingComponent());

		}
		// Delegates
		{
			openDialogDelegate = new SwingButtonDelegate(btnOpenDialog);
			dialogDelegate = new SwingPresentableDialogDelegate<SwingButtonBinderDemo.Presenter>(dialog, swingButtonBinderDemo);
		}
		return result;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		presenter.getOpenDialogBinder().setDelegate(openDialogDelegate);
		presenter.getDialogBinder().setDelegate(dialogDelegate);
	}

	static class Presenter extends AbstractPresenter {
		private IButtonBinder openDialogBinder;
		private AbstractPresentableDialogBinder<SwingButtonBinderDemo.Presenter> dialogBinder;

		public IButtonBinder getOpenDialogBinder() {
			if (openDialogBinder == null) {
				openDialogBinder = new AbstractButtonBinder() {
					@Override
					public String getText() {
						return "Open";
					}

					@Override
					public void handlePressed() {
						getDialogBinder().open();
					}
				};
			}
			return openDialogBinder;
		}

		public AbstractPresentableDialogBinder<SwingButtonBinderDemo.Presenter> getDialogBinder() {
			if (dialogBinder == null) {
				dialogBinder = new AbstractPresentableDialogBinder<SwingButtonBinderDemo.Presenter>() {

					@Override
					public SwingButtonBinderDemo.Presenter getPresenter() {
						return new SwingButtonBinderDemo.Presenter();
					}
				};
			}
			return dialogBinder;
		}

		@Override
		public void free() {
		}
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		final JFrame frame = new JFrame("Swing");

		SwingPresentableDialogDemo panel = new SwingPresentableDialogDemo();
		SwingPresentableDialogDemo.Presenter presenter = new SwingPresentableDialogDemo.Presenter();
		frame.getContentPane().add(panel.getSwingComponent());
		panel.setPresenter(presenter);
		presenter.updateUI();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
