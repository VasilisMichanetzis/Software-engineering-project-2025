package gui1;

import javax.swing.*;
import java.awt.*;

public class GhostPanelPreview {
    private JPanel ghostPanel = null;
    
    public void update(Container parent, int x, int y, int width, int height, int zOrder) {
        if (ghostPanel == null) {
            ghostPanel = new JPanel();
            ghostPanel.setBackground(Color.GRAY);
            ghostPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
            ghostPanel.setSize(width, height);
            parent.add(ghostPanel);
        }
        ghostPanel.setBounds(x, y, width, height);
        parent.setComponentZOrder(ghostPanel, Math.min(zOrder, parent.getComponentCount() - 1));
        parent.revalidate();
        parent.repaint();
    }
    
    public void remove(Container parent) {
        if (ghostPanel != null) {
            parent.remove(ghostPanel);
            ghostPanel = null;
            parent.revalidate();
            parent.repaint();
        }
    }
}
