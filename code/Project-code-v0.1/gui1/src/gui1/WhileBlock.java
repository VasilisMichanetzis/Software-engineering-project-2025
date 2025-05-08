package gui1;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class WhileBlock extends Dragpanel{
	
	
	
	public int endwhilepos;
	
	
	
	private JTextField pos1Field;
	private JTextField symbolField;
	private JTextField pos2Field;
	
	String pos1;
	String symbol;
	String pos2;
	
	private VarEntry first;
	private VarEntry second;
	
	int pos1int;
	int pos2int;
	
	WhileBlock()
	{
		this.type="while";
		
		this.setLayout(null);
        
		this.setBackground(Color.orange);
		
		 JLabel label1 = new JLabel("While");
	     label1.setFont(new Font("Arial", Font.BOLD, 20));
	     label1.setBounds(20, 5, 130, 60);
	     this.add(label1);
		
		
		pos1Field = new JTextField();
        pos1Field.setBounds(15, 52, 25, 25);
        this.add(pos1Field);
        
        symbolField = new JTextField();
        symbolField.setBounds(45, 52, 35, 25);
        this.add(symbolField);
        
        pos2Field = new JTextField();
        pos2Field.setBounds(85, 52, 25, 25);
        this.add(pos2Field);
		
	}
	
public String checkwhile() {
    	
        pos1 = pos1Field.getText().trim();
        symbol = symbolField.getText().trim();
        pos2 = pos2Field.getText().trim();

        
        if  (!(
        				symbol.contains("==")||symbol.contains("!=")||
        				symbol.contains(">=")||symbol.contains("<=")||
        				symbol.contains("<")||symbol.contains(">")
        	 )) 
        {
        	
        	return "error: comparison symbol input";
        	
        }
           
      
        //1st field
        int isinlist=0;
        try {
            pos1int=Integer.parseInt(pos1);  // Try to convert value to an integer
            //System.out.println(pos1int);
        } catch (NumberFormatException e) {
        	
        	isinlist =0;
            for (int i = 0; i < VarList.entries.size(); i++) 
            {
                VarEntry entry = VarList.entries.get(i);
                if (entry != null && entry.name.equals(pos1)) 
                {
                	isinlist = 1;
                	first = entry;
                }
             }     
             if(isinlist == 0) 
             {
                this.setBackground(Color.red);
                return "error: 2nd entry not found in declarations or not an integer";
             }

        }
        
        //3rd field
        try {
            pos2int=Integer.parseInt(pos2);  // Try to convert value to an integer
			//System.out.println(pos2int);
        } catch (NumberFormatException e) {
        	
        	isinlist =0;
            for (int i = 0; i < VarList.entries.size(); i++) 
            {
                VarEntry entry = VarList.entries.get(i);
                if (entry != null && entry.name.equals(pos2)) 
                {
                	isinlist = 1;
                	second = entry;
                }
             }     
             if(isinlist == 0) 
             {
                this.setBackground(Color.red);
                return "error: 4th entry not found in declarations or not an integer";
             }

        }
        
        
        this.setBackground(Color.orange);
		
        return this.type+pos1+symbol+pos2;
    }
	
public int execute() 
{

	int boolresult = 0;
	
	if (first==null && second==null) 
	{
		if (symbol.contentEquals("==")) 
		{	
			if(pos1int==pos2int) {boolresult=1;}
		}
		else if (symbol.contentEquals("!=")) 
		{
			if(pos1int!=pos2int) {boolresult=1;}
		}
		else if (symbol.contentEquals(">=")) 
		{
			if(pos1int>=pos2int) {boolresult=1;}
		}
		else if (symbol.contentEquals("<=")) 
		{
			if(pos1int<=pos2int) {boolresult=1;}
		}
		else if (symbol.contentEquals("<")) 
		{
			if(pos1int<pos2int) {boolresult=1;}
		}
		else if (symbol.contentEquals(">")) 
		{
			if(pos1int>pos2int) {boolresult=1;}
		}
		
	}
	else if (first==null) 
	{
		if (symbol.contentEquals("==")) 
		{	
			if(pos1int==second.value) {boolresult=1;}
		}
		else if (symbol.contentEquals("!=")) 
		{
			if(pos1int!=second.value) {boolresult=1;}
		}
		else if (symbol.contentEquals(">=")) 
		{
			if(pos1int>=second.value) {boolresult=1;}
		}
		else if (symbol.contentEquals("<=")) 
		{
			if(pos1int<=second.value) {boolresult=1;}
		}
		else if (symbol.contentEquals("<")) 
		{
			if(pos1int<second.value) {boolresult=1;}
		}
		else if (symbol.contentEquals(">")) 
		{
			if(pos1int>second.value) {boolresult=1;}
		}
		
		
	}
	else if (second==null)
	{
		
		if (symbol.contentEquals("==")) 
		{	
			if(first.value==pos2int) {boolresult=1;}
		}
		else if (symbol.contentEquals("!=")) 
		{
			if(first.value!=pos2int) {boolresult=1;}
		}
		else if (symbol.contentEquals(">=")) 
		{
			if(first.value>=pos2int) {boolresult=1;}
		}
		else if (symbol.contentEquals("<=")) 
		{
			if(first.value<=pos2int) {boolresult=1;}
		}
		else if (symbol.contentEquals("<")) 
		{
			if(first.value<pos2int) {boolresult=1;}
		}
		else if (symbol.contentEquals(">")) 
		{
			if(first.value>pos2int) {boolresult=1;}
		}

	}
	else
	{
		if (symbol.contentEquals("==")) 
		{	
			if(first.value==second.value) {boolresult=1;}
		}
		else if (symbol.contentEquals("!=")) 
		{
			if(first.value!=second.value) {boolresult=1;}
		}
		else if (symbol.contentEquals(">=")) 
		{
			if(first.value>=second.value) {boolresult=1;}
		}
		else if (symbol.contentEquals("<=")) 
		{
			if(first.value<=second.value) {boolresult=1;}
		}
		else if (symbol.contentEquals("<")) 
		{
			if(first.value<second.value) {boolresult=1;}
		}
		else if (symbol.contentEquals(">")) 
		{
			if(first.value>second.value) {boolresult=1;}
		}

		
	}

	return boolresult;
}	
	
	
	
	

}
