package android.smartmirror.view;

import android.smartmirror.api.pojos.UserPOJO;

import java.util.List;

/**
 * Created by jannik on 09.03.18.
 */

public interface ISelectUserActivity {
    void setUserList(List<UserPOJO> userPOJOs);
}
