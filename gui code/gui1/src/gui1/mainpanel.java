package gui1;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class mainpanel extends JPanel{
	private int dragPanelCount = 0;
	
	mainpanel()
	{
		
		this.setBackground(Color.cyan);
		this.setPreferredSize(new Dimension(150,150));
		Border border = BorderFactory.createLineBorder(Color.BLACK, 3);
	    this.setBorder(border);
	    this.setLayout(null);
	    
		
		
	}

	 public void addDragPanel() {

		 if (dragPanelCount == 5) {dragPanelCount=0;}
		 
	        Dragpanel block = new Dragpanel();
	        block.setBounds(20 + (dragPanelCount * 20), 20 + (dragPanelCount * 20), 250, 100); // Staggered positions

	        this.add(block);
	        this.revalidate();
	        this.repaint();

	        dragPanelCount++; // Track added panels
	    }
}
