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

	public static int IsCompiled=0;
	public static int CompError=0;

	private static final ArrayList<Dragpanel> blocks = new ArrayList<>();

	    // Add a Start block to the beginning if none exists
	    public static void addStartBlock(Dragpanel startBlock) {
	        if (blocks.isEmpty() || !(blocks.get(0).type != null && blocks.get(0).type.equals("start"))) {
	            blocks.add(0, startBlock);
	            IsCompiled=0;
	            ProgMax=1;
	        }
	    }

	    // Insert a new block to the right of a given block
	    public static void insertRightOf(Dragpanel target, Dragpanel newBlock) {
	        int index = blocks.indexOf(target);
	        if (index != -1 && index < blocks.size() - 1) {
	            blocks.add(index + 1, newBlock);
	            ProgMax++;
	            IsCompiled=0;
	        } else {
	            blocks.add(newBlock); // fallback: add at the end
	            IsCompiled=0;
	            ProgMax++;
	        }
	    }

	    // Remove a block and shift remaining to the left
	    public static void removeBlock(Dragpanel block) {
	        blocks.remove(block);
	        IsCompiled=0;
	        ProgMax--;
	    }

	    public static boolean contains(Dragpanel block) {
	        return blocks.contains(block);
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
	        IsCompiled=0;
	        ProgMax=0;
	    }
	    
	    public static void compile() 
	    {
	    	 IsCompiled=1;
	    	 CompError=0;
	    	 int localcomperror=0;
	    	 Stack<Integer> ifStack = new Stack<>();
	    	 Stack<Integer> whileStack = new Stack<>();
	    	 
	    	 Dragpanel end = blocks.get(blocks.size()-1);
	    	 Dragpanel start = blocks.get(0);
	    	 
	    	 start.setBackground(Color.green);
	         if (!end.type.equals("end")) {
	        	 start.setBackground(Color.red);
	        	 CodeList.CompError=1;
	         }
	    	 
	    	 
	    	 for (int i = 0; i < blocks.size(); i++) 
	    	 {
	    		 	Dragpanel block = blocks.get(i);
		            System.out.println(i + ": " + block.type);
		            
		            if (block.type != null && block.type.equals("math") && block instanceof MathBlock) {
		                MathBlock math = (MathBlock) block;
		                localcomperror=math.checkmath();
		                if (localcomperror==1) {CompError=localcomperror;}
		            }
		            else if (block.type != null && block.type.equals("if") && block instanceof IfBlock) {
		                IfBlock ifblock = (IfBlock) block;
		                ifblock.setBackground(Color.magenta);
		                localcomperror=ifblock.checkif();
		                if (localcomperror==1) {CompError=localcomperror;}
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
		                localcomperror=whileblock.checkwhile();
		                if (localcomperror==1) {CompError=localcomperror;}
		                whileStack.push(i); // Store the index of the "while" block
		                
		            }
		            else if (block.type != null && block.type.equals("callfunc") && block instanceof CallFuncBlock) {
		            	CallFuncBlock funcblock = (CallFuncBlock) block;
		                funcblock.setBackground(Color.yellow);
		                localcomperror=funcblock.checkfunc();
		                if (localcomperror==1) {CompError=localcomperror;}
  
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
	    		    CodeList.CompError=1;
	    		    ifblock.setBackground(Color.RED); 
	    		}
	    	 //make whiles not matched with their endwhiles red
	    	 while (!whileStack.isEmpty()) {
	    		    int unmatchedWhile = whileStack.pop();
	    		    WhileBlock whileblock = (WhileBlock) blocks.get(unmatchedWhile);
	    		    System.err.println("Unmatched if at index " + unmatchedWhile);
	    		    CodeList.CompError=1;
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
		            else if("callfunc".equals(block.type) && block instanceof CallFuncBlock) {
		            	
		            	CallFuncBlock funcblock = (CallFuncBlock) block;
		            	
		                funcblock.execute();
		               
		                CodeList.ProgCounter++;
		                
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

	
	
	
	
	

