package com.beirtipol.binding.swing;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.collections.CollectionUtils;

import com.beirtipol.binding.core.binders.IPresenter;
import com.beirtipol.binding.core.binders.component.IPresentableComponentBinder;
import com.beirtipol.binding.core.delegates.IMultiplePresentableComponentDelegate;

@SuppressWarnings("rawtypes")
public abstract class SwingMultiplePresentableComponentDelegate implements
		IMultiplePresentableComponentDelegate {
	private final JPanel control;
	private final Map<IPresentableComponentBinder, AbstractPresentableSwingPanel> binders = new LinkedHashMap<IPresentableComponentBinder, AbstractPresentableSwingPanel>();

	public SwingMultiplePresentableComponentDelegate(JPanel control) {
		this.control = control;
	}

	@Override
	public void free() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void setComponentBinders(
			final Collection<IPresentableComponentBinder> newBindersToSet) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Set<IPresentableComponentBinder> existingBinders = SwingMultiplePresentableComponentDelegate.this.binders
						.keySet();
				Collection<IPresentableComponentBinder> bindersToRemove = CollectionUtils
						.subtract(existingBinders, newBindersToSet);
				for (IPresentableComponentBinder compBinder : existingBinders) {
					if (bindersToRemove.contains(compBinder)) {
						AbstractPresentableSwingPanel panel = SwingMultiplePresentableComponentDelegate.this.binders
								.get(compBinder);
						control.remove(panel.getSwingComponent());
						compBinder.setDelegate(null);
					}
				}
				for (IPresentableComponentBinder compBinder : bindersToRemove) {
					SwingMultiplePresentableComponentDelegate.this.binders
							.remove(compBinder);
				}
				Collection<IPresentableComponentBinder> bindersToAdd = CollectionUtils
						.subtract(newBindersToSet, existingBinders);
				for (IPresentableComponentBinder eiBinder : bindersToAdd) {
					SwingPresentableComponentDelegate<? extends IPresenter> panelDelegate = createDelegateForBinder(eiBinder);
					eiBinder.setDelegate(panelDelegate);
					AbstractPresentableSwingPanel<? extends IPresenter> presentablePanel = panelDelegate
							.getComponent();
					control.add(presentablePanel.getSwingComponent(), "grow");
					eiBinder.updateUI();
					SwingMultiplePresentableComponentDelegate.this.binders.put(
							eiBinder, presentablePanel);
				}
				control.updateUI();
			}
		});
	}

	/**
	 * Here you can check the instance of the binder's presenter to choose which
	 * panel & delegate to create.
	 * 
	 * @param binder
	 * @return
	 */
	protected abstract SwingPresentableComponentDelegate<? extends IPresenter> createDelegateForBinder(
			IPresentableComponentBinder binder);
}
