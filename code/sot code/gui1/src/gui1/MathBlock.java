package gui1;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class MathBlock extends Dragpanel{

	
	MathBlock(){
		
		this.type="math";
		
		this.setLayout(null);
        
		this.setBackground(Color.gray);
		
        JLabel label1 = new JLabel("=");
        label1.setFont(new Font("Arial", Font.BOLD, 30));
        label1.setBounds(35, 35, 130, 60);
        this.add(label1);

        
        
        
        
	}
	
}
