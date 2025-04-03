package gui1;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;

public class Gui extends JFrame{

		
	Gui()
	{	
			
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(1200, 800);
			this.setVisible(true);
			this.getContentPane().setBackground(new Color(255,255,255));
			
			Canvas codepanel = new Canvas();
			this.add(codepanel,BorderLayout.CENTER);
			
			Blockpanel blockpanel = new Blockpanel(codepanel);
			this.add(blockpanel,BorderLayout.WEST);
			
			Declaration declpanel = new Declaration();
			this.add(declpanel,BorderLayout.NORTH);
			
			Output outpanel = new Output();
			this.add(outpanel,BorderLayout.SOUTH);
			
			
			
	}
		
		
}


