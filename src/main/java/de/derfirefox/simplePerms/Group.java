package de.derfirefox.simplePerms;

import java.util.ArrayList;
import java.util.List;

// Klasse für Gruppen (Roles)
public class Group {

    private final String name;
    private String prefix = "";
    private int weight = 0;
    private final List<String> permissions = new ArrayList<>();

    public Group(String name) {
        this.name = name.toLowerCase();
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}
