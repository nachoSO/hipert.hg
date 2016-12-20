package hipert.hg.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;

import hipert.hg.XMLParser.XMLGenerator;
import hipert.hg.modelToCode.DagToCode;

public class MethodInterface extends JDialog {
	
	public static File[] loadFiles(hgGUI hgGUI,boolean multiSelection){
		File[] dagFiles = null;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./"));
		fileChooser.setMultiSelectionEnabled(multiSelection);

		// Show the dialog; wait until dialog is closed
		int result = fileChooser.showOpenDialog(hgGUI);
		if (result == JFileChooser.APPROVE_OPTION) {
			dagFiles = fileChooser.getSelectedFiles();
			for(File f:dagFiles)
				System.out.println("Selected file: " + f.getAbsolutePath());
		}
		
		return dagFiles;
	}
	
	public static File loadFile(hgGUI hgGUI,boolean multiSelection){
		File dagFile;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./"));
		fileChooser.setMultiSelectionEnabled(multiSelection);

		// Show the dialog; wait until dialog is closed
		int result = fileChooser.showOpenDialog(hgGUI);
		dagFile=fileChooser.getSelectedFile();

		return dagFile;
	}
	


}
