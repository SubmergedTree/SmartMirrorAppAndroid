package android.smartmirror.presenter;

import android.content.Context;
import android.smartmirror.model.BluetoothName;
import android.smartmirror.model.Connection;
import android.smartmirror.view.IStartActivity;
import android.util.Log;

/**
 * Created by jannik on 07.03.18.
 */

public class StartComponent implements IStartComponent {
    private BluetoothName bluetoothName;
    private IStartActivity iStartActivity;

    public StartComponent(IStartActivity iStartActivity) {
        this.iStartActivity = iStartActivity;
    }

    @Override
    public String getBluetoothName(Context context) {
        return bluetoothName.getName(context);
    }

    @Override
    public void setBluetoothName(Context context, String name) {
        bluetoothName.setName(name,context);
    }

    private void connectToMirror() {
    }

    private void startUserSelection() {
        iStartActivity.startSelectUserActivity();
    }

}
