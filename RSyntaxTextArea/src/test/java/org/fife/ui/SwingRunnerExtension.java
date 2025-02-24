/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;


/**
 * Runs Swing unit tests on the EDT.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class SwingRunnerExtension implements InvocationInterceptor {

	@Override
	public void interceptTestMethod(InvocationInterceptor.Invocation<Void> invocation,
									ReflectiveInvocationContext<Method> invocationContext,
									ExtensionContext extensionContext) throws Throwable {

		AtomicReference<Throwable> throwable = new AtomicReference<>();

		SwingUtilities.invokeAndWait(() -> {
			try {
				invocation.proceed();
			} catch (Throwable t) {
				throwable.set(t);
			}
		});
		Throwable t = throwable.get();
		if (t != null) {
			throw t;
		}
	}
}
