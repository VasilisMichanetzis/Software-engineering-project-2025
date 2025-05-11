package gui1;

import java.util.ArrayList;
import java.util.Arrays;

public class VarList {
    public static final int MAX_VARS = 5;

    // Shared, indexed list
    public static ArrayList<VarEntry> entries = new ArrayList<>(Arrays.asList(
        null, null, null, null, null, null
    ));

    // Adds or replaces at a specific index
    public static void setEntry(int index, VarEntry entry) {
        if (index >= 0 && index < MAX_VARS) {
            entries.set(index, entry);
        }
    }

    // Removes from a specific index
    public static void removeEntry(int index) {
        if (index >= 0 && index < MAX_VARS) {
            entries.set(index, null);
        }
    }

    
    public static void printAll() {
        for (int i = 0; i < MAX_VARS; i++) {
            VarEntry e = entries.get(i);
            System.out.println("[" + i + "]: " + (e != null ? e.toString() : "empty"));
        }
    }
}
