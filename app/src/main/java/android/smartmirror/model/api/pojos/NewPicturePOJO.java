package android.smartmirror.model.api.pojos;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 23.04.18.
 */

public class NewPicturePOJO {
    private String user;
    private String[] pictures;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String[] getPictures() {
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }
}
