package unicen.compiladores.gui.panels;

import gc_Assembler.NodoArbol;

import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import unicen.compiladores.gui.ParserError;
import unicen.compiladores.gui.Sentencia;
import unicen.compiladores.gui.panels.ErrorPane.SimpleModel;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class ArbolPane extends JPanel{

	private JScrollPane jscPnl;
	private JTree table;

	public ArbolPane(){
		
		jscPnl = new JScrollPane();
		
		DefaultTreeModel modelo = new  DefaultTreeModel(new DefaultMutableTreeNode("Arbol"));
		table = new JTree(modelo);
		jscPnl.setViewportView(table);
		jscPnl.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(jscPnl, GroupLayout.PREFERRED_SIZE, 479, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(jscPnl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
	}
	
	public void generateTable(NodoArbol nodoArbol){
		table.removeAll();
		DefaultMutableTreeNode result = this.popularArbol(nodoArbol);
		DefaultTreeModel modelo = (DefaultTreeModel) table.getModel();
		modelo.insertNodeInto(result, (MutableTreeNode) modelo.getRoot(), 0);
		table.updateUI();
	}

	private DefaultMutableTreeNode popularArbol(NodoArbol nodoArbol) {
		if(nodoArbol==null) return null;
		DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(nodoArbol.getValue());
		if(!nodoArbol.esHoja()){
			DefaultMutableTreeNode hijo1 = popularArbol(nodoArbol.getHijoIzq());
			DefaultMutableTreeNode hijo2 = popularArbol(nodoArbol.getHijoDer());
			DefaultTreeModel modelo = (DefaultTreeModel) table.getModel();
			int countIndex = 0;
			if(hijo1!=null)
				modelo.insertNodeInto(hijo1, nodo, countIndex++);
			if(hijo2!=null)
				modelo.insertNodeInto(hijo2, nodo, countIndex);
		}
		
		return nodo;
		
	}
	
	
	
}
