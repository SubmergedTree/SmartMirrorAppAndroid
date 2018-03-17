package android.smartmirror.model;

import android.smartmirror.view.BaseActivity;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * Created by jannik on 11.03.18.
 */

public class Connection {
    private static final Connection ourInstance = new Connection();
    private BluetoothClient bluetoothClient;
    private Map<Integer,WeakReference<Observer>> registered;
    private Timer connectTimer;
    private boolean isTimerRunning;
    private int counter;
    private String name;
    public static Connection use() {
        return ourInstance;
    }

    private Connection() {
        this.bluetoothClient = new BluetoothClient();
        this.registered = new HashMap();
        this.connectTimer = new Timer();
        this.counter = 0;
        this.isTimerRunning = false;
    }

    public int register(WeakReference<Observer> o) {
        registered.put(++counter,o);
        return counter;
    }

    public void activateBluetooth(BaseActivity activity) {

    }

    public void sendToMirror() {

    }

    public interface Observer {
        void receive(String msg);
        void requestEnableBluetooth();
        void noBluetoothSupported();
    }

}
