package hipert.hg.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;

import org.antlr.runtime.misc.IntArray;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.epsilon.eol.execute.operations.declarative.IAbstractOperationContributor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import hipert.hg.Globals;
import hipert.hg.backend.IBackend;
import hipert.hg.backend.bostangomp.DagToCodeBostanGomp;
import hipert.hg.backend.ptask.DagToCode;
import hipert.hg.core.RTDag;
import hipert.hg.core.Tools;
import hipert.hg.core.Utils;
import hipert.hg.frontend.IFrontend;
import hipert.hg.frontend.rtdot.DAG;
import hipert.hg.frontend.rtdot.XMLGenerator;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class SwtGui {
	private static class ContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			return new Object[0];
		}
		public void dispose() {
		}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	protected Shell shell;
	private Text statusText;

	static SwtGui window = null;

	Group grpOutput = null;
	Group grpPattern = null;
	TableViewer tableViewer = null;
	Combo comboBackends = null;
	Combo comboStep = null;
	Combo comboScheduling = null; 
	Combo comboPartitioning = null;
	Combo comboStride = null;
	Button btnPrem = null;
	Button btnSparse = null;
	Button btnStrideRandom = null;
	Button btnStrideSequential = null;
	Button btnGenerate = null;
	
	List<IBackend> backends = new ArrayList<IBackend>();
	List<IFrontend> frontends = new ArrayList<IFrontend>();
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			window = new SwtGui();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void loadFrontends() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		File folder = new File(Globals.FrontendsDir);
		File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++)
	    {
	    	if (listOfFiles[i].isFile())
			{
	    	  	String pathToJar = listOfFiles[i].getAbsolutePath();
				JarFile jarFile;
				
				jarFile = new JarFile(pathToJar);
				Enumeration<JarEntry> e = jarFile.entries();
		
				URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
				URLClassLoader cl = URLClassLoader.newInstance(urls);
		
				while (e.hasMoreElements())
				{
					JarEntry je = e.nextElement();
				    if(je.isDirectory() || !je.getName().endsWith(".class"))
				    {
				        continue;
				    }
				    
				    // -6 because of .class
				    String className = je.getName().substring(0,je.getName().length()-6);
				    className = className.replace('/', '.');
//				    System.out.println(className);
				    Class c = cl.loadClass(className);
				    Class[] interfaces = c.getInterfaces();
				    for (Class iface : interfaces)
				    {
						if(iface.getName().equals(IFrontend.class.getName()))
				    	{
//						    System.out.println(interfaces[0].getName());
						    this.frontends.add((IFrontend) c.newInstance());
						    break;
					    }
					}
		
				}
				jarFile.close();
			}
	    }
	}
		

	public SwtGui()
	{
		try
		{
			this.frontends.add(new XMLGenerator());
			this.loadFrontends();
		}
		catch (Exception e)
		{
			onException(e);
		}
		
		// Load backends
		this.backends.add(new DagToCode());
		this.backends.add(new DagToCodeBostanGomp());
		
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		window.showText("Init done");
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				onShellResized(e);
			}
		});
		shell.setMinimumSize(new Point(800, 400));
		shell.setImage(SWTResourceManager.getImage(SwtGui.class, "/hipert/hg/res/LogoHGT_Icon.png"));
		shell.setSize(1084, 588);
		shell.setText("HiPeRT Generator Tool v" + Globals.ProgramVersion);
		shell.setLayout(new GridLayout(1, false));
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		MenuItem mntmOpen = new MenuItem(menu_1, SWT.NONE);
		mntmOpen.setText("Open DAG");
		
		new MenuItem(menu_1, SWT.SEPARATOR);
		
		MenuItem mntmGenerate = new MenuItem(menu_1, SWT.NONE);
		mntmGenerate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WarnNotImplemented("Random RT-task generation");
			}
		});
		mntmGenerate.setText("Generate random");
		
		new MenuItem(menu_1, SWT.SEPARATOR);
		
		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeEverything();
				System.exit(0);
			}
		});
		mntmExit.setText("Exit");	
		
		MenuItem mntmEdit = new MenuItem(menu, SWT.CASCADE);
		mntmEdit.setText("Edit");
		
		Menu menu_3 = new Menu(mntmEdit);
		mntmEdit.setMenu(menu_3);
		
		MenuItem mntmPreferences = new MenuItem(menu_3, SWT.NONE);
		mntmPreferences.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showPreferencesWindow();
			}
		});
		mntmPreferences.setText("Preferences");
		
		MenuItem mntmAbout = new MenuItem(menu, SWT.CASCADE);
		mntmAbout.setText("?");
		
		Menu menu_2 = new Menu(mntmAbout);
		mntmAbout.setMenu(menu_2);
		
		MenuItem mntmAboutHgt = new MenuItem(menu_2, SWT.NONE);
		mntmAboutHgt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showAboutPage();
			}
		});
		mntmAboutHgt.setText("About HGT");
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_composite.heightHint = 363;
		composite.setLayoutData(gd_composite);
		
		Composite topLeftCompositeComposite = new Composite(composite, SWT.NONE);
		topLeftCompositeComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		Button btnAddDag = new Button(topLeftCompositeComposite, SWT.NONE);
		btnAddDag.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addDags();				
			}
		});
		btnAddDag.setText("Add DAG");
		btnAddDag.setImage(SWTResourceManager.getImage(SwtGui.class, "/hipert/hg/res/icon_plus.png"));
		
		Button btnClearList = new Button(topLeftCompositeComposite, SWT.NONE);
		btnClearList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearDags();
			}
		});
		btnClearList.setText("Clear");
		btnClearList.setImage(SWTResourceManager.getImage(SwtGui.class, "/hipert/hg/res/icon_del.png"));
		
		Composite topRightComposite = new Composite(composite, SWT.NONE);
		topRightComposite.setLayout(new GridLayout(2, false));
		topRightComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		
		Label lblSelectBackend = new Label(topRightComposite, SWT.NONE);
		lblSelectBackend.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSelectBackend.setText("Available backends");
		
		ComboViewer comboViewer = new ComboViewer(topRightComposite, SWT.READ_ONLY);
		comboBackends = comboViewer.getCombo();
		comboBackends.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableGUIComponents();
				if(comboBackends.getSelectionIndex() == 1) // Bostan
				{
					WarnNotImplemented("Bostan backend");
				}
				enableGUIComponents();
			}
		});
		
		String []backendsFriendlyName = new String[this.backends.size()];
		int i = 0;
		for (Iterator iterator = backends.iterator(); iterator.hasNext();) {
			IBackend iBackend = (IBackend) iterator.next();
			backendsFriendlyName[i++] = iBackend.getFriendlyName();
		}
		comboBackends.setItems(backendsFriendlyName);
		comboBackends.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboBackends.select(0);
		
		Composite centerLeftComposite = new Composite(composite, SWT.NONE);
		centerLeftComposite.setLayout(new GridLayout(1, false));
		GridData gd_centerLeftComposite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_centerLeftComposite.heightHint = 271;
		centerLeftComposite.setLayoutData(gd_centerLeftComposite);
		
		tableViewer = new TableViewer(centerLeftComposite, SWT.BORDER | SWT.FULL_SELECTION);
		dagsTableViewer = tableViewer.getTable();
		GridData gd_dagsTableViewer = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_dagsTableViewer.heightHint = 353;
		dagsTableViewer.setLayoutData(gd_dagsTableViewer);
		dagsTableViewer.setTouchEnabled(true);
		dagsTableViewer.setLinesVisible(true);
		dagsTableViewer.setHeaderVisible(true);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				return null;
			}
			public String getText(Object element) {
				RTDag dag = (RTDag) element;
				String ret = dag.getFileName();
				return ret;
			}
		});
		TableColumn tblclmnDagFile = tableViewerColumn.getColumn();
		tblclmnDagFile.setWidth(100);
		tblclmnDagFile.setText("DAG file");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				return null;
			}
			public String getText(Object element) {
				RTDag dag = (RTDag) element;
				return dag.getType();
			}
		});
		TableColumn tblclmnType = tableViewerColumn_1.getColumn();
		tblclmnType.setWidth(100);
		tblclmnType.setText("Type");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				return null;
			}
			public String getText(Object element) {
				RTDag dag = (RTDag) element;
				return dag.getFileFolder();
			}
		});
		TableColumn tblclmnPath = tableViewerColumn_2.getColumn();
		tblclmnPath.setWidth(217);
		tblclmnPath.setText("Path");
		
		TableViewerColumn tableViewerColumnView = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumnView.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				RTDag dag = (RTDag) element;
				showDagImage(dag);
				
				return false;
			}

			protected CellEditor getCellEditor(Object element) {
			    return new TextCellEditor(tableViewer.getTable());
			}
			protected Object getValue(Object element) {
				return "";
			}
			protected void setValue(Object element, Object value) {
			}
		});
		tableViewerColumnView.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				return SWTResourceManager.getImage(SwtGui.class, "/hipert/hg/res/icon_eye.png");
			}
			public String getText(Object element) {
				return "";
			}
		});
		TableColumn tableColumnView = tableViewerColumnView.getColumn();
		tableColumnView.setWidth(37);
		
		TableViewerColumn tableViewerColumnDelete = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumnDelete.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				dags.remove((RTDag) element);
				tableViewer.refresh();
				return false;
			}
			protected CellEditor getCellEditor(Object element) {
			    return new TextCellEditor(tableViewer.getTable());
			}
			protected Object getValue(Object element) {
				return "";
			}
			protected void setValue(Object element, Object value) {
			}
		});
		tableViewerColumnDelete.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				 return SWTResourceManager.getImage(SwtGui.class, "/hipert/hg/res/icon_del.png");
			}
			public String getText(Object element) {
				return "";
			}
		});
		TableColumn tblclmnDelete = tableViewerColumnDelete.getColumn();
		tblclmnDelete.setWidth(34);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		tableViewer.setInput(dags);
		
		Composite centerRightComposite = new Composite(composite, SWT.NONE);
		GridData gd_centerRightComposite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_centerRightComposite.heightHint = 307;
		centerRightComposite.setLayoutData(gd_centerRightComposite);
		centerRightComposite.setLayout(new GridLayout(1, false));
		
		Group grpThreading = new Group(centerRightComposite, SWT.NONE);
		grpThreading.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpThreading.setText("Threading");
		grpThreading.setLayout(new GridLayout(2, false));
		
		Label lblScheduling = new Label(grpThreading, SWT.NONE);
		lblScheduling.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblScheduling.setText("Scheduling");
		
		ComboViewer comboViewer_1 = new ComboViewer(grpThreading, SWT.READ_ONLY);
		comboScheduling = comboViewer_1.getCombo();
		comboScheduling.setItems(new String[] {"SCHED_FIFO", "SCHED_RR", "SCHED_OTHER"});
		comboScheduling.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboScheduling.select(0);
		
		Label lblPartitioning = new Label(grpThreading, SWT.NONE);
		lblPartitioning.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblPartitioning.setText("Partitioning policy");
		
		ComboViewer comboViewer_2 = new ComboViewer(grpThreading, SWT.READ_ONLY);
		comboPartitioning = comboViewer_2.getCombo();
		comboPartitioning.setItems(new String[] {"GLOBAL", "PARTITIONED"});
		comboPartitioning.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboPartitioning.select(0);
		
		Group grpMemory = new Group(centerRightComposite, SWT.NONE);
		grpMemory.setLayout(new GridLayout(1, false));
		GridData gd_grpMemory = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_grpMemory.heightHint = 139;
		grpMemory.setLayoutData(gd_grpMemory);
		grpMemory.setText("Memory");
		
		Composite memorySelectComposite = new Composite(grpMemory, SWT.NONE);
		memorySelectComposite.setLayout(new GridLayout(3, false));
		memorySelectComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));

		btnSparse = new Button(memorySelectComposite, SWT.RADIO);
		btnSparse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableGUIComponents();
			}
		});
		btnSparse.setSelection(true);
		btnSparse.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSparse.setSize(68, 20);
		btnSparse.setText("Sparse");
		new Label(memorySelectComposite, SWT.NONE);
		
		btnPrem = new Button(memorySelectComposite, SWT.RADIO);
		btnPrem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableGUIComponents();
			}
		});
		btnPrem.setSize(62, 20);
		btnPrem.setText("PREM");
		
		Composite memorySparseComposite = new Composite(grpMemory, SWT.NONE);
		memorySparseComposite.setLayout(new GridLayout(2, false));
		GridData gd_memorySparseComposite = new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
		gd_memorySparseComposite.heightHint = 35;
		memorySparseComposite.setLayoutData(gd_memorySparseComposite);
		
		Label lblStep = new Label(memorySparseComposite, SWT.NONE);
		lblStep.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStep.setText("Step");
		
		ComboViewer comboViewer_3 = new ComboViewer(memorySparseComposite, SWT.READ_ONLY);
		comboStep = comboViewer_3.getCombo();
		comboStep.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		comboStep.setItems(new String[] {"char", "int", "double", "long double"});
		comboStep.select(0);
		
		grpPattern = new Group(grpMemory, SWT.NONE);
		grpPattern.setLayout(new GridLayout(3, false));
		grpPattern.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		grpPattern.setText("Pattern");

		btnStrideSequential = new Button(grpPattern, SWT.RADIO);
		btnStrideSequential.setSelection(true);
		btnStrideSequential.setText("Sequential");
		
		Label lblNewLabel = new Label(grpPattern, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Stride");
		
		ComboViewer comboViewer_4 = new ComboViewer(grpPattern, SWT.READ_ONLY);
		comboStride = comboViewer_4.getCombo();
		comboStride.setItems(new String[] {"1", "2", "4", "8", "16", "32", "64", "128", "256"});
		comboStride.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboStride.select(0);
		
		btnStrideRandom = new Button(grpPattern, SWT.RADIO);
		btnStrideRandom.setText("Random");
		new Label(grpPattern, SWT.NONE);
		new Label(grpPattern, SWT.NONE);
		
		btnGenerate = new Button(centerRightComposite, SWT.NONE);
		btnGenerate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				generateCode();
			}
		});
		btnGenerate.setText("Generate code");
		
		grpOutput = new Group(shell, SWT.NONE);
		GridData gd_grpOutput = new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1);
		gd_grpOutput.heightHint = 71;
		grpOutput.setLayoutData(gd_grpOutput);
		grpOutput.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				
			}
		});
		grpOutput.setText("Output");
		grpOutput.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		statusText = new Text(grpOutput, SWT.READ_ONLY | SWT.MULTI);
		
		enableGUIComponents();
	}
	
	protected void enableGUIComponents() {
		
		// Strided memory pattern
		this.recursiveSetEnabled(this.grpPattern, this.btnSparse.getSelection());
		
		// Button to generate code
		this.btnGenerate.setEnabled(this.dagsTableViewer.getItemCount() != 0);
	}

	protected void WarnNotImplemented(String what) {
		if(what != null)
			MessageDialog.openWarning(shell, "Warning", what + " not implemented yet");
		else
			MessageDialog.openWarning(shell, "Warning", "Not implemented yet");		
	}

	protected void showDagImage(RTDag dag) {
		String imageFileName;
		try {
			imageFileName = Tools.CreateDagImage(dag.getFullFileName());
			new SwtDagImage(shell, SWT.CLOSE, dag.getName(), imageFileName).open();
		} catch (Exception e) {
			onException(e);
		}	
	}
	
	protected void clearDags() {
			this.dags.clear();
			this.tableViewer.refresh();
		
	}

	protected void showPreferencesWindow()
	{
		SwtPropertyPage.Show(this);
	}
	
	protected void showAboutPage()
	{
		new SwtAboutDialog(this.shell, SWT.CLOSE).open();
	}
	
	List<RTDag> dags = new ArrayList<RTDag>();
	private Table dagsTableViewer;
	
	protected void addDags()
	{
		String folder = System.getProperty("user.dir"); // Current
		
		List<String> extensions= new ArrayList<String>();
		for (IFrontend f: this.frontends)
		{
			for (String s : f.getFileExtensions())
				extensions.add(s);
		}

		String []filterExtensions = extensions.toArray(new String[0]);
		FileDialog dialog = new FileDialog(shell, SWT.MULTI);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(folder);
		String result = dialog.open();
		if(result != null)
		{
			String[] names = dialog.getFileNames();
			for (int i = 0, n = names.length; i < n; i++) {
				
				RTDag newDag = new RTDag(names[i], dialog.getFilterPath());
				dags.add(newDag);

				String fullName = newDag.getFullFileName();
				showText("Added DAG from file " + fullName);
			}
			this.tableViewer.refresh();
		}
		enableGUIComponents();
	}

	public void onShellResized(ControlEvent e)
	{
		// TODO resize all components
//		if(grpOutput != null)
//		{
//			Point shellSize = shell.getSize();
//			grpOutput.setSize(shellSize.x, shellSize.y);
//		}
	}
	
	protected void disposeEverything()
	{
		
	}
	
	private DAG[] packDags() {
		int dagsCount = this.dags.size();
		DAG dags[] = new DAG[dagsCount]; // FIXME 

		String sched_policy = this.comboScheduling.getItem(this.comboScheduling.getSelectionIndex());
		String partitioning_policy = this.comboPartitioning.getItem(this.comboPartitioning.getSelectionIndex());
		
		for(int i=0; i<dagsCount; i++) {
			RTDag currDag = this.dags.get(i);
			String filePath = currDag.getFullFileName();

			String stepString = this.comboStep.getItem(this.comboStep.getSelectionIndex());
			int step = Utils.CalcStep(stepString);
			
			if(btnSparse.getSelection()) {
				int stride = 0; //random access
				
				if(this.btnStrideSequential.getSelection()) {
					String strideString = this.comboStride.getItem(this.comboStride.getSelectionIndex());
					stride = Integer.parseInt(strideString);
				}
				else if(this.btnStrideRandom.getSelection()) {
					stride = 0;
				}
				else {
					// Error
				}
				
				dags[i] = new DAG(filePath, "sparse", step, stride, sched_policy, partitioning_policy, Globals.OutputDir);
			}
			
			else if(btnPrem.getSelection()) {
				dags[i] = new DAG(filePath, "prem", step, sched_policy, partitioning_policy, Globals.OutputDir);
			}
		
		}
		return dags;
	}
	
	protected IFrontend getParser(String fileExt)
	{
		for (IFrontend ifend : this.frontends)
			if(Arrays.asList(ifend.getFileExtensions()).contains(fileExt))
				return ifend;
		return null;
	}
	
	protected void generateCode()
	{  
		// We assume they are all equal!
	    String ext = this.dags.get(0).getType();		
	    IFrontend parser = getParser("*." + ext);
		if(parser == null)
		{
			System.out.println("Unable to find a parser for " + ext + " file");
			return;
		}
			
		//String modelTempFile = Globals.GetTempDir() + Globals.ModelTempFileName;
		String modelTempFile="./modelToCode/dagParsed.model"; // FIXME
		parser.Parse(packDags(), modelTempFile);

		IBackend codeGenerator = this.backends.get(this.comboBackends.getSelectionIndex());
		
		ArrayList<String> fileNames = new ArrayList<String>();
		try
		{
			for (RTDag dag: this.dags)
				fileNames.add(dag.getFullFileName());
			
			codeGenerator.GenerateCode(modelTempFile);

			MessageDialog.openInformation(shell, "Done", "Code generated");
			if(codeGenerator.Post(fileNames))			
				MessageDialog.openInformation(shell, "Done", "Executed post operations");
		}
		catch(Exception ex) {
	        JOptionPane.showMessageDialog(null, ex.getMessage(), "Code Generator", JOptionPane.ERROR_MESSAGE);			
		}
		
	
	}

	protected void showText(String text)
	{
		if(statusText != null)
			statusText.append(text + "\r\n");
	}
	
	private static MultiStatus createMultiStatus(String msg, Throwable t)
	{
        List<Status> childStatuses = new ArrayList<>();
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();

        for (StackTraceElement stackTrace: stackTraces)
        {
            Status status = new Status(IStatus.ERROR,
                    "com.example.e4.rcp.todo", stackTrace.toString());
            childStatuses.add(status);
        }

        MultiStatus ms = new MultiStatus("com.example.e4.rcp.todo",
                IStatus.ERROR, childStatuses.toArray(new Status[] {}),
                t.toString(), t);
        return ms;
    }
	
	public void onException(Exception ex)
	{
		try
		{
			// build the error message and include the current stack trace
			MultiStatus status = createMultiStatus(ex.getLocalizedMessage(), ex);
			// show error dialog
			ErrorDialog.openError(shell, "Error", "This is an error", status);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			showText("Impossible to open Error Dialog. Cause: " + e.getLocalizedMessage());
		}
		ex.printStackTrace();
	}
	
	public void recursiveSetEnabled(Control ctrl, boolean enabled) {
		   if (ctrl instanceof Group) {
			   Group comp = (Group) ctrl;
		      for (Control c : comp.getChildren())
		         recursiveSetEnabled(c, enabled);
		   } else {
		      ctrl.setEnabled(enabled);
		   }
		}
}
