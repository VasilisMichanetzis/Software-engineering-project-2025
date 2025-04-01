package gui1;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class toppanel extends JPanel{
	
	toppanel()
	{
		
		this.setBackground(Color.green);
		this.setPreferredSize(new Dimension(150,150));
		Border border = BorderFactory.createLineBorder(Color.BLACK, 3);
	    this.setBorder(border);
		
		
		
		
	}

}
