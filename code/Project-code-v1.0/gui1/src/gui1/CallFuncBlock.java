package gui1;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class CallFuncBlock extends Dragpanel{

	public StartFuncBlock startf;
	public EndFuncBlock endf;
	
	
	
	CallFuncBlock(String name,StartFuncBlock startf, EndFuncBlock endf)
	{
		
		this.type="callfunc";
		this.startf= startf;
		this.endf = endf;
		
		
		this.setLayout(null);
        
		this.setBackground(Color.yellow);
		
        JLabel label = new JLabel(name);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setBounds(25, 35, 130, 60);
        this.add(label);
		
	}
	
	public int checkfunc() 
	{
		FuncList.CompError = 0;
		FuncList.compile();
		
		if(FuncList.CompError==1) {this.setBackground(Color.red);}
		else {this.setBackground(Color.yellow);}
		
		return FuncList.CompError;
	}
	
	public void execute() 
	{
		FuncList.ProgCounter = 0;
        int total = FuncList.ProgMax;
        
        while (FuncList.ProgCounter < total) 
        {
        	
        	int prcount =FuncList.ProgCounter;
        	
        	FuncList.run(prcount);
		
        }
		
		FuncList.resetAllBorders();
	}

	public StartFuncBlock getStartf() {
        return startf;
    }

    public EndFuncBlock getEndf() {
        return endf;
    }
}
