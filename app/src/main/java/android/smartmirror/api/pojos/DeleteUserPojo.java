package android.smartmirror.api.pojos;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 21.03.18.
 */

public class DeleteUserPojo {
    private String username;

    public DeleteUserPojo(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
