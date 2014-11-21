package unicen.compiladores.gui.panels;

import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import unicen.compiladores.gui.ParserError;
import unicen.compiladores.gui.Sentencia;
import unicen.compiladores.gui.panels.ErrorPane.SimpleModel;

public class SentenciasPane extends JPanel{

	private JScrollPane jscPnl;
	private JTable table;

	public SentenciasPane(){
		
		jscPnl = new JScrollPane();
		table = new JTable();
		table.setModel((TableModel) new SimpleModel());
		
		jscPnl.setViewportView(table);
		jscPnl.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(jscPnl);
	}
	
	public void generateTable(Vector<Sentencia> sentencias){
		table.removeAll();((SimpleModel) table.getModel()).erase();
		((SimpleModel) table.getModel()).addElements(sentencias);
		table.updateUI();
	}
	
	public class SimpleModel extends AbstractTableModel{

		Vector<Sentencia> data = new Vector<Sentencia>();
		String[] cabeceras = {"nro linea",
							  "tipo de estructura"};
		int countColumns = 0;
		
		public void erase() {
			data.removeAllElements();
			
		}
		public SimpleModel(){
			
		}
		
		public void addElements(Vector<Sentencia> sentencias) {
			data = sentencias;
			
		}

		public void addElement(Sentencia sentencia){
			data.add(sentencia);
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
				return data.get(row).getSentencia();
			}
			
			return null;
		}
		
	}
}
