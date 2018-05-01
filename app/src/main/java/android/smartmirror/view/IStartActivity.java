package android.smartmirror.view;

import android.content.Context;
import android.view.View;

/**
 * Created by jannik on 09.03.18.
 */

public interface IStartActivity {
    void startSelectUserActivity();
    void noBluetoothSupported();
    void requestBluetoothActivation();
    Context getContext();
    void hideProgressCircle();
    void showProgressCircle();
}
