package gui1;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class botpanel extends JPanel{
	
	botpanel()
	{
		
		this.setBackground(Color.pink);
		this.setPreferredSize(new Dimension(150,100));
		Border border = BorderFactory.createLineBorder(Color.BLACK, 3);
	    this.setBorder(border);
		
		
		
		
	}

}
