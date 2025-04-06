package gui1;

public class SnapInfo {
    private boolean snappedLeft;
    private boolean snappedRight;
    private Dragpanel leftNeighbor;
    private Dragpanel rightNeighbor;
    
    public SnapInfo() {
        reset();
    }
    
    public void reset() {
        snappedLeft = false;
        snappedRight = false;
        leftNeighbor = null;
        rightNeighbor = null;
    }
    
    public void setSnappedLeft(Dragpanel neighbor) {
        snappedLeft = true;
        leftNeighbor = neighbor;
    }
    
    public void setSnappedRight(Dragpanel neighbor) {
        snappedRight = true;
        rightNeighbor = neighbor;
    }
    
    public boolean isSnappedLeft() {
        return snappedLeft;
    }
    
    public boolean isSnappedRight() {
        return snappedRight;
    }
    
    public Dragpanel getLeftNeighbor() {
        return leftNeighbor;
    }
    
    public Dragpanel getRightNeighbor() {
        return rightNeighbor;
    }
    
    public String getSnapSide() {
        if(snappedLeft && snappedRight) {
            return "Both";
        } else if(snappedLeft) {
            return "Left";
        } else if(snappedRight) {
            return "Right";
        } else {
            return "None";
        }
    }
    
    @Override
    public String toString() {
        return "SnapInfo [Side=" + getSnapSide() +
                ", leftNeighbor=" + (leftNeighbor != null ? leftNeighbor.getId() : "none") +
                ", rightNeighbor=" + (rightNeighbor != null ? rightNeighbor.getId() : "none") + "]";
    }
}
