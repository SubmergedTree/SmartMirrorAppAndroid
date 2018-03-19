package android.smartmirror.api;

import android.smartmirror.api.pojos.UserPOJO;
import android.smartmirror.bluetooth.Connection;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 18.03.18.
 */

public class GetUsers implements Connection.Observer {
    private IGetUsers callback;
    private ObjectMapper mapper;

    public GetUsers(IGetUsers callback) {
        this.callback = callback;
        this.mapper = new ObjectMapper();
        Connection.use().register(new WeakReference<Connection.Observer>(this));
        Connection.use().send("GETUSERS");
    }


    @Override
    public void requestEnableBluetooth() {}

    @Override
    public void noBluetoothSupported() {}

    @Override
    public void onConnected() {}

    @Override
    public void receive(String msg) {
        Log.i("received", msg);
        List<UserPOJO> userPOJOs = new ArrayList<>();
        try {
            userPOJOs = new ObjectMapper().readValue(msg, new TypeReference<ArrayList<UserPOJO>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        callback.getResult(userPOJOs);
    }
}
