package android.smartmirror.bluetooth;

import android.content.Context;
import android.smartmirror.bluetooth.exception.NoBluetoothSupportedException;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 17.03.18.
 */

public class Connection {
    private static final Connection ourInstance = new Connection();

    public static Connection use() {
        return ourInstance;
    }

    private BluetoothClient bluetoothClient;
    private BluetoothServerName bluetoothServerName;

    private Map<Integer,WeakReference<Observer>> registered;
    private Timer connectTimer;
    private boolean isTimerRunning;
    private int counter;

    private Connection() {
        this.bluetoothClient = new BluetoothClient();
        this.bluetoothServerName = new BluetoothServerName();
        this.registered = new HashMap();
        this.connectTimer = new Timer();
        this.counter = 0;
        this.isTimerRunning = false;
    }

    public int register(WeakReference<Observer> o) {
        registered.put(++counter,o);
        return counter;
    }

    public void remove(int code) {
        registered.remove(code);
    }

    public void setBluetoothServerName(final String name) {
        bluetoothServerName.setName(name);
    }

    public void setContext(Context context) {
        bluetoothServerName.setApplicationContext(context);
    }

    public void setUpBluetooth() {
        try {
            if(!bluetoothClient.activateBluetooth()) {
                this.requestEnableBluetooth();
            }
        } catch (NoBluetoothSupportedException e) {
            this.noBluetoothSupported();
        }
    }

    public void connectToMirror() {
        final String serverName = bluetoothServerName.getName();

        if (isConnected()) {
            this.cancel();
        }

        if(isTimerRunning) {
            connectTimer.cancel();
            connectTimer.purge();
        }

        if(this.connect(serverName)) {
            return;
        }

        connectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                isTimerRunning = true;
                connect(serverName);
            }
        },1,1000*10);
    }

    // maybe somebody try to call connect from multiple threads -> synchronized
    synchronized private boolean connect(String serverName) {
        if(bluetoothClient.searchMirror(serverName)) {
            bluetoothClient.start();
            isTimerRunning = false;
            connectTimer.cancel();
            connectTimer.purge();
            Log.e("Is connected" ,String.valueOf(isConnected()));
            return true;
        }
        return false;
    }

    public void send(String msg) {
        if(!this.isConnected()) {
            connectToMirror();
        }
        bluetoothClient.send(msg);
    }

    public boolean isConnected() {
        return bluetoothClient.isConnected();
    }

    void receive(String msg) {
        Iterator it = registered.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            if(entry.getValue() == null) {
                it.remove();
            } else {
                ((WeakReference<Observer>)entry.getValue()).get().receive(msg);
            }
        }
    }

    private void noBluetoothSupported() {
        Iterator it = registered.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            if(entry.getValue() == null) {
                it.remove();
            } else {
                ((WeakReference<Observer>)entry.getValue()).get().noBluetoothSupported();
            }
        }
    }

    private void requestEnableBluetooth() {
        Iterator it = registered.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            if(entry.getValue() == null) {
                it.remove();
            } else {
                ((WeakReference<Observer>)entry.getValue()).get().requestEnableBluetooth();
            }
        }
    }

    public void cancel() {
        Log.e("Connection: ","cancel in Connection");
        bluetoothClient.cancel();
    }

    public interface Observer {
        void requestEnableBluetooth();
        void noBluetoothSupported();
        void receive(final String msg);
    }
}
