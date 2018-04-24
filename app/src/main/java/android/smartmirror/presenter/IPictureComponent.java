package android.smartmirror.presenter;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 07.04.18.
 */

public interface IPictureComponent {
    void getPictureBitmap(Bitmap bitmap);
    void sendToMirror();
    void removeBitmapFromGrid(int position);
    List<Bitmap> getBitmaps();
}
