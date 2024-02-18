package org.fife.ui.rtextarea.readonly;

import org.fife.io.UnicodeReader;
import org.fife.ui.rsyntaxtextarea.FileLocation;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.fife.ui.rsyntaxtextarea.SyntaxConstants.SYNTAX_STYLE_XML;

public class ReadOnlyDocumentLauncher {

	private static final Logger LOGGER = Logger.getLogger(ReadOnlyDocumentLauncher.class.getName());
	private static TextEditorPane textArea = new TextEditorPane();

	public static void main(String[] args) throws IOException {
		TestApp testApp = new TestApp();
		testApp.show();
	}

	private static class TestApp {

		private File documentFile;
		boolean createBom = true;
		Charset selectedCharset = StandardCharsets.UTF_8;

		JTextField fileContentField = new JTextField("abcd");
		JTextField fileRowsField = new JTextField(String.valueOf(20));
		JTextField tailField = new JTextField(String.valueOf(0));

		JLabel statusLabel = new JLabel(" ");
		private JProgressBar progressBar = new JProgressBar();

		public void show() {
			JFrame frame = buildAndDisplayGui();
			frame.setVisible(true);
		}

		private JFrame buildAndDisplayGui() {
			JFrame frame = new JFrame("Test Frame");
			frame.setMinimumSize(new Dimension(640, 480));
			frame.setResizable(false);

			frame.setContentPane(createPanel());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setResizable(true);
			return frame;
		}

		private JComponent createPanel() {
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(createConfigurationPanel(), BorderLayout.EAST);
			panel.add(createEditorPanel(), BorderLayout.CENTER);
			return panel;
		}

		private JComponent createConfigurationPanel() {
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			JCheckBox bomCheckbox = new JCheckBox(new AbstractAction("Add BOM") {
				@Override
				public void actionPerformed(ActionEvent e) {
					createBom = ((JCheckBox) e.getSource()).isSelected();
				}
			});
			bomCheckbox.setSelected(createBom);
			panel.add(bomCheckbox);

			panel.add(createCharsetPanel());

			panel.add(new JLabel("File content"));
			panel.add(fileContentField);

			panel.add(new JLabel("Rows"));
			panel.add(fileRowsField);

			panel.add(new JLabel("Skip chars"));
			panel.add(tailField);

			JButton createFileButton = new JButton(new AbstractAction("Write file") {
				@Override
				public void actionPerformed(ActionEvent e) {
					createFile();
				}
			});
			panel.add(createFileButton);

			JButton readFileButton = new JButton(new AbstractAction("Read file") {
				@Override
				public void actionPerformed(ActionEvent e) {
					createReadDocumentWorker().execute();
				}
			});
			panel.add(readFileButton);

			panel.add(statusLabel);

			panel.add(progressBar);
//			progressBar.setEnabled(false);
			progressBar.setIndeterminate(false);


			return panel;
		}

		private JPanel createCharsetPanel() {
			ButtonGroup charsetGroup = new ButtonGroup();
			JPanel panel = new JPanel();
			BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
			panel.setLayout(layout);

			panel.add(new JLabel("Charset"));
			panel.add(createCharsetRadioButton(charsetGroup, StandardCharsets.US_ASCII));
			panel.add(createCharsetRadioButton(charsetGroup, StandardCharsets.ISO_8859_1));
			panel.add(createCharsetRadioButton(charsetGroup, StandardCharsets.UTF_8));
			panel.add(createCharsetRadioButton(charsetGroup, StandardCharsets.UTF_16BE));
			panel.add(createCharsetRadioButton(charsetGroup, StandardCharsets.UTF_16LE));
			panel.add(createCharsetRadioButton(charsetGroup, StandardCharsets.UTF_16));
			panel.add(createCharsetRadioButton(charsetGroup, null));

			return panel;

		}


		private JRadioButton createCharsetRadioButton(ButtonGroup charsetGroup, Charset charset) {
			JRadioButton radioButton = new JRadioButton(createRadioButtonAction(charset));
			radioButton.setSelected(charset == selectedCharset);
			charsetGroup.add(radioButton);
			return radioButton;
		}

		private Action createRadioButtonAction(Charset charset) {
			String name = charset == null ? "From BOM" : charset.displayName();
			return new AbstractAction(name) {
				@Override
				public void actionPerformed(ActionEvent event) {
					if( charset == null ){
						try( UnicodeReader reader = new UnicodeReader(documentFile) ){
							selectedCharset = Charset.forName(reader.getEncoding());
						} catch( IOException e ){
							LOGGER.log(Level.WARNING, e.getMessage(), e);
						}

					} else selectedCharset = charset;
				}
			};
		}

		private void createFile() {
			try{
				documentFile = File.createTempFile("ReadOnlyDocumentLauncher", ".txt");
				documentFile.deleteOnExit();
				FileBuilderTestUtil.writeTestFile(documentFile.toPath(), selectedCharset, "\n", fileContentField.getText(), Integer.parseInt(fileRowsField.getText()), createBom);
				statusLabel.setText("File saved");
				LOGGER.log(Level.INFO, "File saved: " + documentFile.getAbsolutePath());
			} catch( IOException e ){
				statusLabel.setText("Save file failed");
				LOGGER.log(Level.WARNING, e.getMessage(), e);
			}
		}

		private JComponent createEditorPanel() {
			return new RTextScrollPane(textArea, true);
		}


		private SwingWorker<Object, Object> createReadDocumentWorker() {
			return new SwingWorker<>() {
				@Override
				protected ReadOnlyDocument doInBackground() throws Exception {
					progressBar.setIndeterminate(true);

					Path filePath = documentFile.toPath();
					ReadOnlyFileStructureParser structureParser = new ReadOnlyFileStructureParser(filePath, selectedCharset, Integer.parseInt(tailField.getText()));
					ReadOnlyFileStructure fileStructure = structureParser.readStructure();
					ReadOnlyContent content = new ReadOnlyContent(filePath, selectedCharset, fileStructure);
					ReadOnlyDocument document = new ReadOnlyDocument(null, SYNTAX_STYLE_XML, content);
					textArea.loadDocument(FileLocation.create(documentFile), document, selectedCharset);

					return document;
				}

				@Override
				protected void done() {
					try{
						statusLabel.setText("File loaded");
					} catch( Exception e ){
						LOGGER.log(Level.WARNING, e.getMessage(), e);
						statusLabel.setText("Load file failed");
					}
					progressBar.setIndeterminate(false);
				}
			};
		}

	}

}
