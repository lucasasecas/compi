package unicen.compiladores.gui.panels;

import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import unicen.compiladores.gui.ParserError;
import unicen.compiladores.gui.panels.TokensPane.SimpleModel;
import as_Parser.ParserVal;

public class ErrorPane extends JPanel {

	private JScrollPane jscPnl;
	private JTable table;

	public ErrorPane(){
		jscPnl = new JScrollPane();
		table = new JTable();
		table.setModel((TableModel) new SimpleModel());
		
		jscPnl.setViewportView(table);
		jscPnl.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(jscPnl);
	}
	
	public void generateTable(Vector<ParserError> errores){
		table.removeAll();((SimpleModel) table.getModel()).erase();
		((SimpleModel) table.getModel()).addElements(errores);
		table.updateUI();
	}
	
	public class SimpleModel extends AbstractTableModel{

		Vector<ParserError> data = new Vector<ParserError>();
		String[] cabeceras = {"nro linea",
							  "tipo de error",
							  "mensaje"};
		int countColumns = 0;
		
		public void erase() {
			data.removeAllElements();
			
		}
		
		public SimpleModel(){
			
		}
		
		public void addElements(Vector<ParserError> errores) {
			data = errores;
			
		}

		public void addElement(ParserError error){
			data.add(error);
		}
		
		@Override
		public String getColumnName(int col){ 
            return cabeceras[col];
        }
		
		@Override
		public int getColumnCount() {
			
			return cabeceras.length;
		}

		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public Object getValueAt(int row, int col) {
			if(col==0){
				return data.get(row).getNroLine();
			}
			if(col==1){
				return data.get(row).getType() == ParserError.TYPE_LEXICO?"Lexico" : "Sintactico";
			}
			if(col==2){
				return data.get(row).getMensaje();
			}
			return null;
		}
		
	}
		
}
