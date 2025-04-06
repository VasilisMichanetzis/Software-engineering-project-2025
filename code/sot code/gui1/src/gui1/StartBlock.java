package gui1;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class StartBlock extends Dragpanel{

	
	StartBlock(){
		
		this.type="start";
		
		this.setLayout(null);
        
		this.setBackground(Color.green);
		
        JLabel label = new JLabel("Start");
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setBounds(25, 35, 130, 60);
        this.add(label);
        
        
	}
	
}
