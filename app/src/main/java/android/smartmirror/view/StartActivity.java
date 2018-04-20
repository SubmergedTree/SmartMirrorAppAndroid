package android.smartmirror.view;

import android.content.Context;
import android.content.DialogInterface;
import android.smartmirror.R;
import android.smartmirror.model.bluetooth.Connection;
import android.smartmirror.presenter.IStartComponent;
import android.smartmirror.presenter.StartComponent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 17.03.18.
 */

public class StartActivity extends BaseActivity implements IStartActivity {

    private IStartComponent iStartComponent;
    private Button searchButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Start Activity", "Launched");
        setContentView(R.layout.activity_start);
        iStartComponent = new StartComponent(this);
        searchButton = (Button)findViewById(R.id.search_bt_device);
        progressBar = (ProgressBar)findViewById(R.id.progressBarConnecting);
        hideProgressCircle();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                iStartComponent.search();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu_start_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.menu_change_bt_name_button){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(StartActivity.this);
            LayoutInflater layoutInflater = StartActivity.this.getLayoutInflater();

            final EditText input = new EditText(StartActivity.this);
            LinearLayoutCompat.LayoutParams lp = new LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            input.setText(iStartComponent.getBluetoothName());
            dialogBuilder.setView(input);

            dialogBuilder.setCancelable(false)
                    .setTitle(getResources().getString(R.string.change_bt_name_title))
                    .setPositiveButton(getResources().getString(R.string.change_bt_name_button), new DialogInterface.OnClickListener(){
                          public void onClick(DialogInterface dialog, int id) {
                              iStartComponent.setBluetoothName(input.getText().toString());
                         }
                    });

            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startSelectUserActivity() {
        super.doIntent(SelectUserActivity.class);
    }

    @Override
    public void noBluetoothSupported() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
        builder.setMessage(R.string.bluetooth_not_supported);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void requestBluetoothActivation() {
        super.askForBluetooth();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void hideProgressCircle() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgressCircle() {
        progressBar.setVisibility(View.VISIBLE);
    }
}
