package android.smartmirror.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Binder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 09.04.18.
 */

public class GridViewImageAdapter extends BaseAdapter {
    private List<Bitmap> bitmaps;
    private Context context;

    public GridViewImageAdapter(Context context) {
        this.context = context;
        bitmaps = new ArrayList<>();
    }

    public void setBitmap(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    @Override
    public int getCount() {
        return bitmaps.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(400, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(bitmaps.get(position));
        return imageView;
    }
}
