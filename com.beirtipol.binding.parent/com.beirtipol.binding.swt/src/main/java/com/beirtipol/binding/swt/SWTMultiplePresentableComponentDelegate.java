package com.beirtipol.binding.swt;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.beirtipol.binding.core.binders.IPresenter;
import com.beirtipol.binding.core.binders.component.IPresentableComponentBinder;
import com.beirtipol.binding.core.delegates.IMultiplePresentableComponentDelegate;

@SuppressWarnings("rawtypes")
public abstract class SWTMultiplePresentableComponentDelegate implements
		IMultiplePresentableComponentDelegate {
	private final Composite control;
	private final Map<IPresentableComponentBinder, SWTAbstractPresentableComposite> binders = new LinkedHashMap<IPresentableComponentBinder, SWTAbstractPresentableComposite>();

	public SWTMultiplePresentableComponentDelegate(Composite control) {
		this.control = control;
	}

	@Override
	public void free() {

	}

	@Override
	@SuppressWarnings("unchecked")
	public void setComponentBinders(
			final Collection<IPresentableComponentBinder> newBindersToSet) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				Set<IPresentableComponentBinder> existingBinders = SWTMultiplePresentableComponentDelegate.this.binders
						.keySet();
				Collection<IPresentableComponentBinder> bindersToRemove = CollectionUtils
						.subtract(existingBinders, newBindersToSet);
				for (IPresentableComponentBinder compBinder : existingBinders) {
					if (bindersToRemove.contains(compBinder)) {
						SWTAbstractPresentableComposite panel = SWTMultiplePresentableComponentDelegate.this.binders
								.get(compBinder);
						if (panel != null && !panel.isDisposed()) {
							panel.dispose();
						}
						compBinder.setDelegate(null);
					}
				}
				for (IPresentableComponentBinder compBinder : bindersToRemove) {
					SWTMultiplePresentableComponentDelegate.this.binders
							.remove(compBinder);
				}
				Collection<IPresentableComponentBinder> bindersToAdd = CollectionUtils
						.subtract(newBindersToSet, existingBinders);
				for (IPresentableComponentBinder childBinder : bindersToAdd) {
					SWTPresentableComponentDelegate panelDelegate = createDelegateForBinder(childBinder);
					childBinder.setDelegate(panelDelegate);
					SWTAbstractPresentableComposite presentablePanel = panelDelegate
							.getComponent();
					childBinder.updateUI();
					SWTMultiplePresentableComponentDelegate.this.binders.put(
							childBinder, presentablePanel);
				}

				// Once everything has been added, layout the control so that
				// the children are
				// painted on screen.
				control.layout(true);
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
	protected abstract SWTPresentableComponentDelegate<? extends IPresenter> createDelegateForBinder(
			IPresentableComponentBinder binder);

}