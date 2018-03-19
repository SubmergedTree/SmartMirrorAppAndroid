package android.smartmirror.view;

import android.os.Bundle;
import android.smartmirror.R;
import android.smartmirror.api.pojos.UserPOJO;
import android.smartmirror.presenter.ISelectUserComponent;
import android.smartmirror.presenter.SelectUserComponent;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
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
    public void setUserList(List<UserPOJO> userPOJOs) {
        final ArrayAdapter adapter = new ArrayAdapter(this, R.layout.user_list,userPOJOs.toArray());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userList.setAdapter(adapter);

            }
        });
    }
}
