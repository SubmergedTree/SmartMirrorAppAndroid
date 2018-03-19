package android.smartmirror.api;


import android.smartmirror.api.pojos.UserPOJO;

import java.util.List;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 18.03.18.
 */

/*
We can add a callback method which is called when an error occurred.
 */
public interface IGetUsers {
    void getResult(List<UserPOJO> userPOJOs);
}