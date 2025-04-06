package gui1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class Dragpanel extends JPanel {
    
	private int mouseX, mouseY;
    
    public String type;
    
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
                    parent.remove(Dragpanel.this); // Remove itself from mainpanel
                    parent.revalidate();
                    parent.repaint();
                }
            }
        });

        this.add(closeButton); // Add delete button to panel
        

        // Mouse listener for dragging
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (getParent() == null) return; // Safety check

                // Calculate new position
                int newX = getX() + e.getX() - mouseX;
                int newY = getY() + e.getY() - mouseY;

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
