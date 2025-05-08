package gui1;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class EndIfBlock extends Dragpanel{

	public int ifpos;
	
	
	
	EndIfBlock()
	{
		this.type="endif";
		
		this.setLayout(null);
        
		this.setBackground(Color.magenta);
		
		 JLabel label1 = new JLabel("EndIf");
	     label1.setFont(new Font("Arial", Font.BOLD, 20));
	     label1.setBounds(20, 27, 130, 60);
	     this.add(label1);
		
	}
	
}
