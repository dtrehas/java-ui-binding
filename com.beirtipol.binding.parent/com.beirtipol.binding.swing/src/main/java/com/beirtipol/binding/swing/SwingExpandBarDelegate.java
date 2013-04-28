package com.beirtipol.binding.swing;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.plaf.basic.BasicTaskPaneUI;

import com.beirtipol.binding.core.binders.IPresenter;
import com.beirtipol.binding.core.binders.component.IExpandItemBinder;
import com.beirtipol.binding.core.binders.component.IPresentableComponentBinder;
import com.beirtipol.binding.core.delegates.IExpandBarDelegate;
import com.beirtipol.binding.core.delegates.IPresentableComponentDelegate;

public abstract class SwingExpandBarDelegate implements IExpandBarDelegate {
	private static final Logger LOG = Logger.getLogger(SwingExpandBarDelegate.class);

	private final JXTaskPaneContainer control;
	private final Map<IExpandItemBinder, JXTaskPane> binders = new HashMap<IExpandItemBinder, JXTaskPane>();

	public SwingExpandBarDelegate(JXTaskPaneContainer control) {
		this.control = control;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setExpandItemBinders(final Collection<IExpandItemBinder> newBinders) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				Collection<IExpandItemBinder> bindersToRemove = CollectionUtils.subtract(SwingExpandBarDelegate.this.binders.keySet(), newBinders);
				for (IExpandItemBinder eiBinder : SwingExpandBarDelegate.this.binders.keySet()) {
					if (bindersToRemove.contains(eiBinder)) {
						JXTaskPane jxTaskPane = SwingExpandBarDelegate.this.binders.get(eiBinder);
						control.remove(jxTaskPane);
						eiBinder.getDelegate().free();
						eiBinder.setDelegate(null);
					}
				}
				for (IExpandItemBinder eiBinder : bindersToRemove) {
					SwingExpandBarDelegate.this.binders.remove(eiBinder);
				}

				Collection<IExpandItemBinder> newNewBinders = CollectionUtils.subtract(newBinders, SwingExpandBarDelegate.this.binders.keySet());
				for (IExpandItemBinder eiBinder : newNewBinders) {
					JXTaskPane taskPane = new JXTaskPane();
					BasicTaskPaneUI taskPaneUI = new BasicTaskPaneUI();
					taskPane.setUI(taskPaneUI);

					taskPane.setLayout(new MigLayout("insets 0 0 0 0"));
					taskPane.getContentPane().setLayout(new MigLayout("insets 0 0 0 0"));

					// Animation is very slow on a busy screen.
					taskPane.setAnimated(false);

					SwingExpandItemDelegate delegate = new SwingExpandItemDelegate(taskPane);
					eiBinder.setDelegate(delegate);

					IPresentableComponentBinder componentBinder = eiBinder.getComponentBinder();

					if (componentBinder.getPresenter() == null) {
						LOG.info("No presenter found for " + eiBinder.getTitle() + " binder;");
						continue;
					}

					AbstractPresentableSwingPanel taskPaneComponent = createExpandItemComponent(componentBinder);
					taskPane.getContentPane().add(taskPaneComponent.getSwingComponent());
					taskPane.getContentPane().setBackground(taskPaneComponent.getSwingComponent().getBackground());

					IPresentableComponentDelegate componentDelegate = getComponentDelegate(taskPaneComponent);
					componentBinder.setDelegate(componentDelegate);

					componentBinder.updateUI();

					control.add(taskPane);
					SwingExpandBarDelegate.this.binders.put(eiBinder, taskPane);
				}
				control.updateUI();
				for (IExpandItemBinder binder : SwingExpandBarDelegate.this.binders.keySet()) {
					binder.updateUI();
				}
			}
		});

	}

	protected abstract IPresentableComponentDelegate<? extends IPresenter> getComponentDelegate(AbstractPresentableSwingPanel<? extends IPresenter> panel);

	@SuppressWarnings("rawtypes")
	protected abstract AbstractPresentableSwingPanel<? extends IPresenter> createExpandItemComponent(IPresentableComponentBinder componentBinder);
}