package gui1;

public class SnapResult {
    private final boolean snapped;
    private final int snapState; // 1 = snapped with right edge, -1 = snapped with left edge, 0 = no snap
    private final int snapX;
    private final int snapY;
    
    public SnapResult(boolean snapped, int snapState, int snapX, int snapY) {
        this.snapped = snapped;
        this.snapState = snapState;
        this.snapX = snapX;
        this.snapY = snapY;
    }
    
    public boolean isSnapped() {
        return snapped;
    }
    public int getSnapState() {
        return snapState;
    }
    public int getSnapX() {
        return snapX;
    }
    public int getSnapY() {
        return snapY;
    }
}
