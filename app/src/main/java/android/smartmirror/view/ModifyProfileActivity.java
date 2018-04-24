package android.smartmirror.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.smartmirror.R;
import android.smartmirror.model.User;
import android.smartmirror.presenter.IModifyProfileComponent;
import android.smartmirror.view.fragments.PictureFragment;
import android.smartmirror.view.fragments.WidgetEditorFragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

public class ModifyProfileActivity extends BaseActivity {

    private WidgetEditorFragment widgetEditorFragment;
    private PictureFragment pictureFragment;
    private User user;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.modify_layout:
                    loadFragment(widgetEditorFragment);
                    return true;
                case R.id.learning:
                    loadFragment(pictureFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        Bundle userBundle = new Bundle();
        userBundle.putSerializable("user", user);
        widgetEditorFragment = new WidgetEditorFragment();
        widgetEditorFragment.setArguments(userBundle);
        pictureFragment = new PictureFragment();
        pictureFragment.setArguments(userBundle);

        setContentView(R.layout.activity_modify_profile);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new WidgetEditorFragment());

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
    }

    public User getUser() {
        return user;
    }
}
