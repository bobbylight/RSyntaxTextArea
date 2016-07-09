package org.fife.ui;

import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;


/**
 * Runs Swing unit tests on the EDT.  Stolen from
 * <a href="https://community.oracle.com/thread/1350403">
 * https://community.oracle.com/thread/1350403</a>.<p>
 * This particular class is public domain.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class SwingRunner extends BlockJUnit4ClassRunner {


	public SwingRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}


	@Override
	public void run(final RunNotifier runNotifier) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					SwingRunner.super.run(runNotifier);
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}


}