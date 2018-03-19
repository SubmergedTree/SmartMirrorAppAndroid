package android.smartmirror.bluetooth.exception;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 18.03.18.
 */

public class NoFittingUUIDException extends Exception {
    public NoFittingUUIDException() {
        super("No fitting UUID discovered (maybe)");
    }

}
