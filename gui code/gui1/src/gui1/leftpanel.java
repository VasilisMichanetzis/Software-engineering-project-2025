package gui1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class leftpanel extends JPanel{
	
	 private mainpanel codepanel;
	
	
	leftpanel(mainpanel codepanel)
	{
		this.codepanel = codepanel;
		this.setBackground(Color.red);
		this.setPreferredSize(new Dimension(200,200));
		this.setLayout(null);
		
		Border border = BorderFactory.createLineBorder(Color.BLACK, 3);
	    this.setBorder(border);
	     
	     
		varbutton button1 = new varbutton();
		
		
	    button1.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                codepanel.addDragPanel(); // Call method in MainPanel
	            }
	        });
		
		
		this.add(button1);
		
		
		
	}
	
}
