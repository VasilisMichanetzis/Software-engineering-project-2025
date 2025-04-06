package gui1;

import java.awt.*;

public class SnapManager {
    public static SnapResult calculateSnap(Dragpanel current, Container parent, int currentX, int currentY, int snapThreshold) {
        // Iterate over sibling Dragpanel components.
        for (Component comp : parent.getComponents()) {
            if (comp == current || !(comp instanceof Dragpanel))
                continue;
            Rectangle otherBounds = comp.getBounds();
            
            // Check right-edge snap:
            int myRight = currentX + current.getWidth();
            if (Math.abs(myRight - otherBounds.x) < snapThreshold) {
                int snapX = otherBounds.x - current.getWidth();
                int snapY = otherBounds.y; // lock y to neighbor's y
                return new SnapResult(true, 1, snapX, snapY);
            }
            
            // Check left-edge snap:
            int myLeft = currentX;
            int otherRight = otherBounds.x + otherBounds.width;
            if (Math.abs(myLeft - otherRight) < snapThreshold) {
                int snapX = otherRight;
                int snapY = otherBounds.y; // lock y to neighbor's y
                return new SnapResult(true, -1, snapX, snapY);
            }
        }
        return new SnapResult(false, 0, currentX, currentY);
    }
}
