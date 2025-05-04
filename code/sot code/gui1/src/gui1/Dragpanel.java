package gui1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Dragpanel extends JPanel {
    private int mouseX, mouseY;
    // screen coords when snapped
    private int snapScreenX, snapScreenY;
    public String type;

    private static final int SNAP_THRESHOLD = 40;
    private static final int UNSNAP_DISTANCE = 100;
    private boolean snapped = false;

    // Ghost preview panel
    private final JPanel ghostPanel;

    public Dragpanel() {
        setBackground(Color.BLUE);
        setPreferredSize(new Dimension(200, 100));
        setBounds(100, 150, 210, 90);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        setLayout(null);

        ghostPanel = new JPanel();
        ghostPanel.setBackground(new Color(0, 0, 0, 100)); // stronger preview
        ghostPanel.setOpaque(true);
        ghostPanel.setVisible(false);

        // CLOSE BUTTON: removes this panel and re-snaps the successor
        JButton closeButton = new JButton("X");
        closeButton.setBounds(85, 3, 40, 40);
        closeButton.setFocusable(false);
        closeButton.setFont(new Font("Arial", Font.BOLD, 7));
        closeButton.addActionListener(e -> {
            Container parent = getParent();
            if (parent == null) return;

            // 1) Snapshot sequence before removal
            List<Dragpanel> before = new ArrayList<>(CodeList.getBlocks());
            int removedIndex = before.indexOf(Dragpanel.this);

            // 2) Remove from GUI
            parent.remove(ghostPanel);
            parent.remove(Dragpanel.this);
            parent.revalidate();
            parent.repaint();

            // 3) Remove from CodeList
            if ("start".equals(type)) {
                CodeList.numstart = 0;
                CodeList.clear();
            } else if ("end".equals(type)) {
                CodeList.numend = 0;
                CodeList.removeBlock(Dragpanel.this);
            } else {
                CodeList.removeBlock(Dragpanel.this);
            }

            // 4) Snap the *next* block (if any) to the *previous* one
            List<Dragpanel> after = CodeList.getBlocks();
            if (removedIndex >= 0 && removedIndex < after.size()) {
                // the block that followed the removed one
                Dragpanel next = after.get(removedIndex);

                // its new predecessor in the list
                int predIndex = removedIndex - 1;
                if (predIndex >= 0) {
                    Dragpanel prev = after.get(predIndex);
                    next.setLocation(prev.getX() + prev.getWidth(), prev.getY());
                }
                // if predIndex < 0, next stays where it is (it’s now the new first block)
            }
        });
        add(closeButton);

        // Mouse listener: track press & release for snap/unsnap
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                Container parent = getParent();
                if (parent != null) {
                    parent.remove(ghostPanel);
                    parent.revalidate();
                    parent.repaint();
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
                                CodeList.insertRightOf(other, Dragpanel.this);
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
                                CodeList.insertRightOf(other, Dragpanel.this);
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

        // Mouse motion listener: drag with unsnap resistance and ghost preview
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Container parent = getParent();
                if (parent == null) return;

                // if snapped, require dragging beyond threshold to unsnap
                if (snapped) {
                    int dx = e.getXOnScreen() - snapScreenX;
                    int dy = e.getYOnScreen() - snapScreenY;
                    if (Math.hypot(dx, dy) < UNSNAP_DISTANCE) {
                        return; // remain snapped
                    }
                    snapped = false;
                    mouseX = e.getX();
                    mouseY = e.getY();
                }

                // calculate new candidate position
                int candX = getX() + e.getX() - mouseX;
                int candY = getY() + e.getY() - mouseY;

                // ghost preview only when not snapped
                int ghostX = -1, ghostY = -1;
                if (!snapped) {
                    for (Component comp : parent.getComponents()) {
                        if (!(comp instanceof Dragpanel) || comp == Dragpanel.this) 
                            continue;
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
                    if (ghostPanel.getParent() != parent) 
                        parent.add(ghostPanel);
                    parent.setComponentZOrder(ghostPanel, parent.getComponentCount() - 1);
                    ghostPanel.setVisible(true);
                } else {
                    ghostPanel.setVisible(false);
                    parent.remove(ghostPanel);
                }

                // clamp and move
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
