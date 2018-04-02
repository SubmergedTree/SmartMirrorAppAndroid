package android.smartmirror.view;

import android.smartmirror.model.User;

import java.util.List;

/**
 * Created by jannik on 09.03.18.
 */

public interface ISelectUserActivity  {
    void setUserList(List<User> users);
    void startStartActivity();
    void startModifyProfileActivity(User user);
    void newUserFailure();
}
