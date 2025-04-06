package gui1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class Output extends JPanel{
	
	Output()
	{
		
		this.setBackground(Color.pink);
		this.setPreferredSize(new Dimension(120,120));
		Border border = BorderFactory.createLineBorder(Color.BLACK, 3);
	    this.setBorder(border);
		this.setLayout(null);
	}

	public void create_out_elements() 
	{
		
		this.removeAll();        // clears all components
		this.revalidate();       // tells layout manager to re-calculate
		this.repaint();  
		
		for (int i = 0; i < VarList.entries.size(); i++) {
            VarEntry entry = VarList.entries.get(i);
            if (entry != null) {
                JPanel panel = new JPanel();
                panel.setBackground(Color.yellow);
                panel.setBounds(entry.index * 160 + 20, 30, 150, 60);
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                panel.setLayout(null);
                
                JLabel label = new JLabel(entry.name + " = " + entry.value);
                label.setFont(new Font("Arial", Font.BOLD, 30));
                label.setBounds(35, 3, 150, 60);
                panel.add(label);
                
                this.add(panel);
                panel.setVisible(true);
                
            }
        }
		
		this.revalidate();
		this.repaint();

	}

}
