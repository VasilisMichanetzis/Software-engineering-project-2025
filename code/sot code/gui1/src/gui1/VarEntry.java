package gui1;

public class VarEntry {
    public String name;
    public String value;
    public int index;

    public VarEntry(String name, String value,int index) {
        this.name = name;
        this.value = value;
        this.index = index;
    }

    public String toString() {
        return name + " = " + value;
    }
}
