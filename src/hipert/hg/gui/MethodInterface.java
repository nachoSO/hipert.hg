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

import hipert.hg.backend.ptask.DagToCode;
import hipert.hg.frontend.rtdot.XMLGenerator;

public class MethodInterface extends JDialog {
	
	public static File[] loadFiles(hgGUI hgGUI,boolean multiSelection){
		File[] files = null;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./"));
		fileChooser.setMultiSelectionEnabled(multiSelection);

		// Show the dialog; wait until dialog is closed
		int result = fileChooser.showOpenDialog(hgGUI);
		if (result == JFileChooser.APPROVE_OPTION) {
			files = fileChooser.getSelectedFiles();
			for(File f:files){
				
				System.out.println("Selected file: " + f.getAbsolutePath());
			}
		}
		
		return files;
	}
	
	public static File loadFile(hgGUI hgGUI,boolean multiSelection){
		File file;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./"));
		fileChooser.setMultiSelectionEnabled(multiSelection);

		// Show the dialog; wait until dialog is closed
		int result = fileChooser.showOpenDialog(hgGUI);
		file=fileChooser.getSelectedFile();

		return file;
	}
	
	public static File loadFolder(hgGUI hgGUI,boolean multiSelection){
		File file;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./"));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// Show the dialog; wait until dialog is closed
		int result = fileChooser.showOpenDialog(hgGUI);
		file=fileChooser.getSelectedFile();

		return file;
	}


}
