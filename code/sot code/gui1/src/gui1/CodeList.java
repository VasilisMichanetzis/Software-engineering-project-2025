package gui1;

import java.util.ArrayList;

public class CodeList {

	public static int numstart=0;
	public static int numend=0;
	


	private static final ArrayList<Dragpanel> blocks = new ArrayList<>();

	    // Add a Start block to the beginning if none exists
	    public static void addStartBlock(Dragpanel startBlock) {
	        if (blocks.isEmpty() || !(blocks.get(0).type != null && blocks.get(0).type.equals("start"))) {
	            blocks.add(0, startBlock);
	        }
	    }

	    // Insert a new block to the right of a given block
	    public static void insertRightOf(Dragpanel target, Dragpanel newBlock) {
	        int index = blocks.indexOf(target);
	        if (index != -1 && index < blocks.size() - 1) {
	            blocks.add(index + 1, newBlock);
	        } else {
	            blocks.add(newBlock); // fallback: add at the end
	        }
	    }

	    // Remove a block and shift remaining to the left
	    public static void removeBlock(Dragpanel block) {
	        blocks.remove(block);
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
	    }
	    
	    
	    
	    
	}

	
	
	
	
	

