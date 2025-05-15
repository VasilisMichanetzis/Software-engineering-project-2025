package gui1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Gui extends JFrame{

		
	Gui()
	{	
			
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(1200, 800);
			this.setVisible(true);
			this.getContentPane().setBackground(new Color(255,255,255));
			
			try {
			    Image logo = ImageIO.read(getClass().getResource("/gui1/blocko2.png"));
			    this.setIconImage(logo);
			} catch (Exception e) {
			    e.printStackTrace();
			}
			
			Canvas codepanel = new Canvas();
			this.add(codepanel,BorderLayout.CENTER);
			
			Blockpanel blockpanel = new Blockpanel(codepanel);
			this.add(blockpanel,BorderLayout.WEST);
			
			Output outpanel = new Output();
			this.add(outpanel,BorderLayout.SOUTH);
			
			Declaration declpanel = new Declaration(outpanel);
			this.add(declpanel,BorderLayout.NORTH);
	}
}


