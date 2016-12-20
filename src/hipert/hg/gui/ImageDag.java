package hipert.hg.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImageDag{

	public ImageDag(String string) {
		System.out.println(string);
		JPanel mainPanel = new JPanel(new BorderLayout());
		JFrame frame = new JFrame();
		JLabel lblimage = new JLabel(new ImageIcon(string));
		frame.getContentPane().add(lblimage, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	
	
}
