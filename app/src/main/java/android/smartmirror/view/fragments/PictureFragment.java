package android.smartmirror.view.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.smartmirror.R;
import android.smartmirror.presenter.IPictureComponent;
import android.smartmirror.presenter.PictureComponent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 06.04.18.
 */

public class PictureFragment extends Fragment implements IPictureFragment {

    private View view;
    private final int IMAGE_ID = 100;

    private IPictureComponent pictureComponent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pictures,container,false);

        pictureComponent = new PictureComponent();

        return view;
    }

    public void galleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, IMAGE_ID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pictureComponent.getPictureUri(data.getData());
    }
}
