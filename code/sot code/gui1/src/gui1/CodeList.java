package gui1;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.BorderFactory;

public class CodeList {

	public static int numstart=0;
	public static int numend=0;
	
	public static int ProgCounter=0;
	public static int ProgMax=0;
	//temp var for storing last dragpanel to go in list
	public static Dragpanel lastpanelin;
	
	

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
	    	 for (int i = 0; i < blocks.size(); i++) 
	    	 {
	    		 	Dragpanel block = blocks.get(i);
		            System.out.println(i + ": " + block.type);
		            if (block.type != null && block.type.equals("math") && block instanceof MathBlock) {
		                MathBlock math = (MathBlock) block;
		                math.checkmath();
		            }
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
		            }

		           

	    }
	    
	    
	    
	    
	}

	
	
	
	
	

