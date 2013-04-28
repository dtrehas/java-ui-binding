package com.beirtipol.binding.core.binders;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Set;

/**
 * Events are handled here via transactions. This class offers little
 * Thread-safety as UI managers are Single Threaded by design.
 * 
 * @author O041484
 */
public abstract class AbstractPresenter implements IPresenter, PropertyChangeListener {
	protected PropertyChangeSupport binderChangeSupport = new PropertyChangeSupport(this);

	protected Set<PropertyChangeEvent> currentTransaction = new HashSet<PropertyChangeEvent>();

	private volatile boolean onTransaction;

	/**
	 * No exception is ever thrown from this method. It is simply to remind the
	 * programmer that <code>fire()</code> should be called from a finally
	 * block.
	 * 
	 * @throws TransactionException
	 * @see {@link AbstractPresenter#fire()}
	 */
	protected void startTransaction() throws TransactionException {
		onTransaction = true;
	}

	protected void performTransaction(Runnable transaction) {
		try {
			startTransaction();
			transaction.run();
		} catch (TransactionException e) {
		} finally {
			fire();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		synchronized (currentTransaction) {
			currentTransaction.add(evt);
			if (!onTransaction) {
				fire();
			}
		}
	}

	/**
	 * Notifies all listening binders of all captured events and ends the
	 * current transaction.
	 */
	protected void fire() {
		synchronized (currentTransaction) {
			for (PropertyChangeEvent evt : currentTransaction) {
				binderChangeSupport.firePropertyChange(evt);
			}
			currentTransaction.clear();
			onTransaction = false;
		}
	}

	@SuppressWarnings("serial")
	protected class TransactionException extends Exception {
	}

	@Override
	public void updateUI() {
		PresentationModelReflectionSupport.updateUI(this);
	}

}
