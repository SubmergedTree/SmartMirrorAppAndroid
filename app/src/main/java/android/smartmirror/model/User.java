package android.smartmirror.model;

import android.smartmirror.model.api.pojos.UserPOJO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jannik on 04.03.18.
 */

public class User implements Serializable {
    private String username;
    private String prename;
    private String name;

    public User(UserPOJO userPOJO) {
        this(userPOJO.getUsername(), userPOJO.getPrename(),userPOJO.getName());
    }

    public User(String username, String prename, String name) {
        this.username = username;
        this.prename = prename;
        this.name = name;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPrename(String prename) {
        this.prename = prename;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return username;
    }
}
