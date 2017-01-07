package hipert.hg.gui.old;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class GuiCodeGenerator extends JFrame{
	
	ImageIcon imgIcon,imgHeader,checkIcon,uncheckIcon;
	JLabel labelCheck1,labelCheck2,labelUncheck1,labelUncheck2;
	Boolean dagSelected,pathSelected;
	File[] dagFiles;
	File pathDst;
	private MemAccessDialog memWindow = new MemAccessDialog(this);

	public static void main(String[] args) {

		new GuiCodeGenerator();
	}

	public GuiCodeGenerator() 
	{   
		setLayout(new FlowLayout());
		JFrame guiFrame = new JFrame();
		
		dagSelected=false;
		pathSelected=false;
		imgIcon = new ImageIcon("./imgs/icon_hipert.png");
		imgHeader = new ImageIcon("./imgs/logo_hipert2.png");
		checkIcon = new ImageIcon("./imgs/icon_check.png");
		uncheckIcon = new ImageIcon("./imgs/icon_uncheck.png");
		labelCheck1=new JLabel(checkIcon);
		labelCheck2=new JLabel(checkIcon);
		labelUncheck1=new JLabel(uncheckIcon);
		labelUncheck2=new JLabel(uncheckIcon);
		
		//make sure the program exits when the frame closes
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setSize(900,350);

		JLabel labelHeader=new JLabel(imgHeader);
		guiFrame.add(labelHeader,BorderLayout.NORTH);

		guiFrame.setIconImage(imgIcon.getImage());
		//This will center the JFrame in the middle of the screen
		guiFrame.setLocationRelativeTo(null);

		JButton codeGenBut = new JButton("Generate code");
		guiFrame.add(codeGenBut,BorderLayout.SOUTH);
		codeGenBut.addActionListener(new generateCodeHandler());

		JButton pathSrc = new JButton("Select file/s");
		pathSrc.addActionListener(new SrcButtonHandler());

		JButton pathDst = new JButton("Select folder");
		pathDst.addActionListener(new DstButtonHandler());

		JPanel p1 = new JPanel(new GridLayout(1,0));
		p1.add(pathSrc);
		p1.add(labelUncheck1);

		//p1.add(new JLabel("Folder code destination"));
		//p1.add(pathDst);
		//p1.add(labelUncheck2);

		JPanel p3 = new JPanel(new FlowLayout());
		p3.add(p1);
		guiFrame.add(p3,BorderLayout.CENTER);


		//make sure the JFrame is visible
		guiFrame.setTitle("HipertLab Code generator");
		guiFrame.setVisible(true);
		pack();
	}

	class SrcButtonHandler implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			openDagFiles();
		}
	}
	
	class DstButtonHandler implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			openPathDestination();
		}
	}
	
	class generateCodeHandler implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if(!dagSelected)
		        JOptionPane.showMessageDialog(null, "Insert dag and code destination", "Code Generator", JOptionPane.WARNING_MESSAGE);
			else{
				//show the list 
				String[] components = {"PREM", "Random_1","Random_8",
						"Random_16", "Random_32", "Random_64"};
				JList lstComp = new JList(components);
				/*super("Lista y Area de texto con eventos");
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				taDescrip.setLineWrap(true);
				taDescrip.setWrapStyleWord(true);
				lstComp.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
				lstComp.setSelectedIndex(0);
				JScrollPane spLista = new JScrollPane(lstComp,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.				HORIZONTAL_SCROLLBAR_AS_NEEDED);
				JScrollPane spTexto = new JScrollPane(taDescrip);*/
		        System.out.println(memWindow.showWindow(dagFiles));
			}
		}
	}

	public void openDagFiles(){

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./"));
		fileChooser.setMultiSelectionEnabled(true);

		// Show the dialog; wait until dialog is closed
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			dagFiles = fileChooser.getSelectedFiles();
			for(File f:dagFiles)
				System.out.println("Selected file: " + f.getAbsolutePath());
			labelUncheck1.setIcon(checkIcon);
			dagSelected=true;
		}
	}
	
	public void openPathDestination(){

		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File(".")); // start at application current directory
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showSaveDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
		    pathDst = fc.getSelectedFile();
		    System.out.println("Selected path: " + pathDst.getAbsolutePath());
			labelUncheck2.setIcon(checkIcon);
			pathSelected=true;
		}
	}
  
}