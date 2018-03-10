package android.smartmirror.exceptions;

/**
 * Created by jannik on 04.03.18.
 */

public class NoBluetoothSupportedException extends Exception {
    public NoBluetoothSupportedException() {
        super("This device does not support bluetooth");
    }
}
