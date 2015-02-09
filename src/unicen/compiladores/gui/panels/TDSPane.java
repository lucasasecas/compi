package unicen.compiladores.gui.panels;

import java.util.Dictionary;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import unicen.compiladores.gui.utils.TokensDictionary;
import Utils.TablaSimbolo;
import Utils.TuplaTablaSimbolos;

public class TDSPane extends JPanel {
	private JScrollPane jscPnl;
	private JTable table;
	private TokensDictionary mapa = TokensDictionary.getInstance();
	public TDSPane() {
		jscPnl = new JScrollPane();
		table = new JTable();
		table.setModel(new SimpleModel());
		
		jscPnl.setViewportView(table);
		jscPnl.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(jscPnl);
	}
	
	public void generateTable(TablaSimbolo tds){
		table.removeAll();
		((SimpleModel) table.getModel()).erase();
		for(String key : tds.getAllkeys()){
			TuplaTablaSimbolos tupla = tds.getTupla(key);
			((SimpleModel) table.getModel()).addElement(tupla);
		}
		table.updateUI();
	}
	
	public class SimpleModel extends AbstractTableModel{

		Vector<TuplaTablaSimbolos> data = new Vector<TuplaTablaSimbolos>();
		String[] cabeceras = {"valor",
							  "clase",
							  "tipo",
							  "uso"};
		int countColumns = 0;
		
		
		public SimpleModel(){
			
		}
		
		public void erase() {
			data.removeAllElements();
			
		}

		public void addElement(TuplaTablaSimbolos tupla){
			data.add(tupla);
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
		public Object getValueAt(int arg0, int arg1) {
			if(arg1==1){
				TuplaTablaSimbolos tupla = data.get(arg0);
				String cabecera = cabeceras[arg1];
				int clase = tupla.getValue(cabecera)!=null?(Integer)tupla.getValue(cabecera): 0;
				String trad = mapa.getToken(clase);
				return trad +" ("+ clase+")";
			}
			
			return data.get(arg0).getValue(cabeceras[arg1]);
		}
		
	}

}
