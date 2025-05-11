package gui1;

public class VarEntry {
	public Varpanel panel;
    public String name;
    public int value;
    public int index;

    public VarEntry(String name, int value,int index) {
        this.name = name;
        this.value = value;
        this.index = index;
    }

    public String toString() {
        return name + " = " + value;
    }
}
