package unicen.compiladores.gui;

import gc_Assembler.GeneradorAssembler;
import gc_Assembler.NodoArbol;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JToolBar;

import Utils.TablaSimbolo;
import al_Main.AnalizadorLexico;
import as_Parser.Parser;
import unicen.compiladores.gui.panels.ArbolPane;
import unicen.compiladores.gui.panels.ErrorPane;
import unicen.compiladores.gui.panels.SentenciasPane;
import unicen.compiladores.gui.panels.SourcePane;
import unicen.compiladores.gui.panels.TDSPane;
import unicen.compiladores.gui.panels.TokensPane;
import unicen.compiladores.gui.resources.Icons;

public class Gui extends JFrame implements ActionListener, MouseListener{

	private JPanel contentPane;
	private JToolBar toolBar;
	private JTabbedPane tabPane;
	private JButton saveBtn;
	private JButton btnNew;
	private JButton btnLoad;
	private JButton btnRun;
	private File tmpFile;
	private File file;
	private SourcePane sourcePnl;
	private String  fileName;
	private ErrorManager errorManager;
	private boolean  dirty = false;
	private AnalizadorLexico analizadorLexico;
	private TablaSimbolo tds;
	private Parser parser;
	private TDSPane tdsPnl;
	private TokensPane tokensPnl;
	private ErrorPane errorsPnl;
	private SentenciaManager sentenciasManager;
	private SentenciasPane sentenciasPnl;
	private JButton btnSave;
	private ArbolPane arbolPnl;
	private JDialog dialogoErrores;
	private SourcePane assemblerPnl;
	private String nombreSource;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Gui() {
		setTitle("Compilador");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 

		setBounds(100, 100, 450, 300);
		
		errorManager = new ErrorManager();
		sentenciasManager = new SentenciaManager();
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		this.toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		this.sourcePnl = new SourcePane(this);
		this.tdsPnl = new TDSPane();
		//this.tokensPnl = new TokensPane();
		this.errorsPnl = new ErrorPane();
		//this.sentenciasPnl = new SentenciasPane();
		this.assemblerPnl = new SourcePane(this);

		this.arbolPnl = new ArbolPane();
		
		this.tabPane = new JTabbedPane();
		
		
		this.tabPane.add("Fuente", this.sourcePnl);
		this.tabPane.add("TDS", this.tdsPnl);
		//this.tabPane.add("Tokens", this.tokensPnl);
		this.tabPane.add("Errores", this.errorsPnl);
		//this.tabPane.add("Estructuras Sintacticas", this.sentenciasPnl);
		this.tabPane.add("Arbol Sintactico", this.arbolPnl);
		this.tabPane.add("Codigo Assembler", this.assemblerPnl);
		
		//Boton nuevo
		this.btnNew = new JButton(Icons.NEW);
		this.btnNew.addActionListener(this);
		this.btnNew.setActionCommand("new");
		this.btnNew.setToolTipText("nuevo archivo");
		
		this.toolBar.add(this.btnNew);

		
//		boton correr
		this.btnLoad = new JButton(Icons.LOAD);
		this.btnLoad.addActionListener(this);
		this.btnLoad.setActionCommand("load");
		this.btnLoad.setToolTipText("Cargar archivo");
		
		this.toolBar.add(this.btnLoad);
		
		//boton guardar
		this.btnSave = new JButton(Icons.SAVE);
		this.btnSave.addActionListener(this);
		this.btnSave.setActionCommand("save");
		this.btnSave.setToolTipText("Guardar");
		
		this.toolBar.add(this.btnSave);
		
		//boton Cargar
		this.btnRun = new JButton(Icons.RUN);
		this.btnRun.addActionListener(this);
		this.btnRun.setActionCommand("run");
		this.btnRun.setToolTipText("Ejecutar");
		
		this.toolBar.add(this.btnRun);
		this.add(tabPane, BorderLayout.CENTER);
		
		this.file=null;
		this.tds = new TablaSimbolo();
		
		
		
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command=arg0.getActionCommand();
		switch (command) {
		case "new":
			this.newFile();
			break;
		case "save":
			this.saveFile("txt");
			break;
		case "load":
			this.reset();
			this.loadFile();
			this.setDirty(false);
			break;
		case "run":
			this.run();
			break;
		default:
			break;
		}
		
	}

