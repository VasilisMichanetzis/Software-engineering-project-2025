package gui1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Varpanel extends JPanel {
    private Declaration declpanel;
    private int index;

    private Output outpanel;
    
    private JTextField varField;
    private JTextField valueField;

    Varpanel(int index, Declaration declpanel,Output outpanel) {
        this.index = index;
        this.declpanel = declpanel;
        this.outpanel = outpanel;
        
        this.setBackground(Color.BLUE);
        this.setBounds(index * 160 + 20, 60, 150, 60);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        this.setLayout(null);

        JLabel equalsLabel = new JLabel("=");
        equalsLabel.setBounds(40, 15, 20, 30); // position between text fields
        equalsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        equalsLabel.setForeground(Color.WHITE); // optional: match color theme
        this.add(equalsLabel);
        
        
        // Text field for variable name (e.g., "x")
        varField = new JTextField();
        varField.setBounds(3, 15, 30, 30);
        this.add(varField);

        // Text field for value (e.g., "2")
        valueField = new JTextField();
        valueField.setBounds(55, 15, 30, 30);
        this.add(valueField);

        // Delete button
        JButton closeButton = new JButton("X");
        //closeButton.setPreferredSize(new Dimension(45, 45));
        closeButton.setFont(new Font("Arial", Font.PLAIN, 13));
        closeButton.setBounds(110, 3, 43, 43);
        closeButton.setFocusable(false);
        
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rempanel();
            }
        });
        this.add(closeButton);
      
    }

    public void rempanel() {
        declpanel.remvar(this, index);
        VarList.removeEntry(index);
    }

    public String getEntry() {
    	
        String name = varField.getText().trim();
        String value = valueField.getText().trim();
        int intval=0;
        
        if(name.isEmpty() || value.isEmpty()) {
        	this.setBackground(Color.red);
        	return "error";
        	}
        
     

        // Check if value is an integer
        try {
            intval=Integer.parseInt(value);  // Try to convert value to an integer
        } catch (NumberFormatException e) {
        	this.setBackground(Color.red);
            return "error: value must be an integer";
        }
        
        // Check if name is a single character
        if (name.length() != 1 ) {
        	this.setBackground(Color.red);
            return "error: name must be a single character";
        }
        
        try {
            Integer.parseInt(name);  // Try to convert value to an integer
            this.setBackground(Color.red);
            return "error: name not a char";
        } catch (NumberFormatException e) {
        	
        }
        

        for (int i = 0; i < VarList.entries.size(); i++) {
            VarEntry entry = VarList.entries.get(i);
            if (entry != null && entry.name.equals(name) && entry.index!=index) {
            	this.setBackground(Color.red);
                return "error: duplicate variable name";
            }
        }
        VarEntry varentry = new VarEntry(name, intval,index);
        VarList.setEntry(index, varentry); 
        this.setBackground(Color.BLUE);
        return varentry.toString();
    }
    
    
    
}
