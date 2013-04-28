package com.beirtipol.binding.swing.demo;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import com.beirtipol.binding.core.binders.AbstractPresenter;
import com.beirtipol.binding.core.binders.dialog.DialogBinder;
import com.beirtipol.binding.core.binders.widget.AbstractButtonBinder;
import com.beirtipol.binding.core.binders.widget.IButtonBinder;
import com.beirtipol.binding.core.delegates.IDialogDelegate;
import com.beirtipol.binding.swing.AbstractPresentableSwingPanel;
import com.beirtipol.binding.swing.SwingButtonDelegate;
import com.beirtipol.binding.swing.SwingOldDialogDelegate;
import com.beirtipol.binding.swing.demo.SwingDialogDemo.Presenter;

public class SwingDialogDemo extends AbstractPresentableSwingPanel<Presenter> {
	private JButton btnOpenDialog;
	private SwingButtonDelegate openDialogDelegate;
	private SwingOldDialogDelegate dialogDelegate;
	private JDialog dialog;

	@Override
	protected JComponent createSwingComponent() {
		JPanel result = new JPanel();
		result.setLayout(new MigLayout("insets 0 0 0 0"));

		// Widgets
		{
			btnOpenDialog = new JButton();
			result.add(btnOpenDialog);

			dialog = new JDialog();
			dialog.setTitle("Empty Dialog");

		}
		// Delegates
		{
			openDialogDelegate = new SwingButtonDelegate(btnOpenDialog);
			dialogDelegate = new SwingOldDialogDelegate(dialog, btnOpenDialog);
		}
		return result;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		presenter.getOpenDialogBinder().setDelegate(openDialogDelegate);
		presenter.getDialogBinder().setDelegate(dialogDelegate);
	}

	class Presenter extends AbstractPresenter {
		private IButtonBinder openDialogBinder;
		private DialogBinder<IDialogDelegate> dialogBinder;

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

		public DialogBinder<IDialogDelegate> getDialogBinder() {
			if (dialogBinder == null) {
				dialogBinder = new DialogBinder<IDialogDelegate>();
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

		SwingDialogDemo panel = new SwingDialogDemo();
		Presenter presenter = panel.new Presenter();
		frame.getContentPane().add(panel.getSwingComponent());
		panel.setPresenter(presenter);
		presenter.updateUI();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}
