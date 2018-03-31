package android.smartmirror.view;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

/**
 * Created by jannik on 09.03.18.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected void doIntent(Class<? extends BaseActivity> changeTo) {
        Intent intent = new Intent(getBaseContext(),changeTo);
        startActivity(intent);
    }

    protected void doIntent(Class<? extends BaseActivity> changeTo, Serializable toSend, String key) {
        Intent intent = new Intent(getBaseContext(), changeTo);
        intent.putExtra(key,toSend);
        startActivity(intent);
    }

    protected void askForBluetooth() {
        final int REQUEST_ENABLE_BT = 1;
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }
}
