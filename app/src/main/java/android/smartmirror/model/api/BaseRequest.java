package android.smartmirror.model.api;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 12.04.18.
 */

abstract class BaseRequest extends HandlerThread {
    public BaseRequest(String name) {
        super(name);
        this.start();
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        runStart();
    }

    protected void informUIThread() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                implCallback();
            }
        });
    }

    protected abstract void implCallback();
    protected abstract void runStart();
}
