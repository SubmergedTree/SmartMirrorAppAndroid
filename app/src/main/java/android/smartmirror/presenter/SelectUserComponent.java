package android.smartmirror.presenter;

import android.smartmirror.model.api.IDeleteUser;
import android.smartmirror.model.api.IGetUsers;
import android.smartmirror.model.api.INewUser;
import android.smartmirror.model.api.RequestFactory;
import android.smartmirror.model.api.pojos.UserPOJO;
import android.smartmirror.model.bluetooth.Connection;
import android.smartmirror.model.User;
import android.smartmirror.view.ISelectUserActivity;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 17.03.18.
 */

//TODO: add reload user Button on menu

public class SelectUserComponent implements ISelectUserComponent, Connection.DisconnectObserver {
    private List<User> users;

    private ISelectUserActivity userActivity;
    public SelectUserComponent(final ISelectUserActivity userActivity) {
        Connection.use().registerDisconnect(new WeakReference<Connection.DisconnectObserver>(this));
        this.userActivity = userActivity;
        this.users = new ArrayList<>();
        getUsers();
    }

    @Override
    public void delete(final int position) {
        System.out.println(users.get(position).getUsername());
        RequestFactory.build(users.get(position).getUsername(), new IDeleteUser(){
            @Override
            public void result(boolean success) {
                System.out.println("delete " + position + " success: " + success);
                if(success && (users.size() > position)) {
                    users.remove(position);
                    userActivity.setUserList(users);
                } else {
                    userActivity.deleteUserFailureToast();
                }
            }
        });
    }

    @Override
    public void select(int position) {
        userActivity.startModifyProfileActivity(users.get(position));
    }

    @Override
    public void logout() {
        Connection.use().cancel();
        userActivity.startStartActivity();
    }

    @Override
    public void newUser(String name, String prename, String username) {
        User user = new User(username, prename, name);
        RequestFactory.build(user, new INewUser() {
            @Override
            public void result(boolean success) {
                System.out.println("newUser suceses:" + success);
                if (!success) {
                    userActivity.newUserFailure();
                } else {
                    getUsers();
                }
            }
        });
    }

    @Override
    public void onDisconnect() {
        Log.e("Component", "disconnect");
        userActivity.startStartActivity();
    }

    private void getUsers() {
        RequestFactory.build(new IGetUsers() {
            @Override
            public void result(List<UserPOJO> userPOJOs) {
                users.clear();
                for (UserPOJO u : userPOJOs) {
                    users.add(new User(u));
                }
                userActivity.setUserList(users);
            }
        });
    }
}
