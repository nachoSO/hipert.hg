package hipert.hg.gui.old;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.eclipse.uml2.uml.Enumeration;

import hipert.hg.XMLParser.XMLGenerator;
import hipert.hg.modelToCode.DagToCode;

@SuppressWarnings("serial")
public class MemAccessDialog extends JDialog {
	
	private JButton bAccept = new JButton("Aceptar");
	private File[] dagFiles;
    static String premString = "prem";
    static String RandomString_1 = "random_1";
    static String RandomString_8 = "random_8";
    static String RandomString_16 = "random_16";
    static String RandomString_32 = "random_32";
    static String RandomString_64 = "random_64";
    private ButtonGroup group = new ButtonGroup();
    private String memory_access = "prem";
    		
	public MemAccessDialog(JFrame owner) {
		super(owner, "Memory access", true);
	    
		JRadioButton premRButton = new JRadioButton(premString);
		premRButton.setActionCommand(premString);
		premRButton.setSelected(true);
		
		JRadioButton randomRButton_1 = new JRadioButton(RandomString_1);
		randomRButton_1.setActionCommand(RandomString_1);

		JRadioButton randomRButton_8 = new JRadioButton(RandomString_8);
		randomRButton_8.setActionCommand(RandomString_8);

		JRadioButton randomRButton_16 = new JRadioButton(RandomString_16);
		randomRButton_16.setActionCommand(RandomString_16);

		JRadioButton randomRButton_32 = new JRadioButton(RandomString_32);
		randomRButton_32.setActionCommand(RandomString_32);

		JRadioButton randomRButton_64 = new JRadioButton(RandomString_64);
		randomRButton_64.setActionCommand(RandomString_64);

        group.add(premRButton);
        group.add(randomRButton_1);
        group.add(randomRButton_8);
        group.add(randomRButton_16);
        group.add(randomRButton_32);
        group.add(randomRButton_64);

        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        radioPanel.add(premRButton);
        radioPanel.add(randomRButton_1);
        radioPanel.add(randomRButton_8);
        radioPanel.add(randomRButton_16);
        radioPanel.add(randomRButton_32);
        radioPanel.add(randomRButton_64);

        add(radioPanel,BorderLayout.CENTER);
		
        this.setSize(300,150);
		this.setLocationRelativeTo(null);

		add(bAccept, BorderLayout.SOUTH);
		pack();
		setResizable(false);
		bAccept.addActionListener(new acceptHandler());
	}
		
	public String showWindow(File[] dagFiles) {
		this.dagFiles=dagFiles;
		setVisible(true); 
		return memory_access;
	}
	
	
	class acceptHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			memory_access = group.getSelection().getActionCommand();
			JOptionPane.showMessageDialog(null, "Code Generated", "Code Generator", JOptionPane.INFORMATION_MESSAGE);
	        //new XMLGenerator(dagFiles,memory_access);
	        new DagToCode();
			setVisible(false);
		}
	}
}
