package android.smartmirror.model.api.pojos;

import android.smartmirror.model.User;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 19.03.18.
 */

public class UserPOJO {
    private String name;
    private String prename;
    private String username;

    public String getName() {
        return name;
    }

    public String getPrename() {
        return prename;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrename(String prename) {
        this.prename = prename;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
