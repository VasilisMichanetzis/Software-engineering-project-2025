package gui1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Dragpanel extends JPanel {
    private int mouseX, mouseY;
    private int snapScreenX, snapScreenY;
    public String type;

    private static final int SNAP_THRESHOLD  = 40;
    private static final int UNSNAP_DISTANCE = 100;
    private boolean snapped = false;
    
    // Track the blocks that were shifted during the drag operation
    private List<Dragpanel> shiftedBlocks = new ArrayList<>();
    // Track the block this panel might be inserted after
    private Dragpanel insertAfterBlock = null;

    private final JPanel ghostPanel;
  

    public Dragpanel() {
        setBackground(Color.BLUE);
        setPreferredSize(new Dimension(200, 100));
        setBounds(100, 150, 210, 90);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        setLayout(null);

        ghostPanel = new JPanel();
        ghostPanel.setBackground(new Color(0, 0, 0, 100));
        ghostPanel.setOpaque(true);
        ghostPanel.setVisible(false);

        // CLOSE BUTTON: remove + shift ALL successors
        JButton closeButton = new JButton("X");
        closeButton.setBounds(85, 3, 40, 40);
        closeButton.setFocusable(false);
        closeButton.setFont(new Font("Arial", Font.BOLD, 7));
        closeButton.addActionListener(e -> {
            // 1) Ask for confirmation
            int choice = JOptionPane.showConfirmDialog(
                Dragpanel.this,
                "Are you sure you want to delete this block?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (choice != JOptionPane.YES_OPTION) {
                return;  // user cancelled
            }

            // 2) Snapshot before removal
            Container parent = getParent();
            if (parent == null) return;
            List<Dragpanel> before = new ArrayList<>(CodeList.getBlocks());
            int removedIndex = before.indexOf(this);
            boolean isInFuncList = FuncList.contains(this);
            
            if (isInFuncList) {
                before = new ArrayList<>(FuncList.getBlocks());
                removedIndex = before.indexOf(this);
            }

            // 3) Remove from GUI
            parent.remove(ghostPanel);
            parent.remove(this);
            parent.revalidate();
            parent.repaint();

            // 4) Remove from model
            if ("start".equals(type)) {
                CodeList.numstart = 0;
                CodeList.clear();
            } else if ("callfunc".equals(type)) {
                CodeList.removeBlock(this);
                if (this instanceof CallFuncBlock) {
                    CallFuncBlock callFuncBlock = (CallFuncBlock) this;
                    StartFuncBlock startf = callFuncBlock.getStartf();
                    EndFuncBlock endf = callFuncBlock.getEndf();
                    if (startf != null) parent.remove(startf);
                    if (endf   != null) parent.remove(endf);
                }
                FuncList.clear();
                FuncList.funccreated = 0;
            } else if ("end".equals(type)) {
                CodeList.numend = 0;
                CodeList.removeBlock(this);
            } else {
                // Remove from the appropriate list based on where the block is
                if (isInFuncList) {
                    FuncList.removeBlock(this);
                } else {
                    CodeList.removeBlock(this);
                }
            }

            // 5) Shift successors to close the gap
            List<Dragpanel> after;
            if (isInFuncList) {
                after = FuncList.getBlocks();
            } else {
                after = CodeList.getBlocks();
            }
            
            int start = Math.max(removedIndex, 1);
            for (int i = start; i < after.size(); i++) {
                Dragpanel pred = after.get(i - 1);
                Dragpanel curr = after.get(i);
                curr.setLocation(pred.getX() + pred.getWidth(), pred.getY());
            }
        });
        add(closeButton);

        // MOUSE LISTENER: record origin, clear ghost
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                Container p = getParent();
                if (p != null) {
                    p.remove(ghostPanel);
                    p.revalidate();
                    p.repaint();
                }
                if (snapped) {
                    snapScreenX = e.getXOnScreen();
                    snapScreenY = e.getYOnScreen();
                }
                
                // Clear tracked blocks
                shiftedBlocks.clear();
                insertAfterBlock = null;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Container parent = getParent();
                if (parent == null) return;
                parent.remove(ghostPanel);

                if (!snapped) {
                    // Check if we're inserting between two blocks
                    if (insertAfterBlock != null) {
                        // Calculate the position after the insertAfterBlock
                        setLocation(insertAfterBlock.getX() + insertAfterBlock.getWidth(), insertAfterBlock.getY());
                        
                        // Check if the insertAfterBlock is in CodeList or FuncList
                        boolean isInCodeList = CodeList.contains(insertAfterBlock);
                        boolean isInFuncList = FuncList.contains(insertAfterBlock);
                        
                        // Update the model accordingly
                        if (isInCodeList) {
                            int insertIndex = CodeList.getBlocks().indexOf(insertAfterBlock);
                            if (insertIndex != -1 && insertIndex < CodeList.getBlocks().size() - 1) {
                                CodeList.insertRightOf(insertAfterBlock, Dragpanel.this);
                                snapped = true;
                                snapScreenX = e.getXOnScreen();
                                snapScreenY = e.getYOnScreen();
                            }
                        } else if (isInFuncList) {
                            int insertIndex = FuncList.getBlocks().indexOf(insertAfterBlock);
                            if (insertIndex != -1 && insertIndex < FuncList.getBlocks().size() - 1) {
                                FuncList.insertRightOf(insertAfterBlock, Dragpanel.this);
                                snapped = true;
                                snapScreenX = e.getXOnScreen();
                                snapScreenY = e.getYOnScreen();
                            }
                        }
                    } else {
                        // Original snap-to-right logic
                        for (Component comp : parent.getComponents()) {
                            if (!(comp instanceof Dragpanel) || comp == Dragpanel.this)
                                continue;
                            Dragpanel other = (Dragpanel) comp;
                            int dx, dy;
                            if (Dragpanel.this instanceof StartBlock) {
                                dx = Math.abs((getX() + getWidth()) - other.getX());
                                dy = Math.abs(getY() - other.getY());
                                if (dx <= SNAP_THRESHOLD && dy <= SNAP_THRESHOLD) {
                                    setLocation(other.getX() - getWidth(), other.getY());   
                                    if (CodeList.contains(other)) {
                                        CodeList.insertRightOf(other, Dragpanel.this);
                                    } else if (FuncList.contains(other)) {
                                        FuncList.insertRightOf(other, Dragpanel.this);
                                    }
                                    snapped = true;
                                    snapScreenX = e.getXOnScreen();
                                    snapScreenY = e.getYOnScreen();
                                    break;
                                }
                            } else {
                                dx = Math.abs(getX() - (other.getX() + other.getWidth()));
                                dy = Math.abs(getY() - other.getY());
                                if (dx <= SNAP_THRESHOLD && dy <= SNAP_THRESHOLD) {
                                    setLocation(other.getX() + other.getWidth(), other.getY());
                                    if (CodeList.contains(other)) {
                                        CodeList.insertRightOf(other, Dragpanel.this);
                                    } else if (FuncList.contains(other)) {
                                        FuncList.insertRightOf(other, Dragpanel.this);
                                    }
                                    snapped = true;
                                    snapScreenX = e.getXOnScreen();
                                    snapScreenY = e.getYOnScreen();
                                    break;
                                }
                            }
                        }
                    }
                }
                parent.revalidate();
                parent.repaint();
                
                // Clear tracked blocks
                shiftedBlocks.clear();
                insertAfterBlock = null;
            }
        });

        // MOUSE MOTION: handle drag, unsnap & shift ALL successors, ghost preview
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Container parent = getParent();
                if (parent == null) return;

                // UNSNAP logic: if dragged far enough, unsnap + shift ALL successors
                if (snapped) {
                    int dx = e.getXOnScreen() - snapScreenX;
                    int dy = e.getYOnScreen() - snapScreenY;
                    if (Math.hypot(dx, dy) >= UNSNAP_DISTANCE) {
                        // Check if this block is in CodeList or FuncList
                        boolean isInCodeList = CodeList.contains(Dragpanel.this);
                        boolean isInFuncList = FuncList.contains(Dragpanel.this);
                        
                        List<Dragpanel> before;
                        int removedIndex;
                        
                        if (isInCodeList) {
                            before = new ArrayList<>(CodeList.getBlocks());
                            removedIndex = before.indexOf(Dragpanel.this);
                            CodeList.removeBlock(Dragpanel.this);
                        } else if (isInFuncList) {
                            before = new ArrayList<>(FuncList.getBlocks());
                            removedIndex = before.indexOf(Dragpanel.this);
                            FuncList.removeBlock(Dragpanel.this);
                        } else {
                            // If it's not in either list, we can't properly handle it
                            snapped = false;
                            mouseX = e.getX();
                            mouseY = e.getY();
                            return;
                        }

                        // shift ALL successors
                        List<Dragpanel> after;
                        if (isInCodeList) {
                            after = CodeList.getBlocks();
                        } else {
                            after = FuncList.getBlocks();
                        }
                        
                        int start = Math.max(removedIndex, 1);
                        for (int i = start; i < after.size(); i++) {
                            Dragpanel pred = after.get(i - 1);
                            Dragpanel curr = after.get(i);
                            curr.setLocation(pred.getX() + pred.getWidth(), pred.getY());
                        }

                        snapped = false;
                        mouseX = e.getX();
                        mouseY = e.getY();
                    } else {
                        // still snapped: don't move
                        return;
                    }
                }

                // compute candidate position
                int candX = getX() + e.getX() - mouseX;
                int candY = getY() + e.getY() - mouseY;

                // Reset insert related variables
                insertAfterBlock = null;
                ghostPanel.setVisible(false);
                parent.remove(ghostPanel);
                
                // Restore any previously shifted blocks to their original positions
                if (!shiftedBlocks.isEmpty()) {
                    // First check if we need to restore CodeList blocks
                    List<Dragpanel> codeBlocks = CodeList.getBlocks();
                    for (int i = 0; i < codeBlocks.size(); i++) {
                        if (i > 0) {
                            Dragpanel curr = codeBlocks.get(i);
                            Dragpanel pred = codeBlocks.get(i - 1);
                            curr.setLocation(pred.getX() + pred.getWidth(), pred.getY());
                        }
                    }
                    
                    // Then check if we need to restore FuncList blocks
                    List<Dragpanel> funcBlocks = FuncList.getBlocks();
                    for (int i = 0; i < funcBlocks.size(); i++) {
                        if (i > 0) {
                            Dragpanel curr = funcBlocks.get(i);
                            Dragpanel pred = funcBlocks.get(i - 1);
                            curr.setLocation(pred.getX() + pred.getWidth(), pred.getY());
                        }
                    }
                    
                    shiftedBlocks.clear();
                }

                // First check CodeList blocks for insertion
                checkBlocksForInsertion(CodeList.getBlocks(), candX, candY, parent);
                
                // If we didn't find a place in CodeList, check FuncList
                if (insertAfterBlock == null) {
                    checkBlocksForInsertion(FuncList.getBlocks(), candX, candY, parent);
                }
                
                // If we're not inserting between blocks, check for standard snap points
                if (insertAfterBlock == null) {
                    int ghostX = -1, ghostY = -1;
                    if (!snapped) {
                        for (Component comp : parent.getComponents()) {
                            if (!(comp instanceof Dragpanel) || comp == Dragpanel.this) continue;
                            Dragpanel other = (Dragpanel) comp;
                            int dx2, dy2;
                            if (Dragpanel.this instanceof StartBlock) {
                                dx2 = Math.abs((candX + getWidth()) - other.getX());
                                dy2 = Math.abs(candY - other.getY());
                                if (dx2 <= SNAP_THRESHOLD && dy2 <= SNAP_THRESHOLD) {
                                    ghostX = other.getX() - getWidth();
                                    ghostY = other.getY();
                                    break;
                                }
                            } else {
                                dx2 = Math.abs(candX - (other.getX() + other.getWidth()));
                                dy2 = Math.abs(candY - other.getY());
                                if (dx2 <= SNAP_THRESHOLD && dy2 <= SNAP_THRESHOLD) {
                                    ghostX = other.getX() + other.getWidth();
                                    ghostY = other.getY();
                                    break;
                                }
                            }
                        }
                    }
                    if (ghostX >= 0) {
                        ghostPanel.setBounds(ghostX, ghostY, getWidth(), getHeight());
                        if (ghostPanel.getParent() != parent) parent.add(ghostPanel);
                        parent.setComponentZOrder(ghostPanel, parent.getComponentCount() - 1);
                        ghostPanel.setVisible(true);
                    }
                }

                // clamp & move
                int maxX = parent.getWidth() - getWidth();
                int maxY = parent.getHeight() - getHeight();
                candX = Math.max(0, Math.min(candX, maxX));
                candY = Math.max(0, Math.min(candY, maxY));
                setLocation(candX, candY);
                parent.revalidate();
                parent.repaint();
            }
        });
    }
    
    // Helper method to check a list of blocks for insertion points
    private void checkBlocksForInsertion(List<Dragpanel> blocksInOrder, int candX, int candY, Container parent) {
        for (int i = 0; i < blocksInOrder.size() - 1; i++) {
            Dragpanel prevBlock = blocksInOrder.get(i);
            Dragpanel nextBlock = blocksInOrder.get(i + 1);
            
            // Calculate the connection point between these two blocks
            int connectionX = prevBlock.getX() + prevBlock.getWidth();
            int connectionY = prevBlock.getY();
            
            // Check if we're near the connection point
            int dx = Math.abs(candX - connectionX);
            int dy = Math.abs(candY - connectionY);
            
            if (dx <= SNAP_THRESHOLD && dy <= SNAP_THRESHOLD) {
                // We want to insert between these two blocks
                insertAfterBlock = prevBlock;
                
                // Position ghost panel between the blocks
                ghostPanel.setBounds(connectionX, connectionY, getWidth(), getHeight());
                
                // Temporary shift of all blocks after this point
                for (int j = i + 1; j < blocksInOrder.size(); j++) {
                    Dragpanel blockToShift = blocksInOrder.get(j);
                    blockToShift.setLocation(blockToShift.getX() + getWidth(), blockToShift.getY());
                    shiftedBlocks.add(blockToShift);
                }
                
                if (ghostPanel.getParent() != parent) parent.add(ghostPanel);
                parent.setComponentZOrder(ghostPanel, parent.getComponentCount() - 1);
                ghostPanel.setVisible(true);
                break;
            }
        }
    }
}