package unicen.compiladores.gui.panels;

import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import unicen.compiladores.gui.utils.TokensDictionary;
import as_Parser.ParserVal;
import Utils.TuplaTablaSimbolos;

public class TokensPane extends JPanel{

	private JScrollPane jscPnl;
	private JTable table;
	

	public TokensPane(){
	
		
		jscPnl = new JScrollPane();
		table = new JTable();
		table.setModel((TableModel) new SimpleModel());
		
		jscPnl.setViewportView(table);
		jscPnl.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(jscPnl);
	}
	
	public void generateTable(Vector<ParserVal> tokens){
		table.removeAll();
		((SimpleModel) table.getModel()).erase();
		((SimpleModel) table.getModel()).addElements(tokens);
		table.updateUI();
	}
	
	public class SimpleModel extends AbstractTableModel{
		private TokensDictionary mapa;
		Vector<ParserVal> data = new Vector<ParserVal>();
		String[] cabeceras = {"nro linea",
							  "token id",
							  "lexema"};
		int countColumns = 0;
		
		public void erase(){
			data.removeAllElements();
		}
		
		public SimpleModel(){
			mapa = new TokensDictionary();
		}
		
		public void addElements(Vector<ParserVal> tokens) {
			data = tokens;
			
		}

		public void addElement(ParserVal token){
			data.add(token);
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
				return data.get(row).row;
			}
			if(col==1){
				return this.mapa.getToken(data.get(row).kind);
			}
			if(col==2){
				return data.get(row).sval;
			}
			return null;
		}
		
	}
	
}
