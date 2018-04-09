package android.smartmirror.presenter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.smartmirror.view.fragments.IPictureFragment;


/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 07.04.18.
 */

public class PictureComponent implements IPictureComponent {
    private IPictureFragment pictureFragment;

    public PictureComponent(IPictureFragment pictureFragment) {
        this.pictureFragment = pictureFragment;
    }

    @Override
    public void getPictureBitmap(Bitmap bitmap) {
        System.out.println(bitmap.toString());
        pictureFragment.showPictureOnGrid(bitmap);
    }

    @Override
    public void sendToMirror() {

    }
}
