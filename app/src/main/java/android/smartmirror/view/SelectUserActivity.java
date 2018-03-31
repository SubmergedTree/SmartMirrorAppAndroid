package android.smartmirror.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.smartmirror.R;
import android.smartmirror.api.pojos.UserPOJO;
import android.smartmirror.model.User;
import android.smartmirror.presenter.ISelectUserComponent;
import android.smartmirror.presenter.SelectUserComponent;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 17.03.18.
 */

public class SelectUserActivity extends BaseActivity implements ISelectUserActivity {

    private ISelectUserComponent selectUserComponent;
    private ListView userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        selectUserComponent = new SelectUserComponent(this);
        userList = (ListView) findViewById(R.id.user_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu_user_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logout_button) {
            System.out.println("logout");
            selectUserComponent.logout();
        } else if (item.getItemId() == R.id.new_user_button) {
            //selectUserComponent.newUser();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUserList(List<User> users) {
        final ArrayAdapter adapter = new ArrayAdapter(this, R.layout.user_list,users.toArray());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userList.setAdapter(adapter);
                listOnClick();
            }
        });
    }

    @Override
    public void startStartActivity() {
        super.doIntent(StartActivity.class);
    }

    @Override
    public void startModifyProfileActivity(User user) {

    }

    private void listOnClick() {
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("click");
                selectUserComponent.select(position);
            }
        });

        userList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteAlert(position);
                return false;
            }
        });
    }


    private void deleteAlert(final int position) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(R.string.validate_delete);
        alertBuilder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectUserComponent.delete(position);
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private void newUserAlert() {

    }
}
