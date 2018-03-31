package android.smartmirror.api;

import android.smartmirror.api.pojos.DeleteUserPojo;
import android.smartmirror.api.pojos.UserPOJO;
import android.smartmirror.bluetooth.Connection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.ref.WeakReference;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 21.03.18.
 */

public class DeleteUser implements Connection.Observer {
    private IDeleteUser callback;
    private int ref;
    private boolean firstOK;
    private DeleteUserPojo deleteUserPojo;

    public DeleteUser(IDeleteUser callback, String username) {
        this.callback = callback;
        ref = Connection.use().register(new WeakReference<Connection.Observer>(this));
        Connection.use().send("DELETEUSER");
        firstOK = true;
        deleteUserPojo = new DeleteUserPojo(username);
    }

    @Override
    public void requestEnableBluetooth() {}

    @Override
    public void noBluetoothSupported() {}

    @Override
    public void onConnected() {}

    // should this use a thread ?
    @Override
    public void receive(String msg) {
        if (firstOK) {
            if (msg.equals("OK")) {
                firstOK = false;
                sendJson();
            }
        } else {
            callback.result(msg.equals("OK"));
        }
    }

    private void sendJson() {
        try {
            String json = new ObjectMapper().writeValueAsString(deleteUserPojo);
            Connection.use().send(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
