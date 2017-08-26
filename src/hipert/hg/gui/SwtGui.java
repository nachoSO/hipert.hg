package hipert.hg.gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.awt.Color;
import java.io.File;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Text;

import hipert.hg.Globals;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.wb.swt.SWTResourceManager;
import org.omg.CORBA.Environment;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.TouchListener;
import org.eclipse.swt.events.TouchEvent;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.layout.RowLayout;

public class SwtGui {

	protected Shell shell;
	private Text statusText;

	static SwtGui window = null;
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
	Group grpOutput = null;
	private Table table;
	
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
		shell.setImage(SWTResourceManager.getImage(SwtGui.class, "/hipert/hg/res/LogoHGT32x32.png"));
		shell.setSize(814, 469);
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
		
		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeEverything();
				System.exit(0);
			}
		});
		mntmExit.setText("Exit");	
		
		MenuItem mntmTools = new MenuItem(menu, SWT.CASCADE);
		mntmTools.setText("Tools");
		
		Menu menu_3 = new Menu(mntmTools);
		mntmTools.setMenu(menu_3);
		
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
		composite.setLayout(new GridLayout(1, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
		gd_composite.heightHint = 269;
		composite.setLayoutData(gd_composite);
		
		Composite btnComposite = new Composite(composite, SWT.NONE);
		btnComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		Button btnAddDag = new Button(btnComposite, SWT.NONE);
		btnAddDag.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showFileExplorer();				
			}
		});
		btnAddDag.setText("Add DAG");
		btnAddDag.setImage(SWTResourceManager.getImage(SwtGui.class, "/hipert/hg/res/icon_plus.png"));
		
		Button btnClearList = new Button(btnComposite, SWT.NONE);
		btnClearList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearDags();
			}
		});
		btnClearList.setText("Clear");
		btnClearList.setImage(SWTResourceManager.getImage(SwtGui.class, "/hipert/hg/res/icon_del.png"));
		
		Button btnTest = new Button(btnComposite, SWT.NONE);
		btnTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onBtnTestClicked("CIAO");
			}
		});
		btnTest.setText("TEST");
		
		table = new Table(composite, SWT.FULL_SELECTION | SWT.MULTI);
		table.setEnabled(false);
		table.setLinesVisible(true);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_table.widthHint = 388;
		table.setLayoutData(gd_table);
		table.setBounds(0, 0, 89, 51);
		table.setHeaderVisible(true);
//		Device device = Display.getCurrent ();
//		table.setBackground(new org.eclipse.swt.graphics.Color(device, 255, 255, 255));
		
		TableColumn tblclmnFile = new TableColumn(table, SWT.NONE);
		tblclmnFile.setWidth(265);
		tblclmnFile.setText("DAG file");
		
		TableColumn tblclmnType = new TableColumn(table, SWT.NONE);
		tblclmnType.setWidth(57);
		tblclmnType.setText("Type");
		
		TableColumn tblclmnEdit = new TableColumn(table, SWT.NONE);
		tblclmnEdit.setWidth(85);
		tblclmnEdit.setText("Actions");
		
		grpOutput = new Group(shell, SWT.NONE);
		GridData gd_grpOutput = new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1);
		gd_grpOutput.heightHint = 88;
		grpOutput.setLayoutData(gd_grpOutput);
		grpOutput.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				
			}
		});
		grpOutput.setText("Output");
		grpOutput.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		statusText = new Text(grpOutput, SWT.READ_ONLY | SWT.MULTI);

	}
	
	protected void clearDags() {
//		int count = this.table.getItemCount();
//		for(int i=0; i<count; i++)
//			this.table.remove(count-i-1);
//		 this.table.redraw();
		table.setRedraw(true);
		table.removeAll();
		table.redraw();
	}

	protected void showPreferencesWindow()
	{
		SwtPropertyPage.Show(this);
	}
	
	protected void showAboutPage()
	{
		SwtAboutDialog dlg = new SwtAboutDialog(this.shell, SWT.CLOSE);
        dlg.open();
	}
	
	protected void showFileExplorer() {
		String folder = System.getProperty("user.dir"); // Current
		String [] extensions = new String [] {"*.dot", "*.jpg"}; // TODO ask to the frontends
		
		FileDialog dialog = new FileDialog(shell, SWT.MULTI);
		dialog.setFilterExtensions(extensions);
		dialog.setFilterPath(folder);
		String result = dialog.open();
		if(result != null)
		{
			String[] names = dialog.getFileNames();
			for (int i = 0, n = names.length; i < n; i++) {
				// Add empty item
				TableItem newItem = new TableItem(this.table, SWT.NONE);
				TableItem[] items = this.table.getItems();

				// First column: name
				TableEditor editor = new TableEditor(table);
				Text text = new Text(table, SWT.NONE);
				text.setText(names[i]);
				editor.grabHorizontal = true;
				editor.setEditor(text, items[items.length - 1], 0);
				editor = new TableEditor(table);

				// Second column: ext
				editor = new TableEditor(table);
				text = new Text(table, SWT.NONE);
				String []split = names[i].split("\\.");
				text.setText(split[split.length - 1]);
				editor.grabHorizontal = true;
				editor.setEditor(text, items[items.length - 1], 1);
				editor = new TableEditor(table);
				
				// Third column: operations
				editor = new TableEditor(table);
				Button btn = new Button(table, SWT.NONE);
				btn.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						//showFileExplorer();
					}
				});
				btn.setImage(SWTResourceManager.getImage(SwtGui.class, "/hipert/hg/res/icon_del.png"));
				editor.grabHorizontal = true;
				editor.setEditor(btn, items[items.length - 1], 2);
				
//				CCombo combo = new CCombo(table, SWT.NONE);
//				editor.grabHorizontal = true;
//				editor.setEditor(combo, items[items.length - 1], 2);

				String fullName = folder + File.separator + names[i];
				showText("Added DAG from file " + fullName);				
			}
			
			
		}
		
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
	
	protected void disposeEverything() {
		
	}
	
	protected void onBtnTestClicked(String fileName)
	{
		// Add empty item
		TableItem newItem = new TableItem(this.table, SWT.NONE);
				
		TableItem[] items = this.table.getItems();
		TableEditor editor = new TableEditor(table);
		
		Text text = new Text(table, SWT.NONE);
		text.setText(fileName);
		editor.grabHorizontal = true;
		editor.setEditor(text, items[items.length - 1], 1);
		editor = new TableEditor(table);
		
		CCombo combo = new CCombo(table, SWT.NONE);
		editor.grabHorizontal = true;
		editor.setEditor(combo, items[items.length - 1], 0);
		editor = new TableEditor(table);
	}

	protected void showText(String text)
	{
		if(statusText != null)
			statusText.append(text + "\r\n");
	}
	
	public void onException(Exception ex)
	{
		showText(ex.getMessage());
	}
}
