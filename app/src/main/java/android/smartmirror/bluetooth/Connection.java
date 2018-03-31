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

//Todo: we need a callback which is triggered by a server error.
//Todo: we need a method to terminate client.
// -> this method should be invoked when user presses in user select activity the back button.
// that would probably be the nicest solution. But I think the client side code is robust enough to handel this case.

public class Connection {
    private static final Connection ourInstance = new Connection();

    public static Connection use() {
        return ourInstance;
    }

    private BluetoothClient bluetoothClient;
    private BluetoothServerName bluetoothServerName;

    private Map<Integer,WeakReference<Observer>> registered;
    private Map<Integer,WeakReference<DisconnectObserver>> disconnectRegistered;
    private Timer connectTimer;
    private boolean isTimerRunning;
    private int counter;

    private Connection() {
        this.bluetoothClient = new BluetoothClient();
        this.bluetoothServerName = new BluetoothServerName();
        this.registered = new HashMap();
        this.disconnectRegistered = new HashMap<>();
      // this.connectTimer = new Timer();
        this.counter = 0;
        this.isTimerRunning = false;
    }

    public int register(WeakReference<Observer> o) {
        registered.put(++counter,o);
        return counter;
    }

    public int registerDisconnect(WeakReference<DisconnectObserver> o) {
        disconnectRegistered.put(1,o);
        return 0;
    }

    public void removeDisconnect(int code) {
        //TODO
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

    public String getBluetoothName() {
        return bluetoothServerName.getName();
    }

    public void setUpBluetooth() {
        try {
            if(!bluetoothClient.activateBluetooth()) {
                this.requestEnableBluetooth();
                bluetoothClient.activateBluetooth();
            }
        } catch (NoBluetoothSupportedException e) {
            this.noBluetoothSupported();
        }
    }

    public void connectToMirror() {
        final String serverName = bluetoothServerName.getName();
        connectTimer = new Timer();

        if (isConnected()) {
            this.cancel();
        }

        if(isTimerRunning) {
            stopSearchMirror();
        }

        if(this.connect(serverName)) {
            return;
        }
        connectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e("search", "is searching");
                isTimerRunning = true;
                connect(serverName);
            }
        },1,1000*10);
    }

    private boolean connect(String serverName) {
        if(bluetoothClient.searchMirror(serverName)) {
            bluetoothClient.start();
            isTimerRunning = false;
            connectTimer.cancel();
            connectTimer.purge();
            Log.e("Is connected" ,String.valueOf(isConnected()));
            onConnected();
            return true;
        }
        return false;
    }

    public void stopSearchMirror() {
        isTimerRunning = false;
        connectTimer.cancel();
        connectTimer.purge();
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
            final Map.Entry entry = (Map.Entry)it.next();
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
        Log.i("connection", "request bluetooth");
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

    private void onConnected() {
        Iterator it = registered.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            if(entry.getValue() == null) {
                it.remove();
            } else {
                ((WeakReference<Observer>)entry.getValue()).get().onConnected();
            }
        }
    }

    void onConnectionCanceled() {
        Iterator it = disconnectRegistered.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            if(entry.getValue() == null) {
                it.remove();
            } else {
                ((WeakReference<DisconnectObserver>)entry.getValue()).get().onDisconnect();
            }
        }
    }

    public void cancel() {
        Log.e("Connection: ","cancel in Connection");
        //remove references
        bluetoothClient.cancel();
    }

    public interface Observer {
        void requestEnableBluetooth();
        void noBluetoothSupported();
        void onConnected();
        void receive(final String msg);
    }

    public interface DisconnectObserver {
        void onDisconnect();
    }
}
