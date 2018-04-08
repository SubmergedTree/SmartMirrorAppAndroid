package android.smartmirror.view.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.smartmirror.R;
import android.smartmirror.model.WidgetPosition;
import android.smartmirror.presenter.IWidgetEditorComponent;
import android.smartmirror.presenter.WidgetEditorComponent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 06.04.18.
 */

public class WidgetEditorFragment extends Fragment implements IWidgetEditorFragment {


    private View view;
    private ImageButton leftTop;
    private ImageButton rightTop;
    private ImageButton leftDown;
    private ImageButton rightDown;

    private IWidgetEditorComponent widgetEditorComponent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_widgets_editor,container,false);
        leftTop = (ImageButton)view.findViewById(R.id.leftTop);
        rightTop = (ImageButton)view.findViewById(R.id.rightTop);
        leftDown = (ImageButton)view.findViewById(R.id.leftDown);
        rightDown = (ImageButton)view.findViewById(R.id.rightDown);
        widgetEditorComponent = new WidgetEditorComponent(this);
        buttonPressed();
        return view;
    }

    private void buttonPressed() {
        leftTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widgetEditorComponent.onPositionClicked(WidgetPosition.LEFTTOP);
            }
        });

        rightTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widgetEditorComponent.onPositionClicked(WidgetPosition.RIGHTTOP);
            }
        });

        leftDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widgetEditorComponent.onPositionClicked(WidgetPosition.LEFTDOWN);
            }
        });

        rightDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widgetEditorComponent.onPositionClicked(WidgetPosition.RIGHTDOWN);
            }
        });
    }

    @Override
    public void setImage(WidgetPosition w, Drawable drawableImage) {
        switch (w) {
            case LEFTTOP:
                leftTop.setImageDrawable(drawableImage);
                break;
            case LEFTDOWN:
                rightTop.setImageDrawable(drawableImage);
                break;
            case RIGHTTOP:
                leftDown.setImageDrawable(drawableImage);
                break;
            case RIGHTDOWN:
                rightDown.setImageDrawable(drawableImage);
                break;
        }
    }

    @Override
    public void openChooseWidgetDialog(final String[] names) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.choose_widget_dialog_title);
        builder.setItems(names, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selected = names[which];
                widgetEditorComponent.onWidgetSelected(selected);
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                widgetEditorComponent.onWidgetSelectedDialogCancelled();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
