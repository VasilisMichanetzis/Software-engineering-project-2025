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
            Container parent = getParent();
            if (parent == null) return;

            // 1) Snapshot before removal
            List<Dragpanel> before = new ArrayList<>(CodeList.getBlocks());
            int removedIndex = before.indexOf(this);

            // 2) Remove from GUI
            parent.remove(ghostPanel);
            parent.remove(this);
            parent.revalidate();
            parent.repaint();

            // 3) Remove from model
            if ("start".equals(type)) 
            {
                CodeList.numstart = 0;
                CodeList.clear();
            } 
            else if ("callfunc".equals(type)) 
            {
                CodeList.removeBlock(this);  // Optionally remove from your model (CodeList)
                //cast this to CallFuncBlock and access the attributes
                if (this instanceof CallFuncBlock) {
                    CallFuncBlock callFuncBlock = (CallFuncBlock) this;
                    StartFuncBlock startf = callFuncBlock.getStartf();
                    EndFuncBlock endf = callFuncBlock.getEndf();

                    // Check if startf and endf are not null, and then remove them
                    if (startf != null) {
                        parent.remove(startf);    // Remove StartFuncBlock from the container
                    }
                    if (endf != null) {
                        parent.remove(endf);      // Remove EndFuncBlock from the container
                    }
                }
                FuncList.clear();
                FuncList.funccreated=0;
            }
            else if ("end".equals(type)) 
            {
                CodeList.numend = 0;
                CodeList.removeBlock(this);
            } 
            else 
            {
                CodeList.removeBlock(this);
            }

            // 4) Shift ALL successors to close the gap
            List<Dragpanel> after = CodeList.getBlocks();
            // start shifting at either removedIndex or 1, whichever is larger
            int start = Math.max(removedIndex, 1);
            for (int i = start; i < after.size(); i++) {
                Dragpanel pred = after.get(i - 1);
                Dragpanel curr = after.get(i);
                curr.setLocation(pred.getX() + pred.getWidth(), pred.getY());
            }
        });
        //add(closeButton);
        
        if(type!="startfunc" && type!="endfunc") {add(closeButton);}
        
        
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
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Container parent = getParent();
                if (parent == null) return;
                parent.remove(ghostPanel);

                if (!snapped) {
                    // existing snap-to-right logic
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
                                
                                //check if other is in codelist or funclist
                                
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
                parent.revalidate();
                parent.repaint();
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
                        // snapshot
                        List<Dragpanel> before = new ArrayList<>(CodeList.getBlocks());
                        int removedIndex = before.indexOf(Dragpanel.this);

                        // remove from model
                        CodeList.removeBlock(Dragpanel.this);

                        // shift ALL successors
                        List<Dragpanel> after = CodeList.getBlocks();
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
                        // still snapped: don’t move
                        return;
                    }
                }

                // compute candidate position
                int candX = getX() + e.getX() - mouseX;
                int candY = getY() + e.getY() - mouseY;

                // ghost-preview logic 
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
                } else {
                    ghostPanel.setVisible(false);
                    parent.remove(ghostPanel);
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
}
