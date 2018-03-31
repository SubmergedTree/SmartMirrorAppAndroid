package android.smartmirror.view;

import android.content.Intent;
import android.os.Bundle;
import android.smartmirror.R;
import android.smartmirror.model.User;
import android.smartmirror.presenter.IModifyProfileComponent;
import android.smartmirror.presenter.ISelectUserComponent;
import android.smartmirror.presenter.ModifyProfileComponent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class ModifyProfileActivity extends BaseActivity implements IModifyProfileActivity {

    private IModifyProfileComponent modifyProfileComponent;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.modify_layout:
                    return true;
                case R.id.learning:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        User selectedUser = (User) intent.getSerializableExtra("UserExtra");

        setContentView(R.layout.activity_modify_profile);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        modifyProfileComponent = new ModifyProfileComponent(selectedUser);
    }

}
