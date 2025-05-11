package gui1;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;

public class EndFuncBlock extends Dragpanel{

	
	EndFuncBlock(String name)
	{
		
		this.type="endfunc";
		
		this.setLayout(null);
        
		this.setBackground(Color.yellow);
		
        JLabel label = new JLabel("End "+name);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setBounds(25, 35, 130, 60);
        this.add(label);
		
        for (java.awt.Component comp : getComponents()) {
            if (comp instanceof JButton && ((JButton) comp).getText().equals("X")) {
                remove(comp); // Remove the "X" button
                break; // Exit after removing the "X" button
            }
        }
        
        
        
	}
	
	
	
	
	
	
}
