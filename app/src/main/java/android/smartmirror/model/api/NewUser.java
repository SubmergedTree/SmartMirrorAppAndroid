package android.smartmirror.model.api;

import android.smartmirror.model.api.pojos.UserPOJO;
import android.smartmirror.model.bluetooth.Connection;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.ref.WeakReference;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 31.03.18.
 */

public class NewUser extends BaseRequest {
    private INewUser callback;
    private int ref;
    private Progress progress;
    private UserPOJO userPOJO;

    private enum Progress {
        SENDNEWUSER,
        SENDJSON;
    }

    public NewUser(UserPOJO userPOJO, INewUser callback) {
        this.callback = callback;
        this.userPOJO = userPOJO;
        ref = Connection.use().register(new WeakReference<Connection.Observer>(this));
        Connection.use().send("NEWUSER");
        progress = Progress.SENDNEWUSER;
    }

    @Override
    public void receive(String msg) {
        Log.e("NewUser", "NewUser");
        Log.e("NewUser", msg);
        if (progress == Progress.SENDNEWUSER) {
            if (msg.equals("OK")) {
                Log.d("NewUser", "Receive OK1");
                try {
                    String json = new ObjectMapper().writeValueAsString(userPOJO);
                    System.out.println(json);
                    Connection.use().send(json);
                    progress = Progress.SENDJSON;
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                callback.result(false);
            }
        } else if (progress == Progress.SENDJSON) {
            if (msg.equals("OK")) {
                Log.d("NewUser", "Receive OK2");
                callback.result(true);
            } else {
                callback.result(false);
            }
            Connection.use().remove(ref);
        }
    }
}
