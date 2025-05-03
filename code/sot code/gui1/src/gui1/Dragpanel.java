package gui1;

import javax.swing.*;

//import gui1.Dragpanel;
//import gui1.GhostPanelPreview;
//import gui1.SnapManager;
//import gui1.SnapResult;

import java.awt.*;
import java.awt.event.*;



public class Dragpanel extends JPanel {
    

	private int mouseX, mouseY;
    
    public String type;
    
    private final int SNAP_THRESHOLD = 20;
    private final int UNSNAP_DISTANCE = 120;
    // Snap state: 0 = not snapped, 1 = snapped (right edge), -1 = snapped (left edge)
    private int snapState = 0;
    // Record the x-coordinate when snapping occurred.
    private int snapReferenceX = 0;
    // Locked y coordinate when snapped.
    private int lockedY = -1;
    private GhostPanelPreview ghostPreview = new GhostPanelPreview();
    
    
    Dragpanel() 
    {

        this.setBackground(Color.BLUE);
        this.setPreferredSize(new Dimension(200, 100));
        this.setBounds(100, 150, 210, 90); // Initial position inside the container panel
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        this.setLayout(null);

        JButton closeButton = new JButton("X");
        closeButton.setPreferredSize(new Dimension(45, 45));
        closeButton.setFont(new Font("Arial", Font.BOLD, 7));
        closeButton.setBounds(85, 3, 40, 40); // Position inside the panel
        closeButton.setFocusable(false);
        

        closeButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                Container parent = getParent();
                if (parent != null) {
                    parent.remove(Dragpanel.this); // Remove itself from canvas
                    parent.revalidate();  
                    parent.repaint();
                    
                    if (type.contentEquals("start")) 
                    	{
                    	CodeList.numstart=0; 
                    	CodeList.clear();
                    	CodeList.lastpanelin=null;
                    	}
                    else if (type.contentEquals("end")) {CodeList.numend=0; CodeList.removeBlock(Dragpanel.this); }
                    else {CodeList.removeBlock(Dragpanel.this); }
                    
                    
                    
                }
            }
        });

        this.add(closeButton); // Add delete button to panel
        
        //temp button for adding dragpanels to codelist
        
        
        JButton addtolist = new JButton();
        addtolist.setBounds(50, 0, 20, 20); // Position inside the panel
        addtolist.setFocusable(false);
        
        addtolist.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
               if(CodeList.lastpanelin!=null) 
               {
            	   CodeList.insertRightOf(CodeList.lastpanelin,Dragpanel.this);
            	   CodeList.lastpanelin=Dragpanel.this;
               }
            	
            	
            }
        });
        
        
        this.add(addtolist);
        
        //------------------------------------------------

        // Mouse listener for dragging
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                SnapResult result = SnapManager.calculateSnap(Dragpanel.this, getParent(), getX(), getY(), SNAP_THRESHOLD);
                if (result.isSnapped()) {
                    snapState = result.getSnapState();
                    snapReferenceX = result.getSnapX();
                    lockedY = result.getSnapY(); // Lock y to neighbor's y.
                    setLocation(snapReferenceX, lockedY);
                    
                }
                ghostPreview.remove(getParent());
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (getParent() == null) return; // Safety check

                // Calculate new position
                int newX = getX() + e.getX() - mouseX;
                int newY = getY() + e.getY() - mouseY;
                if(snapState != 0 && lockedY != -1) {
                    newY = lockedY;
                }
                
                if(snapState == 1) {
                    if(newX < snapReferenceX - UNSNAP_DISTANCE) {
                        snapState = 0;
                        lockedY = -1;
                    } else {
                        newX = snapReferenceX;
                    }
                } else if(snapState == -1) {
                    if(newX > snapReferenceX + UNSNAP_DISTANCE) {
                        snapState = 0;
                        lockedY = -1;
                    } else {
                        newX = snapReferenceX;
                    }
                }
                
                if(snapState == 0) {
                    SnapResult ghostResult = SnapManager.calculateSnap(Dragpanel.this, getParent(), newX, newY, SNAP_THRESHOLD);
                    if(ghostResult.isSnapped()) {
                        int ghostX = ghostResult.getSnapX();
                        int ghostY = ghostResult.getSnapY();
                        int dragZ = getParent().getComponentZOrder(Dragpanel.this);
                        ghostPreview.update(getParent(), ghostX, ghostY, getWidth(), getHeight(), dragZ + 1);
                    } else {
                        ghostPreview.remove(getParent());
                    }
                } else {
                    ghostPreview.remove(getParent());
                }

                // Keep inside the parent panel (ContainerPanel)
                int maxX = getParent().getWidth() - getWidth();
                int maxY = getParent().getHeight() - getHeight();

                newX = Math.max(0, Math.min(newX, maxX));
                newY = Math.max(0, Math.min(newY, maxY));

                setLocation(newX, newY);
            }
        });
        
        
    }
}
