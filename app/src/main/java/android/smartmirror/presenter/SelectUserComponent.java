package android.smartmirror.presenter;

import android.smartmirror.api.GetUsers;
import android.smartmirror.api.IGetUsers;
import android.smartmirror.api.RequestFactory;
import android.smartmirror.api.pojos.UserPOJO;
import android.smartmirror.view.ISelectUserActivity;

import java.util.List;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 17.03.18.
 */

public class SelectUserComponent implements ISelectUserComponent {

    private ISelectUserActivity userActivity;
    public SelectUserComponent(final ISelectUserActivity userActivity) {
        this.userActivity = userActivity;
         RequestFactory.build(new IGetUsers() {
            @Override
            public void getResult(List<UserPOJO> userPOJOs) {
                for (UserPOJO u : userPOJOs) {
                    System.out.println(u.toString());
                }
                userActivity.setUserList(userPOJOs);
            }
        });
    }
}
