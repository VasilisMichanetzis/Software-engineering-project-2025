package gui1;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class EndWhileBlock extends Dragpanel{

	public int whilepos;
	
	
	
	EndWhileBlock()
	{
		this.type="endwhile";
		
		this.setLayout(null);
        
		this.setBackground(Color.orange);
		
		 JLabel label1 = new JLabel("EndWhile");
	     label1.setFont(new Font("Arial", Font.BOLD, 20));
	     label1.setBounds(20, 27, 130, 60);
	     this.add(label1);
		
	}
	
}
