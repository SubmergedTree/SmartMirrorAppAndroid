package android.smartmirror.view.fragments;

import android.graphics.Bitmap;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 07.04.18.
 */

public interface IPictureFragment {
    void showPictureOnGrid(Bitmap bitmap);
    void removeAllPicturesFromGrid();
}
