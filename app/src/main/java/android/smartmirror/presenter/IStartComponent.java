package android.smartmirror.presenter;

import android.content.Context;

/**
 * Created by jannik on 06.03.18.
 */

public interface IStartComponent {

    /**
     * @return bluetooth name of Smart Mirror.
     */
    String getBluetoothName();

    void setBluetoothName(String name);


}
