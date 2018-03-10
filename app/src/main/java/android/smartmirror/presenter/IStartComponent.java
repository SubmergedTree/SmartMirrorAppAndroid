package android.smartmirror.presenter;

import android.content.Context;

/**
 * Created by jannik on 06.03.18.
 */

public interface IStartComponent {

    /**
     *
     * @param context android context.
     * @return bluetooth name of Smart Mirror.
     */
    String getBluetoothName(Context context);

    void setBluetoothName(Context context, String name);



}
