package android.smartmirror.presenter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.smartmirror.model.api.INewPictures;
import android.smartmirror.model.api.RequestFactory;
import android.smartmirror.view.fragments.IPictureFragment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 07.04.18.
 */

public class PictureComponent implements IPictureComponent {
    private IPictureFragment pictureFragment;
    private List<Bitmap> bitmaps;

    public PictureComponent(IPictureFragment pictureFragment) {
        this.pictureFragment = pictureFragment;
        bitmaps = new ArrayList<>();
        //pictureFragment.setBitmapList(bitmaps);
    }

    @Override
    public void getPictureBitmap(Bitmap bitmap) {
        bitmaps.add(bitmap);
        pictureFragment.refreshGridView();
    }

    @Override
    public void sendToMirror() {
        RequestFactory.build(encodePictures(), pictureFragment.getUser().getUsername() /*is null*/, new INewPictures() {
            @Override
            public void result(boolean success) {
                if (success) {
                    bitmaps.clear();
                    pictureFragment.refreshGridView();
                } else {
                    pictureFragment.toastSendFailure();
                }
            }
        });
    }

    @Override
    public void removeBitmapFromGrid(int position) {
        bitmaps.remove(position);
        pictureFragment.refreshGridView();
    }

    @Override
    public List<Bitmap> getBitmaps() {
        return bitmaps;
    }

    private String[] encodePictures() {
        String[] encoded = new String[bitmaps.size()];
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        for (int i = 0; i < bitmaps.size(); i++) {
            bitmaps.get(i).compress(Bitmap.CompressFormat.PNG, 100, stream);
            encoded[0] = Base64.encodeToString(stream.toByteArray(),Base64.DEFAULT);
        }
        return encoded;
    }

}
