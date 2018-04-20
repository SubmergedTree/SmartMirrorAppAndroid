package android.smartmirror.model.api;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.smartmirror.model.bluetooth.Connection;

import java.util.Observer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 12.04.18.
 */

abstract class BaseRequest implements Connection.Observer {

    @Override
    final public void requestEnableBluetooth() {}

    @Override
    final public void noBluetoothSupported() {}

    @Override
    final public void onConnected() {}
}
