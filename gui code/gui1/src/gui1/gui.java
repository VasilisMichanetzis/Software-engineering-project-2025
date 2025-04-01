package gui1;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;

public class gui extends JFrame{

		
	gui()
	{	
			
			this.setTitle("test");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(1200, 800);
			this.setVisible(true);
			this.getContentPane().setBackground(new Color(255,255,255));
			
			mainpanel codepanel = new mainpanel();
			this.add(codepanel,BorderLayout.CENTER);
			leftpanel varpanel = new leftpanel(codepanel);
			this.add(varpanel,BorderLayout.WEST);
			toppanel declpanel = new toppanel();
			this.add(declpanel,BorderLayout.NORTH);
			botpanel outpanel = new botpanel();
			this.add(outpanel,BorderLayout.SOUTH);
			
			
			
	}
		
		
}


