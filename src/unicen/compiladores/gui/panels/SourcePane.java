package unicen.compiladores.gui.panels;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.BoxLayout;

import unicen.compiladores.gui.Gui;

public class SourcePane extends JPanel{
	private JScrollPane jsp;
	private JTextArea jta;
	private JTextArea lines;
	private Gui gui;

	public SourcePane(final Gui gui) {
		this.gui = gui;
		this.jsp = new JScrollPane();
		jta = new JTextArea();
		lines = new JTextArea("1");
 
		lines.setBackground(Color.LIGHT_GRAY);
		lines.setEditable(false);
 
		jta.getDocument().addDocumentListener(new DocumentListener(){
			public String getText(){
				int caretPosition = jta.getDocument().getLength();
				Element root = jta.getDocument().getDefaultRootElement();
				String text = "1" + System.getProperty("line.separator");
				for(int i = 2; i < root.getElementIndex( caretPosition ) + 2; i++){
					text += i + System.getProperty("line.separator");
				}
				return text;
			}
			@Override
			public void changedUpdate(DocumentEvent de) {
				gui.setDirty(true);
				lines.setText(getText());
			}
 
			@Override
			public void insertUpdate(DocumentEvent de) {
				gui.setDirty(true);
				lines.setText(getText());
			}
 
			@Override
			public void removeUpdate(DocumentEvent de) {
				gui.setDirty(true);
				lines.setText(getText());
			}
 
		});
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		jsp.setViewportView(jta);
		jsp.setRowHeaderView(lines);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(jsp);
	}

	public void clean() {
		this.jta.setText("");
		
	}

	public void append(String current) {
		this.jta.append(current);
		
	}

	public String getSource() {
		return jta.getText();
	}

}
