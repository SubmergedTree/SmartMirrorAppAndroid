package android.smartmirror.model;

/**
 * Created by jannik on 07.03.18.
 */

public class InstanceManager {
    private static final InstanceManager ourInstance = new InstanceManager();
    private BluetoothName bluetoothName;
    private BluetoothClient bluetoothClient;

    public static InstanceManager use() {
        return ourInstance;
    }

    public BluetoothName getBluetoothName() {
        return bluetoothName;
    }

    public void setBluetoothClient(BluetoothClient bluetoothClient) {
        this.bluetoothClient = bluetoothClient;
    }

    private InstanceManager() {
        bluetoothName = new BluetoothName();
    }
}
