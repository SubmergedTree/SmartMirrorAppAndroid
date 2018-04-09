package android.smartmirror.view.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.smartmirror.R;
import android.smartmirror.presenter.IPictureComponent;
import android.smartmirror.presenter.PictureComponent;
import android.smartmirror.view.adapter.GridViewImageAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 06.04.18.
 */

public class PictureFragment extends Fragment implements IPictureFragment {

    private View view;
    private final int IMAGE_FROM_GALLERY_ID = 100;
    private final int IMAGE_FROM_CAMERA_ID = 200;

    private IPictureComponent pictureComponent;

    private Button useCamera;
    private Button useGallery;
    private Button send;
    private GridView gridView;
    private GridViewImageAdapter gridViewImageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pictures,container,false);

        useCamera = (Button) view.findViewById(R.id.camera);
        useGallery = (Button) view.findViewById(R.id.gallery);
        send = (Button) view.findViewById(R.id.send);
        gridView = (GridView)view.findViewById(R.id.gridview);

        gridViewImageAdapter = new GridViewImageAdapter(getActivity());
        gridView.setAdapter(gridViewImageAdapter);

        buttonListener();
        pictureComponent = new PictureComponent(this);
        return view;
    }

    private void buttonListener() {
        useCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });

        useGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryIntent();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridViewImageAdapter.reset();
                pictureComponent.sendToMirror();
            }
        });
    }

    public void showPictureOnGrid(Bitmap bitmap) {
        gridViewImageAdapter.addBitmap(bitmap);
        gridView.setAdapter(gridViewImageAdapter);
    }

    @Override
    public void removeAllPicturesFromGrid() {
        gridViewImageAdapter.reset();
        gridView.setAdapter(gridViewImageAdapter);
    }

    public void galleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, IMAGE_FROM_GALLERY_ID);
    }

    public void cameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, IMAGE_FROM_CAMERA_ID);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_FROM_GALLERY_ID) {
                Uri imageURI = data.getData();
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(imageURI);
                    Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);
                    Bitmap scaledImage = Bitmap.createScaledBitmap(imageBitmap, 400, 300, true);
                    pictureComponent.getPictureBitmap(scaledImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == IMAGE_FROM_CAMERA_ID) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                //Bitmap scaledImage = Bitmap.createScaledBitmap(imageBitmap, 400, 300, true);
                pictureComponent.getPictureBitmap(imageBitmap);
            }
        }
    }
}
