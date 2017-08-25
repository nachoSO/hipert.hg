package hipert.hg.gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Text;

import hipert.hg.Globals;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.FillLayout;

public class SwtGui {

	protected Shell shell;
	private Text statusTex;

	static SwtGui window = null;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			window = new SwtGui();
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					
					try {
						Thread.sleep(10000L);
					} catch (InterruptedException e) {
					}
					window.showText("Init done");
					
				}
			});
			
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
		shell.setSize(450, 300);
		shell.setText("HiPeRT Generator Tool v" + Globals.ProgramVersion);
		shell.setLayout(new BorderLayout(0, 0));
		
		Group grpOutput = new Group(shell, SWT.NONE);
		grpOutput.setText("Output");
		grpOutput.setLayoutData(BorderLayout.SOUTH);
		grpOutput.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		statusTex = new Text(grpOutput, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI);

	}
	
	protected void showText(String text)
	{
		if(statusTex != null)
			statusTex.append(text);
	}
}
