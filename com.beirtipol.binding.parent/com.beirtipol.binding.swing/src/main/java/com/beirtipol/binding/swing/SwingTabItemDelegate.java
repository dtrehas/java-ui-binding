package com.beirtipol.binding.swing;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;

import com.beirtipol.binding.core.binders.IPresenter;
import com.beirtipol.binding.core.binders.component.ITabItemBinder;
import com.beirtipol.binding.core.delegates.IPresentableComponentDelegate;
import com.beirtipol.binding.core.delegates.ITabItemDelegate;

public class SwingTabItemDelegate<T extends IPresenter> implements
		ITabItemDelegate, IPresentableComponentDelegate<T> {
	private String tabTitle = "";
	private JTabbedPane tabPane;
	private AbstractPresentableSwingPanel<T> tabComponent;

	public SwingTabItemDelegate(String tabTitle, JTabbedPane tabPane,
			AbstractPresentableSwingPanel<T> tabComponent) {
		this.tabPane = tabPane;
		this.tabComponent = tabComponent;
		this.tabTitle = tabTitle;
	}

	@Override
	public void addCloseListener(ITabItemBinder abstractTabItemBinder) {
		throw new UnsupportedOperationException("This needs to be implemented");
	}

	@Override
	public void setVisible(final boolean visible) {
		JComponent swingComponent = tabComponent.getSwingComponent();
		if (visible) {
			boolean found = false;
			for (Component c : tabPane.getComponents()) {
				if (c == swingComponent) {
					found = true;
				}
			}

			if (!found) {
				for (int i = 0; i < tabPane.getTabCount(); i++) {
					if (tabTitle.equals(tabPane.getTitleAt(i))) {
						tabPane.removeTabAt(i);
					}
				}
				tabPane.addTab(tabTitle, swingComponent);
			}

		} else {
			tabPane.remove(swingComponent);
		}
	}

	@Override
	public void setTitle(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (StringUtils.equals(tabTitle, text)) {
					return;
				}
				final int tabToChangeIndex = tabPane
						.indexOfComponent(tabComponent.getSwingComponent());
				final int selectedTabIndex = tabPane.getSelectedIndex();

				if (selectedTabIndex == -1 || tabToChangeIndex == -1) {
					return;
				}

				tabTitle = text;
				tabPane.setTitleAt(tabToChangeIndex, tabTitle);
				tabPane.setSelectedIndex(selectedTabIndex);
				tabPane.updateUI();
			}
		});
	}

	@Override
	public void setEnabled(final boolean enabled) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tabPane.setEnabledAt(tabPane.indexOfTab(tabTitle), enabled);
			}
		});
	}

	@Override
	public void setPresenter(T presenter) {
		tabComponent.setPresenter(presenter);
	}

	@Override
	public void free() {
		if (tabPane != null) {
			tabPane.remove(tabComponent.getSwingComponent());
			tabPane = null;
		}

		if (tabComponent != null) {
			tabComponent.dispose();
			tabComponent = null;
		}
	}
}