package android.smartmirror.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.smartmirror.R;
import android.smartmirror.model.User;
import android.smartmirror.presenter.ISelectUserComponent;
import android.smartmirror.presenter.SelectUserComponent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
            selectUserComponent.logout();
        } else if (item.getItemId() == R.id.new_user_button) {
            newUserAlert();
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
        super.doIntent(ModifyProfileActivity.class, user, "user");
    }

    @Override
    public void newUserFailure() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectUserActivity.this);
                final TextView input = new TextView(SelectUserActivity.this);
                LinearLayoutCompat.LayoutParams lp = new LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setText(R.string.error_new_user);
                dialogBuilder.setView(input);
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

    }

    private void listOnClick() {
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SelectUserActivity.this);
                alertBuilder.setMessage(R.string.validate_delete);
                alertBuilder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectUserComponent.delete(position);
                    }
                });
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void deleteUserFailureToast() {
        Toast.makeText(getApplicationContext(), R.string.error_delete_user, Toast.LENGTH_SHORT).show();
    }

    private void newUserAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectUserActivity.this);
        LayoutInflater layoutInflater = SelectUserActivity.this.getLayoutInflater();
        View newUserView = layoutInflater.inflate(R.layout.select_user_activity_new_user,null);
        dialogBuilder.setView(newUserView);

        final EditText inputName = (EditText) newUserView.findViewById(R.id.new_user_name);
        final EditText inputPrename = (EditText) newUserView.findViewById(R.id.new_user_prename);
        final EditText inputUsername = (EditText) newUserView.findViewById(R.id.new_user_username);
        final Button apply = (Button) newUserView.findViewById(R.id.apply_new_user);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString();
                String prename = inputPrename.getText().toString();
                String username = inputUsername.getText().toString();
                selectUserComponent.newUser(name, prename, username);
                alertDialog.cancel();
            }
        });


    }
}
