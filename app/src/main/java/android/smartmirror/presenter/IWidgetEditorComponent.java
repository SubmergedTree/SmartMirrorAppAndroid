package android.smartmirror.presenter;

import android.smartmirror.model.WidgetPosition;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 07.04.18.
 */

public interface IWidgetEditorComponent {
    void onPositionClicked(WidgetPosition position);
    void onWidgetSelected(String name);
    void onWidgetSelectedDialogCancelled();
}
