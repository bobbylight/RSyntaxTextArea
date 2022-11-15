package org.fife.ui.rtextarea.readonly;

import org.fife.ui.rsyntaxtextarea.FileLocation;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import static org.fife.ui.rsyntaxtextarea.SyntaxConstants.SYNTAX_STYLE_XML;

public class ReadOnlyDocumentLauncher {

	public static void main(String[] args) throws IOException {
		TestApp testApp = new TestApp();
		testApp.show();
	}

	private static class TestApp {

		public void show() throws IOException {
			JFrame frame = buildAndDisplayGui();
			frame.setVisible(true);
		}

		private JFrame buildAndDisplayGui() throws IOException {
			JFrame frame = new JFrame("Test Frame");
			frame.setMinimumSize(new Dimension(400, 400));
			frame.setMaximumSize(new Dimension(400, 400));
			frame.setResizable(false);

			buildContent(frame);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setMinimumSize(new Dimension(400, 400));
			frame.setSize(new Dimension(400, 400));
			frame.setResizable(true);
			return frame;
		}

		private void buildContent(JFrame frame) throws IOException {
			File documentFile = File.createTempFile("ReadOnlyDocumentLauncher", ".txt");
			documentFile.deleteOnExit();
			Charset charset = StandardCharsets.UTF_8;
			FileBuilderTestUtil.writeTestFileWithAsciiChars(documentFile.toPath(), charset, "\n", 1024);

			TextEditorPane textArea = new TextEditorPane();

			textArea.loadLocalFileInReadOnlyDocument(documentFile, charset);
			textArea.setSyntaxEditingStyle(SYNTAX_STYLE_XML);
			RTextScrollPane scrollPane = new RTextScrollPane(textArea, true);
			frame.getContentPane().add(scrollPane);
			JMenuBar menubar = new JMenuBar();

			menubar.add(creteFileChooserAction(frame, (selectedFile) -> textArea.load(FileLocation.create(selectedFile)), "Open editable document"));
			menubar.add(creteFileChooserAction(frame, file -> {

				JProgressBar progressBar = new JProgressBar();
				progressBar.setIndeterminate(true);
				JDialog progressDialog = new JDialog(frame);
				progressDialog.add(progressBar);
				progressDialog.setSize(400, 400);
				progressDialog.setVisible(true);
				SwingWorker<Object, Object> swingWorker = new SwingWorker<>() {
					@Override
					protected ReadOnlyDocument doInBackground() throws IOException {
						ReadOnlyFileStructure fileStructure = new ReadOnlyFileStructureParser(file.toPath(), StandardCharsets.UTF_8).readStructure();
						ReadOnlyContent content = new ReadOnlyContent(file.toPath(), StandardCharsets.UTF_8, fileStructure);
						ReadOnlyDocument tokens = new ReadOnlyDocument(null, SYNTAX_STYLE_XML, content);
						textArea.loadDocument(FileLocation.create(file), tokens, StandardCharsets.UTF_8);
						return null;
					}

					@Override
					protected void done() {
						progressDialog.dispose();
					}
				};

				swingWorker.execute();

			}, "Open read only document"));

			frame.setJMenuBar(menubar);
		}
	}

	private static JMenuItem creteFileChooserAction(JFrame frame, LoadFileAction fileAction, String label) {
		return new JMenuItem(new AbstractAction(label) {
			@Override
			public void actionPerformed(ActionEvent e) {
				onActionPerformed(frame, fileAction);
			}
		});
	}

	private static void onActionPerformed(JFrame frame, LoadFileAction fileAction) {
		final JFileChooser fc = new JFileChooser();
		if( fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION ){
			File selectedFile = fc.getSelectedFile();
			try{
				fileAction.onFileSelected(selectedFile);
			} catch( Exception ex ){
				throw new RuntimeException(ex);
			}
		}
	}

	@FunctionalInterface
	private interface LoadFileAction {

		void onFileSelected(File file) throws IOException, ExecutionException, InterruptedException;

	}

}
