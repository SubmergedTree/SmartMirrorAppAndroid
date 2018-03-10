package android.smartmirror.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jannik on 09.03.18.
 */

public class BaseActivity extends AppCompatActivity {
    protected void doIntent(Class<? extends BaseActivity> changeTo) {
        Intent intent = new Intent(getBaseContext(),changeTo);
        startActivity(intent);
    }
}
