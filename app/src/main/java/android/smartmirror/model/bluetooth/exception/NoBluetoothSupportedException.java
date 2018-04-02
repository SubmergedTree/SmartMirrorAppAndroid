package android.smartmirror.model.bluetooth.exception;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 17.03.18.
 */

public class NoBluetoothSupportedException extends Exception {
    public NoBluetoothSupportedException() {
        super("This device does not support bluetooth");
    }
}
