package android.smartmirror.model.bluetooth;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.smartmirror.model.bluetooth.exception.NoBluetoothSupportedException;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 17.03.18.
 */

//Todo: Rework this class
public class Connection {
    private static final Connection ourInstance = new Connection();

    public static Connection use() {
        return ourInstance;
    }

    private BluetoothClient bluetoothClient;
    private BluetoothServerName bluetoothServerName;

    private Map<Integer,WeakReference<Observer>> registered;
    private Map<Integer,WeakReference<DisconnectObserver>> disconnectRegistered;

    private int counter;

    private Connection() {
        this.bluetoothClient = new BluetoothClient();
        this.bluetoothServerName = new BluetoothServerName();
        this.registered = new HashMap<>();
        this.disconnectRegistered = new HashMap<>();
        this.counter = 0;
    }

    public int register(WeakReference<Observer> o) {
        registered.put(++counter,o);
        return counter;
    }

    public int registerDisconnect(WeakReference<DisconnectObserver> o) {
        disconnectRegistered.put(++counter,o);
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

    public String getBluetoothName() {
        return bluetoothServerName.getName();
    }

    public void setUpBluetooth() {
        try {
           // if(!bluetoothClient.activateBluetooth()) {
             //   invokeCallbacks(Callbacks.REQUEST_ENABLE_BLUETOOTH);
             //   bluetoothClient.activateBluetooth();
           // }
            bluetoothClient.activateBluetooth();
        } catch (NoBluetoothSupportedException e) {
            invokeCallbacks(Callbacks.NO_BLUETOOTH_SUPPORTED);
        }
    }

    public void tryConnectToMirror() {
        Log.e("Coonection", "tryConnectToMirror");
        final String serverName = bluetoothServerName.getName();
        bluetoothClient.searchDevice(serverName);
    }

    public boolean isConnected() {
        return bluetoothClient.isConnected();
    }

    public void send(String msg) {
       if(!this.isConnected()) {
            Log.e("Connection", "is not connected while trying to send");
           invokeDisconnectCallback();
           return;
        }
        bluetoothClient.send(msg);
    }

    void invokeCallbacks(Callbacks callbacks) {
        invokeCallbacks(callbacks, "");
    }

    @SuppressWarnings("unchecked")
    void invokeCallbacks(final Callbacks callbacks,final String msg) {
        Iterator it = registered.entrySet().iterator();
            while(it.hasNext()) {
            final Map.Entry entry = (Map.Entry) it.next();
            if (entry.getValue() == null) {
                it.remove();
            } else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("received" + callbacks.name());
                        switch (callbacks) {
                            case RECEIVE:
                                System.out.println("received" + msg);
                                ((WeakReference<Observer>) entry.getValue()).get().receive(msg);
                                break;
                            case ON_CONNECTED:
                                ((WeakReference<Observer>) entry.getValue()).get().onConnected();
                                break;
                            case NO_BLUETOOTH_SUPPORTED:
                                ((WeakReference<Observer>) entry.getValue()).get().noBluetoothSupported();
                                break;
                            case REQUEST_ENABLE_BLUETOOTH:
                                //             ((WeakReference<Observer>) entry.getValue()).get().requestEnableBluetooth();
                                break;
                        }
                    }
                });
            }
        }
    }

    @SuppressWarnings("unchecked")
     void invokeDisconnectCallback() {
        Iterator it = disconnectRegistered.entrySet().iterator();
        while(it.hasNext()) {
            final Map.Entry entry = (Map.Entry) it.next();
            if (entry.getValue() == null) {
                it.remove();
            } else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ((WeakReference<DisconnectObserver>) entry.getValue()).get().onDisconnect();
                    }
                });
            }
        }
    }

    public void cancel() {
        Log.e("Connection: ","cancel in Connection");
        registered.clear();
        disconnectRegistered.clear();
        bluetoothClient.cancel();
    }

    public interface Observer {
        @Deprecated
        void requestEnableBluetooth();
        void noBluetoothSupported();
        void onConnected();
        void receive(final String msg);
    }

    public interface DisconnectObserver {
        void onDisconnect();
    }

     enum Callbacks {
        @Deprecated
        REQUEST_ENABLE_BLUETOOTH,
        NO_BLUETOOTH_SUPPORTED,
        ON_CONNECTED,
        RECEIVE
    }
}
