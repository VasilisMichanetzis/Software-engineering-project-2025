package gui1;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MathBlock extends Dragpanel{
	
  private JTextField updatedvarField;
  private JTextField pos1Field;
  private JTextField symbolField;
  private JTextField pos2Field;

  String updatedvar;
  String pos1;
  String symbol;
  String pos2;
  
  int pos1int;
  int pos2int;
  
  private VarEntry first;
  private VarEntry second;
  private VarEntry forth;
  
  
	MathBlock(){
		
		this.type="math";
		
		this.setLayout(null);
        
		this.setBackground(Color.gray);
		
        JLabel label1 = new JLabel("=");
        label1.setFont(new Font("Arial", Font.BOLD, 30));
        label1.setBounds(35, 35, 130, 60);
        this.add(label1);

        updatedvarField = new JTextField();
        updatedvarField.setBounds(3, 52, 25, 25);
        this.add(updatedvarField);
        
        pos1Field = new JTextField();
        pos1Field.setBounds(53, 52, 25, 25);
        this.add(pos1Field);
        
        symbolField = new JTextField();
        symbolField.setBounds(78, 52, 25, 25);
        this.add(symbolField);
        
        pos2Field = new JTextField();
        pos2Field.setBounds(103, 52, 25, 25);
        this.add(pos2Field);
        
        //temporary for testing
        //
        JButton checkButton = new JButton();
        //closeButton.setPreferredSize(new Dimension(45, 45));
        checkButton.setBackground(Color.GREEN);
        checkButton.setBounds(0, 0, 20, 20);
        checkButton.setFocusable(false);
        
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getmath();
                execute();
            }
        });
        
        this.add(checkButton);

        
        
	}
	
	public String getmath() {
    	
        updatedvar = updatedvarField.getText().trim();
        pos1 = pos1Field.getText().trim();
        symbol = symbolField.getText().trim();
        pos2 = pos2Field.getText().trim();
        
        
        if(updatedvar.isEmpty() || pos1.isEmpty() || symbol.isEmpty() || pos2.isEmpty()) 
        {
        	this.setBackground(Color.red);
        	return "error";
        }
        
        if (!(symbol.contains("+")||symbol.contains("-")||symbol.contains("/")||symbol.contains("*"))) 
        {
        	
        	return "error: operation symbol input";
        	
        }
           
        //for 1st field
       int isinlist =0;
        for (int i = 0; i < VarList.entries.size(); i++) 
        {
            VarEntry entry = VarList.entries.get(i);
            if (entry != null && entry.name.equals(updatedvar)) 
            {
            	isinlist = 1;
            	first = entry;
            }
         }     
         if(isinlist == 0) 
         {
            this.setBackground(Color.red);
            return "error: 1st entry not found in declarations";
         }
      
        //2nd field
        try {
            pos1int=Integer.parseInt(pos1);  // Try to convert value to an integer
            System.out.println(pos1int);
        } catch (NumberFormatException e) {
        	
        	isinlist =0;
            for (int i = 0; i < VarList.entries.size(); i++) 
            {
                VarEntry entry = VarList.entries.get(i);
                if (entry != null && entry.name.equals(pos1)) 
                {
                	isinlist = 1;
                	second = entry;
                }
             }     
             if(isinlist == 0) 
             {
                this.setBackground(Color.red);
                return "error: 2nd entry not found in declarations or not an integer";
             }

        }
        
        //4th field
        try {
            pos2int=Integer.parseInt(pos2);  // Try to convert value to an integer
			System.out.println(pos2int);
        } catch (NumberFormatException e) {
        	
        	isinlist =0;
            for (int i = 0; i < VarList.entries.size(); i++) 
            {
                VarEntry entry = VarList.entries.get(i);
                if (entry != null && entry.name.equals(pos2)) 
                {
                	isinlist = 1;
                	forth = entry;
                }
             }     
             if(isinlist == 0) 
             {
                this.setBackground(Color.red);
                return "error: 4th entry not found in declarations or not an integer";
             }

        }
        
        
        this.setBackground(Color.gray);
		
        return this.type+updatedvar+"="+pos1+symbol+pos2;
    }
	
	
	public String execute() 
	{
	
		System.out.println("field 1:"+second);
		System.out.println("field 2:"+forth);
		
		if (second==null && forth==null) 
		{
			if (symbol.contentEquals("+")) 
			{	
				first.value=pos1int+pos2int;
			}
			else if (symbol.contentEquals("-")) 
			{
				first.value=pos1int-pos2int;
			}
			else if (symbol.contentEquals("/")) 
			{
				first.value=pos1int/pos2int;
			}
			else if (symbol.contentEquals("*")) 
			{
				first.value=pos1int*pos2int;
			}
			
			
		}
		else if (forth==null) 
		{
			if (symbol.contentEquals("+")) 
			{	
				first.value=second.value+pos2int;
			}
			else if (symbol.contentEquals("-")) 
			{
				first.value=second.value-pos2int;
			}
			else if (symbol.contentEquals("/")) 
			{
				first.value=second.value/pos2int;
			}
			else if (symbol.contentEquals("*")) 
			{
				first.value=second.value*pos2int;
			}
			
			
		}
		else if (second==null)
		{
			
			if (symbol.contentEquals("+")) 
			{	
				first.value=pos1int+forth.value;
			}
			else if (symbol.contentEquals("-")) 
			{
				first.value=pos1int-forth.value;
			}
			else if (symbol.contentEquals("/")) 
			{
				first.value=pos1int/forth.value;
			}
			else if (symbol.contentEquals("*")) 
			{
				first.value=pos1int*forth.value;
			}

		}


		VarList.printAll();
		
		return first.name+"="+first.value;
	}
	
	
}
