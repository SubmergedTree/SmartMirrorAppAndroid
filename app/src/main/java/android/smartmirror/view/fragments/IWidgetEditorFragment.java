package android.smartmirror.view.fragments;

import android.graphics.drawable.Drawable;
import android.smartmirror.model.WidgetPosition;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 07.04.18.
 */

public interface IWidgetEditorFragment {
    void setImage(WidgetPosition w, Drawable drawableImage);
    void openChooseWidgetDialog(String[] names);
}
