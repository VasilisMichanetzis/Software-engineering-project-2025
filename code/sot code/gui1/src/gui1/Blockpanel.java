package gui1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class Blockpanel extends JPanel{
	
	 private Canvas codepanel;
	
	
	Blockpanel(Canvas codepanel)
	{
		this.codepanel = codepanel;
		this.setBackground(Color.red);
		this.setPreferredSize(new Dimension(150,150));
		this.setLayout(null);
		
		Border border = BorderFactory.createLineBorder(Color.BLACK, 3);
	    this.setBorder(border);
	     
	     
		Blockbutton startbutton = new Blockbutton(30,30, "Start");
	    startbutton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                codepanel.addStartBlock(); // Call method in MainPanel
	            }
	        });
		this.add(startbutton);
		
		Blockbutton endbutton = new Blockbutton(30,70, "End");
	    endbutton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                codepanel.addEndBlock(); // Call method in MainPanel
	            }
	        });
		this.add(endbutton);
		
		Blockbutton mathbutton = new Blockbutton(30,110, "Math");
	    mathbutton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                codepanel.addMathBlock(); // Call method in MainPanel
	            }
	        });
		this.add(mathbutton);
		
		
		
		
	}
	
}
