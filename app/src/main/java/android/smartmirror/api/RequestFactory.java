package android.smartmirror.api;

import android.smartmirror.api.pojos.UserPOJO;
import android.smartmirror.model.User;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 18.03.18.
 */

public class RequestFactory {
    public static GetUsers build(IGetUsers cb) {
        return new GetUsers(cb);
    }
    public static DeleteUser build(IDeleteUser du, String username) {
        return new DeleteUser(du, username);
    }
    public static NewUser build(User user, INewUser nu) {
        return new NewUser(new UserPOJO(user), nu);
    }
}
