package android.smartmirror.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.smartmirror.R;
import android.smartmirror.presenter.IStartComponent;
import android.smartmirror.presenter.StartComponent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class StartActivity extends BaseActivity implements IStartActivity {

    private IStartComponent iStartComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        iStartComponent = new StartComponent(this);
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
            View alertView = layoutInflater.inflate(R.layout.start_activity_change_bt_name,null);
            dialogBuilder.setView(alertView);

            final EditText input = new EditText(StartActivity.this);
            LinearLayoutCompat.LayoutParams lp = new LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            input.setText(iStartComponent.getBluetoothName(getApplicationContext()));
            dialogBuilder.setView(input);

            dialogBuilder.setCancelable(false)
                    .setTitle(getResources().getString(R.string.change_bt_name_title))
                    .setPositiveButton(getResources().getString(R.string.change_bt_name_button), new DialogInterface.OnClickListener(){
                          public void onClick(DialogInterface dialog, int id) {
                              iStartComponent.setBluetoothName(getApplicationContext(),input.getText().toString());
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
}