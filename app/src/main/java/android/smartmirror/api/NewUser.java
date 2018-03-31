package android.smartmirror.api;

import android.smartmirror.api.pojos.UserPOJO;
import android.smartmirror.bluetooth.Connection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.ref.WeakReference;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 31.03.18.
 */

public class NewUser implements Connection.Observer {
    private INewUser callback;
    private int ref;
    private Expire expire;
    private UserPOJO userPOJO;

    private enum Expire{
        SENDNEWUSER,
        SENDJSON;
    }

    public NewUser(UserPOJO userPOJO, INewUser callback) {
        this.callback = callback;
        this.userPOJO = userPOJO;
        ref = Connection.use().register(new WeakReference<Connection.Observer>(this));
        Connection.use().send("NEWUSER");
        expire = Expire.SENDNEWUSER;
    }

    @Override
    public void requestEnableBluetooth() {}

    @Override
    public void noBluetoothSupported() {}

    @Override
    public void onConnected() {}

    @Override
    public void receive(String msg) {
        if (expire == Expire.SENDNEWUSER) {
            if (msg.equals("OK")) {
                try {
                    String json = new ObjectMapper().writeValueAsString(userPOJO);
                    Connection.use().send(json);
                    expire = Expire.SENDJSON;
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                callback.result(false);
            }
        } else if (expire == Expire.SENDJSON) {
            if (msg.equals("OK")) {
                callback.result(true);
            } else {
                callback.result(false);
            }
        }
    }
}