	private void run() {
		if(!isDirty("Antes de ejectuar se debe guardar los cambios")){
			this.reset();
			NodoArbol nodo = parser.run(nombreSource);
			tdsPnl.generateTable(tds);
			//tokensPnl.generateTable(parser.getTokens());
			errorsPnl.generateTable(errorManager.getAllErrors());
			if (parser.errorManager.noErrors()){
				String nombre = this.getFileName(true, "asm", "Donde desea guardar el archivo?");
				GeneradorAssembler as = new GeneradorAssembler(tds, nombre, nodo.clone());
				arbolPnl.generateTable(nodo);
				loadAssembler(as);
				
			}else{
				JOptionPane.showMessageDialog(this, "Se ha detenido la compilacion debido a que se han encontrado errores",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		
	}

	private void loadAssembler(GeneradorAssembler as) {

		BufferedReader  br;
		try {
			this.assemblerPnl.clean();
			br = new BufferedReader(new FileReader(as.getNombreArchivo()));
			String current="";
			while((current = br.readLine()) != null){
				this.assemblerPnl.append(current+'\n');
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
		}
		
	
		
	}

	private void loadFile() {
		if(!isDirty("¿Desea guardar los cambios?")){
			String name = this.getFileName(false, "", "Seleccionar archivos");
			try {
				BufferedReader  br = new BufferedReader(new FileReader(name));
				String current="";
				while((current = br.readLine()) != null){
					this.sourcePnl.append(current+'\n');
				}
				br.close();
				this.nombreSource = name;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private void reset() {
		this.tds = new TablaSimbolo();
		errorManager = new ErrorManager();
		sentenciasManager = new SentenciaManager();
		this.analizadorLexico = new AnalizadorLexico(tds, errorManager);
		this.parser = new Parser(tds, analizadorLexico, errorManager, sentenciasManager);
	}

	private void cleanScrn() {
		this.sourcePnl.clean();
		
	}

	private void newFile() {
		if(!isDirty("¿Desea guardar los cambios?")){
			 cleanScrn();
		}
		
	}

	private boolean saveFile(String ext) {
		String name;
		if(this.fileName==null){
			name = this.getFileName(true,ext,"Guardar archivo ...");
			nombreSource = name;
		}
		else name = this.nombreSource;
		
		  try {
   		      PrintWriter os = new PrintWriter(new FileOutputStream(name));
   		      String text = sourcePnl.getSource();
	    	  os.write((String)text);
	    	  setDirty(false);
	    	  this.fileName = name;
	    	  os.close();
	 	} catch (IOException e) {
			  JOptionPane.showMessageDialog(this,"Could not save to file "+fileName,"Warning",JOptionPane.WARNING_MESSAGE);
		      return false;  
		}
		return false;
		
	}
	
	private String getFileName(boolean save,String ext,String title) {
	    final JFileChooser fileChooser=new JFileChooser();
	    ext = ext==""?"txt":ext;
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(ext, ext);
	    fileChooser.setFileFilter(filter);
	    fileChooser.setDialogTitle(title);
	    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
	    int result;
	    if (save) result=fileChooser.showSaveDialog(this);
	    else result=fileChooser.showOpenDialog(this);
	    if (result==JFileChooser.APPROVE_OPTION) {
	        String fileName=fileChooser.getSelectedFile().getPath();
	        this.fileName=fileName;
	        // it also works if the next line is commented out!
	        this.fileName=this.fileName.substring(0,this.fileName.lastIndexOf(File.separatorChar));
	        if (save && fileName.indexOf('.')==-1) fileName+="."+ext;
	        if (save && (new File(fileName)).exists())
	            if (JOptionPane.showConfirmDialog(this,"File "+fileName+" already exists. Overwrite?","Warning",
	                JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.NO_OPTION)
	                return null;
	        return fileName;
	    }
	    else return null;
	}
	
    public boolean isDirty(String saveMessage){ 
        if (!dirty) return false;
        int result=JOptionPane.showConfirmDialog(this,saveMessage,"File Modified",
            JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
        switch (result) {
            case JOptionPane.YES_OPTION:
                if (saveFile("txt")) return false;
                break;
            case JOptionPane.NO_OPTION:
                return false;
        }
        return true;
    }
    
    public Object loadFromFile(String fileName) {
        if (fileName==null) return null;
        try {
        	File loadedFile = new File(fileName);
        	return loadedFile;
		 } catch (Exception e) {
			  JOptionPane.showMessageDialog(this,e.getMessage()+fileName,"Error",JOptionPane.ERROR_MESSAGE);
		 }
        return null;
 }

	public void setDirty(boolean val) {
		if(val){
			if(!dirty)
				tabPane.setTitleAt(0, tabPane.getTitleAt(0) + "*");
			dirty = true;
		}else{
			if(dirty)
				tabPane.setTitleAt(0, tabPane.getTitleAt(0).substring(0, tabPane.getTitleAt(0).indexOf("*")));
			dirty = false;
		}
		
		
		
	}

}
