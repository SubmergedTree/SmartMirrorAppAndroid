package android.smartmirror.model;

import java.util.List;

/**
 * Created by jannik on 04.03.18.
 */

public class User {
    private String username;
    private String prename;
    private String name;

    List<Widget> widgets;

    public User(String username, String prename, String name, List<Widget> widgets) {
        this.username = username;
        this.prename = prename;
        this.name = name;
        this.widgets = widgets;
    }

    public String getUsername() {
        return username;
    }

    public String getPrename() {
        return prename;
    }

    public String getName() {
        return name;
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPrename(String prename) {
        this.prename = prename;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWidgets(List<Widget> widgets) {
        this.widgets = widgets;
    }
}
