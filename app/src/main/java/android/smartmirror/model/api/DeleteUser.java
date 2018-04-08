package android.smartmirror.model.api;

import android.smartmirror.model.api.pojos.DeleteUserPojo;
import android.smartmirror.model.bluetooth.Connection;

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
        deleteUserPojo = new DeleteUserPojo();
        deleteUserPojo.setUsername(username);
    }

    @Override
    public void requestEnableBluetooth() {}

    @Override
    public void noBluetoothSupported() {}

    @Override
    public void onConnected() {}

    @Override
    public void receive(String msg) {
        if (firstOK) {
            if (msg.equals("OK")) {
                firstOK = false;
                sendJson();
            }
        } else {
            callback.result(msg.equals("OK"));
            Connection.use().remove(ref);
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
