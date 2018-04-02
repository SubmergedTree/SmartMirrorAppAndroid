package android.smartmirror.presenter;

import android.smartmirror.model.bluetooth.Connection;
import android.smartmirror.view.IStartActivity;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by jannik on 07.03.18.
 */

public class StartComponent implements IStartComponent, Connection.Observer {
    private IStartActivity iStartActivity;
    private int code;

    public StartComponent(IStartActivity iStartActivity) {
        code = Connection.use().register(new WeakReference<Connection.Observer>(this));
        Connection.use().setContext(iStartActivity.getContext());
        this.iStartActivity = iStartActivity;
        connectToMirror();
    }

    @Override
    public String getBluetoothName() {
        return Connection.use().getBluetoothName();
       // return "raspberrypi";
    }

    @Override
    public void setBluetoothName(String name) {
        Connection.use().stopSearchMirror();
        Connection.use().setBluetoothServerName(name);
     //   Connection.use().connectToMirror();
    }

    private void connectToMirror() {
        Connection.use().setUpBluetooth();
        Connection.use().connectToMirror();
    }

    @Override
    public void requestEnableBluetooth() {
        Log.i("StartComponent", "request Bluetooth");
        iStartActivity.requestBluetoothActivation();
    }

    @Override
    public void noBluetoothSupported() {
        iStartActivity.noBluetoothSupported();
    }

    @Override
    public void onConnected() {
        Log.e("connected", "connected");
        Connection.use().remove(code);
        iStartActivity.startSelectUserActivity();
    }

    @Override
    public void receive(String msg) {
    }
}
