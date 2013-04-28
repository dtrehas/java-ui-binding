package com.beirtipol.binding.swt.util;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

public final class SWTSelectionUtils {
	public static Object getSingleSelection(ISelectionProvider selectionProvider) {
		if (selectionProvider != null) {
			return getSingleSelection(selectionProvider.getSelection());
		}
		return null;
	}

	public static Object getSingleSelection(ISelection selection) {
		Object returnValue = null;

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;

			if (structuredSelection != null) {
				returnValue = structuredSelection.getFirstElement();
			}
		}

		return returnValue;
	}

	public static IStructuredSelection getStructuredSelection(ISelectionProvider provider) {
		IStructuredSelection structuredSelection = new StructuredSelection();

		if (provider != null) {
			ISelection selection = provider.getSelection();
			if (selection instanceof IStructuredSelection) {
				structuredSelection = (IStructuredSelection) selection;
			}
		}

		return structuredSelection;
	}

}
