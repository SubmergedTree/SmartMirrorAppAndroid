package android.smartmirror.presenter;

import android.os.CountDownTimer;
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
        Connection.use().setUpBluetooth();
    }

    @Override
    public String getBluetoothName() {
        return Connection.use().getBluetoothName();
    }

    @Override
    public void setBluetoothName(String name) {
        Connection.use().setBluetoothServerName(name);
    }

    @Override
    public void search() {
        iStartActivity.showProgressCircle();
        tryConnect();
        if (Connection.use().isConnected()) {
            return;
        }
     /*   new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                tryConnect();
                iStartActivity.hideProgressCircle();
            }
        }.start();
*/
    }

    private void tryConnect() {
        Connection.use().tryConnectToMirror();
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
