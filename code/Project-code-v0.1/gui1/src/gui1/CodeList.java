package gui1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.BorderFactory;

public class CodeList {

	public static int numstart=0;
	public static int numend=0;
	
	public static int ProgCounter=0;
	public static int ProgMax=0;
	//temp var for storing last dragpanel to go in list
	//public static Dragpanel lastpanelin;
	
	

	private static final ArrayList<Dragpanel> blocks = new ArrayList<>();

	    // Add a Start block to the beginning if none exists
	    public static void addStartBlock(Dragpanel startBlock) {
	        if (blocks.isEmpty() || !(blocks.get(0).type != null && blocks.get(0).type.equals("start"))) {
	            blocks.add(0, startBlock);
	            ProgMax=1;
	        }
	    }

	    // Insert a new block to the right of a given block
	    public static void insertRightOf(Dragpanel target, Dragpanel newBlock) {
	        int index = blocks.indexOf(target);
	        if (index != -1 && index < blocks.size() - 1) {
	            blocks.add(index + 1, newBlock);
	            ProgMax++;
	        } else {
	            blocks.add(newBlock); // fallback: add at the end
	            ProgMax++;
	        }
	    }

	    // Remove a block and shift remaining to the left
	    public static void removeBlock(Dragpanel block) {
	        blocks.remove(block);
	        ProgMax--;
	    }

	    // Debug helper: print list
	    public static void printList() {
	        for (int i = 0; i < blocks.size(); i++) {
	            System.out.println(i + ": " + blocks.get(i).type);
	        }
	    }

	    // Get list if needed elsewhere
	    public static ArrayList<Dragpanel> getBlocks() {
	        return blocks;
	    }

	    // Clear all
	    public static void clear() {
	        blocks.clear();
	        ProgMax=0;
	    }
	    
	    public static void compile() 
	    {
	    	 Stack<Integer> ifStack = new Stack<>();
	    	 Stack<Integer> whileStack = new Stack<>();
	    	 
	    	 
	    	 for (int i = 0; i < blocks.size(); i++) 
	    	 {
	    		 	Dragpanel block = blocks.get(i);
		            System.out.println(i + ": " + block.type);
		            
		            if (block.type != null && block.type.equals("math") && block instanceof MathBlock) {
		                MathBlock math = (MathBlock) block;
		                math.checkmath();
		            }
		            else if (block.type != null && block.type.equals("if") && block instanceof IfBlock) {
		                IfBlock ifblock = (IfBlock) block;
		                ifblock.setBackground(Color.magenta);
		                ifblock.checkif();
		                ifStack.push(i); // Store the index of the "if" block
		                 
		            }
		            else if (block.type != null && block.type.equals("endif") && block instanceof EndIfBlock) {
		                EndIfBlock endifblock = (EndIfBlock) block;
		                endifblock.setBackground(Color.magenta);
		                if(ifStack.isEmpty()) {endifblock.setBackground(Color.RED);}
		                
		                int matchingIfIndex = ifStack.pop();
		                IfBlock ifblock = (IfBlock) blocks.get(matchingIfIndex);
		                ifblock.endifpos = i;
		                endifblock.ifpos = matchingIfIndex;

		            }
		            else if (block.type != null && block.type.equals("while") && block instanceof WhileBlock) {
		            	WhileBlock whileblock = (WhileBlock) block;
		                whileblock.setBackground(Color.orange);
		                whileblock.checkwhile();
		                whileStack.push(i); // Store the index of the "while" block
		                
		            }
		            else if (block.type != null && block.type.equals("endwhile") && block instanceof EndWhileBlock) {
		                EndWhileBlock endwhileblock = (EndWhileBlock) block;
		                endwhileblock.setBackground(Color.orange);
		                if(whileStack.isEmpty()) {endwhileblock.setBackground(Color.RED);}
		                
		                int matchingWhileIndex = whileStack.pop();
		                WhileBlock whileblock = (WhileBlock) blocks.get(matchingWhileIndex);
		                whileblock.endwhilepos = i;
		                endwhileblock.whilepos = matchingWhileIndex;

		            }
		     }
	    	 //make ifs not matched with their endifs red
	    	 while (!ifStack.isEmpty()) {
	    		    int unmatchedIf = ifStack.pop();
	    		    IfBlock ifblock = (IfBlock) blocks.get(unmatchedIf);
	    		    System.err.println("Unmatched if at index " + unmatchedIf);
	    		    ifblock.setBackground(Color.RED); 
	    		}
	    	 //make whiles not matched with their endwhiles red
	    	 while (!whileStack.isEmpty()) {
	    		    int unmatchedWhile = whileStack.pop();
	    		    WhileBlock whileblock = (WhileBlock) blocks.get(unmatchedWhile);
	    		    System.err.println("Unmatched if at index " + unmatchedWhile);
	    		    whileblock.setBackground(Color.RED); 
	    		}

	    }
	    
	    public static void run(int progcounter) 
	    {

	    	if (progcounter >= blocks.size()) return;
	    	
	    		 	Dragpanel block = blocks.get(progcounter);
		            System.out.println(progcounter + ": " + block.type);
		            block.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));

		            // Execute block logic
		            if ("math".equals(block.type) && block instanceof MathBlock) {
		                MathBlock math = (MathBlock) block;
		                System.out.println(math.execute());
		                
		                CodeList.ProgCounter++;
		            }
		            else if("if".equals(block.type) && block instanceof IfBlock) {
		            	
		            	IfBlock ifblock = (IfBlock) block;
		            	int ifres;
		                ifres=ifblock.execute();
		                if(ifres==1) {CodeList.ProgCounter++;}
		                else {CodeList.ProgCounter = ifblock.endifpos+1;}
		            }
		            else if("while".equals(block.type) && block instanceof WhileBlock) {
		            	
		            	WhileBlock whileblock = (WhileBlock) block;
		            	int whileres;
		                whileres=whileblock.execute();
		                if(whileres==1) {CodeList.ProgCounter++;}
		                else {CodeList.ProgCounter = whileblock.endwhilepos+1;}
		            }
		            else if("endwhile".equals(block.type) && block instanceof EndWhileBlock) {
		            	
		            	EndWhileBlock endwhileblock = (EndWhileBlock) block;
		            	
		                CodeList.ProgCounter = endwhileblock.whilepos;
		            }
		            else {
		            	CodeList.ProgCounter++;
		            }
		           

	    }
	    
	    public static void resetborder(int progcounter) 
	    {

	    	if (progcounter >= blocks.size()) return;
	    	
	    		 	Dragpanel block = blocks.get(progcounter);
		            block.setBorder(BorderFactory.createLineBorder(Color.black, 3));

	    }
	    
	    
	    
	}

	
	
	
	
	

