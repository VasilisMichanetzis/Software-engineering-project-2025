package gui1;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class EndBlock extends Dragpanel{

	
	EndBlock(){
		
		this.type="end";
		
		this.setLayout(null);
        
		this.setBackground(Color.green);
		
        JLabel label = new JLabel("End");
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setBounds(25, 35, 130, 60);
        this.add(label);
        
        
	}
	
}