package gui1;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class Canvas extends JPanel{
	private int dragPanelCount = 0;

	
	
	
	Canvas()
	{
		
		this.setBackground(Color.cyan);
		this.setPreferredSize(new Dimension(150,150));
		Border border = BorderFactory.createLineBorder(Color.BLACK, 3);
	    this.setBorder(border);
	    this.setLayout(null);
	    
		
		
	}

	 public void addStartBlock() {

		 if (dragPanelCount == 5) {dragPanelCount=0;}
		 	if(CodeList.numstart == 0) 
		 	{
		 	CodeList.numstart=1;
	        StartBlock block = new StartBlock();
	        block.setBounds(20 + (dragPanelCount * 20), 20 + (dragPanelCount * 20), 130, 90); 
	        this.add(block);
	        this.revalidate();
	        this.repaint();
	        CodeList.addStartBlock(block);
	        //CodeList.lastpanelin=block;
	        dragPanelCount++;
		 	}
		 }

	 
	 public void addEndBlock() {

		 if (dragPanelCount == 5) {dragPanelCount=0;}
		 	if(CodeList.numend == 0) 
		 	{
		 	CodeList.numend=1;
	        EndBlock block = new EndBlock();
	        block.setBounds(20 + (dragPanelCount * 20), 20 + (dragPanelCount * 20), 130, 90); // Staggered positions

	        this.add(block);
	        this.revalidate();
	        this.repaint();

	        dragPanelCount++; // Track added panels
		 	}
	    }
	 public void addMathBlock() {

		 if (dragPanelCount == 5) {dragPanelCount=0;}
		 
	        MathBlock block = new MathBlock();
	        block.setBounds(20 + (dragPanelCount * 20), 20 + (dragPanelCount * 20), 130, 90); // Staggered positions

	        this.add(block);
	        this.revalidate();
	        this.repaint();

	        dragPanelCount++; // Track added panels
	    }
	 public void addIfBlock() {

		 if (dragPanelCount == 5) {dragPanelCount=0;}
		 
	        IfBlock block = new IfBlock();
	        block.setBounds(20 + (dragPanelCount * 20), 20 + (dragPanelCount * 20), 130, 90); // Staggered positions

	        this.add(block);
	        this.revalidate();
	        this.repaint();

	        dragPanelCount++; // Track added panels
	    }
	 public void addEndIfBlock() {

		 if (dragPanelCount == 5) {dragPanelCount=0;}
		 
	        EndIfBlock block = new EndIfBlock();
	        block.setBounds(20 + (dragPanelCount * 20), 20 + (dragPanelCount * 20), 130, 90); // Staggered positions

	        this.add(block);
	        this.revalidate();
	        this.repaint();

	        dragPanelCount++; // Track added panels
	    } 
	 public void addWhileBlock() {

		 if (dragPanelCount == 5) {dragPanelCount=0;}
		 
	        WhileBlock block = new WhileBlock();
	        block.setBounds(20 + (dragPanelCount * 20), 20 + (dragPanelCount * 20), 130, 90); // Staggered positions

	        this.add(block);
	        this.revalidate();
	        this.repaint();

	        dragPanelCount++; // Track added panels
	    }
	 public void addEndWhileBlock() {

		 if (dragPanelCount == 5) {dragPanelCount=0;}
		 
	        EndWhileBlock block = new EndWhileBlock();
	        block.setBounds(20 + (dragPanelCount * 20), 20 + (dragPanelCount * 20), 130, 90); // Staggered positions

	        this.add(block);
	        this.revalidate();
	        this.repaint();

	        dragPanelCount++; // Track added panels
	    } 
	 public void addCallFuncBlock(String name, StartFuncBlock startf, EndFuncBlock endf) {

		 if (dragPanelCount == 5) {dragPanelCount=0;}
		 
	        CallFuncBlock block = new CallFuncBlock(name,startf,endf);
	        block.setBounds(20 + (dragPanelCount * 20), 20 + (dragPanelCount * 20), 130, 90); // Staggered positions

	        this.add(block);
	        this.revalidate();
	        this.repaint();

	        dragPanelCount++; // Track added panels
	    } 
	 public StartFuncBlock addStartFuncBlock(String name) {

		 if (dragPanelCount == 5) {dragPanelCount=0;}
		 
	        StartFuncBlock block = new StartFuncBlock(name);
	        block.setBounds(20 + (dragPanelCount * 20), 20 + (dragPanelCount * 20), 130, 90); // Staggered positions
	        
	        FuncList.addStartBlock(block);
	        this.add(block);
	        this.revalidate();
	        this.repaint();

	        dragPanelCount++; // Track added panels
	        return block;
	    } 
	 public EndFuncBlock addEndFuncBlock(String name) {

		 if (dragPanelCount == 5) {dragPanelCount=0;}
		 
	        EndFuncBlock block = new EndFuncBlock(name);
	        block.setBounds(20 + (dragPanelCount * 20), 20 + (dragPanelCount * 20), 130, 90); // Staggered positions

	        this.add(block);
	        this.revalidate();
	        this.repaint();

	        dragPanelCount++; // Track added panels
	        return block;
	    } 
	 public void createFuncTriplet(String name) 
	 {		
		 FuncList.funccreated=1;
		 addCallFuncBlock(name, addStartFuncBlock(name), addEndFuncBlock(name));
	 }
}
