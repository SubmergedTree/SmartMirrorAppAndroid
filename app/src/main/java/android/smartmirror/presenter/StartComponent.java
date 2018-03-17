package android.smartmirror.presenter;

import android.content.Context;
import android.smartmirror.view.IStartActivity;

/**
 * Created by jannik on 07.03.18.
 */

public class StartComponent implements IStartComponent {
    private IStartActivity iStartActivity;

    public StartComponent(IStartActivity iStartActivity) {
        this.iStartActivity = iStartActivity;
    }

    @Override
    public String getBluetoothName(Context context) {
        return "foo";
    }

    @Override
    public void setBluetoothName(Context context, String name) {
    }

    private void connectToMirror() {
    }

    private void startUserSelection() {
        iStartActivity.startSelectUserActivity();
    }

}
