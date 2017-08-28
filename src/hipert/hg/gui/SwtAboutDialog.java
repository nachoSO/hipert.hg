package hipert.hg.gui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Link;

public class SwtAboutDialog extends Dialog {

	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public SwtAboutDialog(Shell parent, int style) {
		super(parent, style);
		setText("About HGT");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setImage(SWTResourceManager.getImage(SwtAboutDialog.class, "/hipert/hg/res/LogoHGT_Icon.png"));
		shell.setSize(450, 300);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		
		Label lblLogoHiPeRT = new Label(shell, SWT.NONE);
		lblLogoHiPeRT.setText("HiPeRT Image");
		lblLogoHiPeRT.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblLogoHiPeRT.setImage(null);
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		
		Label lblLogoHGT = new Label(composite, SWT.NONE);
		lblLogoHGT.setSize(116, 128);
		lblLogoHGT.setImage(SWTResourceManager.getImage(SwtAboutDialog.class, "/hipert/hg/res/LogoHGT_116x128.png"));
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		composite_1.setSize(424, 80);
		composite_1.setLayout(new GridLayout(1, false));
		
		Label lblTitle = new Label(composite_1, SWT.NONE);
		lblTitle.setText("The HiPeRT Generator Tool");
		
		Label lblSpacer = new Label(composite_1, SWT.NONE);
		
		Link link = new Link(composite_1, SWT.NONE);
		link.setText("<a>https://github.com/HiPeRT/HGT</a>");
		
		Composite bottomComposite = new Composite(shell, SWT.NONE);
		bottomComposite.setLayout(new GridLayout(1, false));
		bottomComposite.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
		
		Button btnClose = new Button(bottomComposite, SWT.NONE);
		btnClose.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnClose.setText("Close");

	}
}
