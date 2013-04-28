package com.beirtipol.binding.swing.demo;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import com.beirtipol.binding.core.binders.AbstractPresenter;
import com.beirtipol.binding.core.binders.widget.AbstractButtonBinder;
import com.beirtipol.binding.core.binders.widget.IButtonBinder;
import com.beirtipol.binding.swing.AbstractPresentableSwingPanel;
import com.beirtipol.binding.swing.SwingButtonDelegate;

public class SwingButtonBinderDemo extends AbstractPresentableSwingPanel<SwingButtonBinderDemo.Presenter> {
	private JButton btnCount;
	private SwingButtonDelegate btnCountDelegate;

	@Override
	protected JComponent createSwingComponent() {
		JPanel result = new JPanel();
		result.setLayout(new MigLayout("insets 0 0 0 0"));

		// Widgets
		{
			btnCount = new JButton();
			result.add(btnCount);

		}
		// Delegates
		{
			btnCountDelegate = new SwingButtonDelegate(btnCount);
		}
		return result;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		presenter.getCountBinder().setDelegate(btnCountDelegate);
	}

	static class Presenter extends AbstractPresenter {
		private IButtonBinder countBinder;
		private int count = 0;

		public IButtonBinder getCountBinder() {
			if (countBinder == null) {
				countBinder = new AbstractButtonBinder() {
					@Override
					public String getText() {
						return "" + count;
					}

					@Override
					public void handlePressed() {
						count++;
						updateUI();
					}
				};
			}
			return countBinder;
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

		SwingButtonBinderDemo panel = new SwingButtonBinderDemo();
		Presenter presenter = new Presenter();
		frame.getContentPane().add(panel.getSwingComponent());
		panel.setPresenter(presenter);
		presenter.updateUI();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
