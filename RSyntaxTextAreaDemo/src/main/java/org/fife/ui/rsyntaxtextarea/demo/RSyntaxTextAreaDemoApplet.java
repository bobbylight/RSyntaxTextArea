package org.fife.ui.rsyntaxtextarea.demo;

import javax.swing.*;

import org.fife.ui.rsyntaxtextarea.*;


/**
 * Demo application for {@link RSyntaxTextArea}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RSyntaxTextAreaDemoApplet extends JApplet {


	public RSyntaxTextAreaDemoApplet() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace(); // Never happens
		}
		setRootPane(new DemoRootPane());
	}


	@Override
	public void start() {
		super.start();
		((DemoRootPane)getRootPane()).focusTextArea();
	}


}