package android.smartmirror.api;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 18.03.18.
 */

public class RequestFactory {
    public static GetUsers build(IGetUsers cb) {
        return new GetUsers(cb);
    }

}
