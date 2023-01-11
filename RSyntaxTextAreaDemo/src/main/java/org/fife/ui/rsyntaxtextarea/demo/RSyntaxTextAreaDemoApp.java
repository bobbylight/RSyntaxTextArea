package org.fife.ui.rsyntaxtextarea.demo;

import java.awt.*;
import javax.swing.*;


/**
 * Standalone version of the demo.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public final class RSyntaxTextAreaDemoApp extends JFrame {


	private RSyntaxTextAreaDemoApp() {
		setRootPane(new DemoRootPane());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("RSyntaxTextArea Demo Application");
		pack();
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.
										getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace(); // Never happens
			}
			Toolkit.getDefaultToolkit().setDynamicLayout(true);
			RSyntaxTextAreaDemoApp app = new RSyntaxTextAreaDemoApp();
			app.setLocationRelativeTo(null);
			app.setSize(new Dimension(1200, 800));
			app.setVisible(true);
		});
	}


}
