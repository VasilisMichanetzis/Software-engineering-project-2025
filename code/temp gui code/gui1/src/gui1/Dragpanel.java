package gui1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dragpanel extends JPanel {
    
    private int mouseX, mouseY;
    // We'll use an id for identification.
    private String id;
    
    public String type;
    private final int SNAP_THRESHOLD = 20;
    private final int UNSNAP_DISTANCE = 120;
    // Snap state: 0 = not snapped, 1 = snapped (right edge), -1 = snapped (left edge)
    private int snapState = 0;
    // Record the x-coordinate when snapping occurred.
    private int snapReferenceX = 0;
    // Locked y coordinate when snapped.
    private int lockedY = -1;
    
    // Instance of SnapInfo to record snap details.
    private SnapInfo snapInfo = new SnapInfo();
    private GhostPanelPreview ghostPreview = new GhostPanelPreview();
    
    // Label to display snap info on the GUI.
    private JLabel snapInfoLabel;
    
    Dragpanel() {
        // Set an id (using hashCode for uniqueness)
        this.id = "ID-" + Integer.toHexString(hashCode());
        
        this.setBackground(Color.BLUE);
        this.setPreferredSize(new Dimension(200, 100));
        this.setBounds(100, 150, 210, 90); // Initial position inside the container panel
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        this.setLayout(null);
        
       
        
        // Create a label to display snap info.
        snapInfoLabel = new JLabel("");
        snapInfoLabel.setForeground(Color.RED);
        snapInfoLabel.setFont(new Font("Arial", Font.PLAIN, 8));
        snapInfoLabel.setBounds(2, this.getHeight() - 28, this.getWidth() - 4, 26);
        snapInfoLabel.setText("<html></html>");
        this.add(snapInfoLabel);
        
        JButton closeButton = new JButton("X");
        closeButton.setPreferredSize(new Dimension(45, 45));
        closeButton.setFont(new Font("Arial", Font.BOLD, 7));
        closeButton.setBounds(85, 3, 40, 40); // Position inside the panel
        closeButton.setFocusable(false);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Container parent = getParent();
                if (parent != null) {
                    // Record the x position and width of the block being deleted.
                    int deletedX = getX();
                    int blockWidth = getWidth();
                    // Remove this block.
                    parent.remove(Dragpanel.this);
                    
                    // Shift all Dragpanel components to the right of the deleted block leftward by blockWidth.
                    for (Component comp : parent.getComponents()) {
                        if (comp instanceof Dragpanel) {
                            int compX = comp.getX();
                            if (compX > deletedX) {
                                comp.setLocation(compX - blockWidth, comp.getY());
                            }
                        }
                    }
                    parent.revalidate();
                    parent.repaint();
                    // Update snap info on all remaining blocks.
                    updateAllSnapInfo();
                }
            }
        });
        this.add(closeButton); // Add delete button to panel
        
        // Mouse listener for press and release events.
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
                updateSnapInfo();
                updateAllSnapInfo();
            }
        });
        
        // Mouse motion listener for dragging.
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (getParent() == null) return; // Safety check

                // Calculate new position based on mouse movement.
                int newX = getX() + e.getX() - mouseX;
                int newY = getY() + e.getY() - mouseY;
                if (snapState != 0 && lockedY != -1) {
                    newY = lockedY;
                }
                
                if (snapState == 1) {
                    if (newX < snapReferenceX - UNSNAP_DISTANCE) {
                        snapState = 0;
                        lockedY = -1;
                    } else {
                        newX = snapReferenceX;
                    }
                } else if (snapState == -1) {
                    if (newX > snapReferenceX + UNSNAP_DISTANCE) {
                        snapState = 0;
                        lockedY = -1;
                    } else {
                        newX = snapReferenceX;
                    }
                }
                
                if (snapState == 0) {
                    SnapResult ghostResult = SnapManager.calculateSnap(Dragpanel.this, getParent(), newX, newY, SNAP_THRESHOLD);
                    if (ghostResult.isSnapped()) {
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

                // Keep inside the parent panel.
                int maxX = getParent().getWidth() - getWidth();
                int maxY = getParent().getHeight() - getHeight();
                newX = Math.max(0, Math.min(newX, maxX));
                newY = Math.max(0, Math.min(newY, maxY));

                setLocation(newX, newY);
                // Update snap info continuously as the block moves.
                updateSnapInfo();
            }
        });
    }
    
    // Method to return the id.
    public String getId() {
        return id;
    }
    
    // Updates the snapInfo based on current position and neighbors,
    // then updates the snapInfoLabel to display the info on the GUI.
    public void updateSnapInfo() {
        snapInfo.reset();
        Container parent = getParent();
        if (parent == null) return;
        int currentX = getX();
        int myRight = currentX + getWidth();
        
        // Check for left neighbor (if our left edge is near neighbor's right edge).
        for (Component comp : parent.getComponents()) {
            if (comp == Dragpanel.this || !(comp instanceof Dragpanel))
                continue;
            Rectangle otherBounds = comp.getBounds();
            int otherRight = otherBounds.x + otherBounds.width;
            if (Math.abs(currentX - otherRight) < SNAP_THRESHOLD) {
                snapInfo.setSnappedLeft((Dragpanel) comp);
                break;
            }
        }
        
        // Check for right neighbor (if our right edge is near neighbor's left edge).
        for (Component comp : parent.getComponents()) {
            if (comp == Dragpanel.this || !(comp instanceof Dragpanel))
                continue;
            Rectangle otherBounds = comp.getBounds();
            if (Math.abs(myRight - otherBounds.x) < SNAP_THRESHOLD) {
                snapInfo.setSnappedRight((Dragpanel) comp);
                break;
            }
        }
        
        // Update the label text using HTML for multi-line support.
        snapInfoLabel.setText("<html>" + snapInfo.toString() + "</html>");
    }
    
    // Updates snap info for all Dragpanel components in the parent container.
    public void updateAllSnapInfo() {
        Container parent = getParent();
        if (parent == null) return;
        for (Component comp : parent.getComponents()) {
            if (comp instanceof Dragpanel) {
                ((Dragpanel) comp).updateSnapInfo();
            }
        }
    }
}
