package android.smartmirror.model.api;

import android.graphics.Bitmap;
import android.smartmirror.model.api.pojos.NewPicturePOJO;
import android.smartmirror.model.api.pojos.UserPOJO;
import android.smartmirror.model.User;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 18.03.18.
 */

public class RequestFactory {

    public static GetUsers build(IGetUsers cb) {
        return new GetUsers(cb);
    }

    public static DeleteUser build(String username, IDeleteUser du) {
        return new DeleteUser(du, username);
    }

    public static NewUser build(User user, INewUser nu) {
        UserPOJO userPOJO = new UserPOJO();
        userPOJO.setName(user.getName());
        userPOJO.setPrename(user.getPrename());
        userPOJO.setUsername(user.getUsername());
        return new NewUser(userPOJO, nu);
    }

    public static NewPictures build(String[] encodedPictures, String username, INewPictures np) {
        NewPicturePOJO newPicturePOJO = new NewPicturePOJO();
        newPicturePOJO.setPictures(encodedPictures);
        newPicturePOJO.setUser(username);
        return new NewPictures(newPicturePOJO, np);
    }
}
