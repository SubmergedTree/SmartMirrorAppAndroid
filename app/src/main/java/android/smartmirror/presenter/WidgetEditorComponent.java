package android.smartmirror.presenter;

import android.smartmirror.model.Widget;
import android.smartmirror.model.WidgetPosition;
import android.smartmirror.view.fragments.IWidgetEditorFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 07.04.18.
 */

public class WidgetEditorComponent implements IWidgetEditorComponent {

    private IWidgetEditorFragment widgetEditorFragment;
    private WidgetPosition selectedPosition;

    public WidgetEditorComponent(IWidgetEditorFragment widgetEditorFragment) {
        this.widgetEditorFragment = widgetEditorFragment;
        selectedPosition = WidgetPosition.NONESELECTED;
    }

    @Override
    public void onPositionClicked(WidgetPosition position) {
        selectedPosition = position;
        List<String> names = new ArrayList<>();
        for (Widget w : Widget.values()) {
            names.add(w.name());
        }
        widgetEditorFragment.openChooseWidgetDialog(names.toArray(new String[names.size()]));
    }

    @Override
    public void onWidgetSelected(String name) {
        Widget selectedWidget = Widget.valueOf(name);
        // send widget and position to mirror
    }

    @Override
    public void onWidgetSelectedDialogCancelled() {
        selectedPosition = WidgetPosition.NONESELECTED;
    }
}
