package hipert.hg.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import hipert.hg.Globals;
import hipert.hg.frontend.amalthea.main;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;

public class SwtPropertyPage extends Shell {
	static SwtPropertyPage shell = null;
	private Text outputFolderText;
	private Text runtimePathText;
	private Text grapvizText;
	public static void Show(SwtGui mainShell) {
		try {
			Display display = Display.getDefault();
			shell = new SwtPropertyPage(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			mainShell.onException(e);
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public SwtPropertyPage(Display display) {
		super(display, SWT.SHELL_TRIM);
		setImage(SWTResourceManager.getImage(SwtPropertyPage.class, "/hipert/hg/res/LogoHGT32x32.png"));
		setLayout(new GridLayout(1, false));
		
		Group foldersGroup = new Group(this, SWT.NONE);
		foldersGroup.setText("Folders");
		foldersGroup.setLayout(new GridLayout(1, false));
		foldersGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		
		Composite outputFolderComposite = new Composite(foldersGroup, SWT.NONE);
		outputFolderComposite.setLayout(new GridLayout(3, false));
		outputFolderComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label outputFolderLabel = new Label(outputFolderComposite, SWT.NONE);
		outputFolderLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		outputFolderLabel.setText("Output folder");
		
		outputFolderText = new Text(outputFolderComposite, SWT.BORDER);
		outputFolderText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button outputFolderBtn = new Button(outputFolderComposite, SWT.NONE);
		outputFolderBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectOutputFolder();
			}
		});
		outputFolderBtn.setText("Select");
		
		Composite runtimePathComposite = new Composite(foldersGroup, SWT.NONE);
		runtimePathComposite.setLayout(new GridLayout(3, false));
		runtimePathComposite.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		
		Label runtimePathLabel = new Label(runtimePathComposite, SWT.NONE);
		runtimePathLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		runtimePathLabel.setText("Runtime path");
		
		runtimePathText = new Text(runtimePathComposite, SWT.BORDER);
		runtimePathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button runtimePathBtn = new Button(runtimePathComposite, SWT.NONE);
		runtimePathBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectRuntimePath();
			}
		});
		runtimePathBtn.setText("Select");
		
		Group toolsGroup = new Group(this, SWT.NONE);
		toolsGroup.setText("Tools");
		toolsGroup.setLayout(new GridLayout(1, false));
		toolsGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		
		Composite graphvizComposite = new Composite(toolsGroup, SWT.NONE);
		graphvizComposite.setLayout(new GridLayout(3, false));
		graphvizComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Label graphvizLabel = new Label(graphvizComposite, SWT.NONE);
		graphvizLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		graphvizLabel.setText("Graphviz");
		
		grapvizText = new Text(graphvizComposite, SWT.BORDER);
		grapvizText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button graphVizBtn = new Button(graphvizComposite, SWT.NONE);
		graphVizBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectGraphvizPath();
			}
		});
		graphVizBtn.setSize(52, 30);
		graphVizBtn.setText("Select");
		
		Composite spacerComposite = new Composite(this, SWT.NONE);
		spacerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		
		Composite bottomComposite = new Composite(this, SWT.NONE);
		bottomComposite.setLayout(new FormLayout());
		bottomComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
		
		Button btnSave = new Button(bottomComposite, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				savePreferences();
				shell.close();
			}
		});
		FormData fd_btnSave = new FormData();
		btnSave.setLayoutData(fd_btnSave);
		btnSave.setText("Save and Close");
		
		Button btnClose = new Button(bottomComposite, SWT.NONE);
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		fd_btnSave.top = new FormAttachment(btnClose, 0, SWT.TOP);
		fd_btnSave.right = new FormAttachment(btnClose, -9);
		FormData fd_btnClose = new FormData();
		fd_btnClose.top = new FormAttachment(0, 5);
		fd_btnClose.left = new FormAttachment(0, 647);
		btnClose.setLayoutData(fd_btnClose);
		btnClose.setText("Close");
		createContents();
		updateText();
	}
	
	protected void updateText()
	{
		this.outputFolderText.setText(Globals.defaultOutputDir);
		this.runtimePathText.setText(Globals.defaultRuntimeDir);
		this.grapvizText.setText(Globals.defaultGraphvizDir);		
	}
	
	protected String selectPath(String var)
	{
		String folder = System.getProperty("user.dir"); // Current
		DirectoryDialog dialog = new DirectoryDialog(shell);
		String result = dialog.open();
		if(result != null)
			var = result;
		return var;
	}

	protected void selectOutputFolder() {
		selectPath(Globals.defaultOutputDir);
		updateText();
	}

	protected void selectRuntimePath() {
		selectPath(Globals.defaultRuntimeDir);
		updateText();
	}

	protected void selectGraphvizPath() {
		selectPath(Globals.defaultGraphvizDir);
		updateText();
	}

	protected void savePreferences() {
		
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Properties");
		setSize(728, 436);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing  SWT components
	}
}
