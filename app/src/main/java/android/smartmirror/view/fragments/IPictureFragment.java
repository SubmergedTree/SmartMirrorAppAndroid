package android.smartmirror.view.fragments;

import android.graphics.Bitmap;
import android.smartmirror.model.User;

import java.util.List;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 07.04.18.
 */

public interface IPictureFragment {
    void refreshGridView();
    void setBitmapList(List<Bitmap> bitmaps);
    User getUser();
    void toastSendFailure();
}
