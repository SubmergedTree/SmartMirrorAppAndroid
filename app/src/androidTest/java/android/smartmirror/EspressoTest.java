package android.smartmirror;

import android.smartmirror.view.StartActivity;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 02.04.18.
 */

public class EspressoTest {

    @Before
    public void setUp() {
    }

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule<>(StartActivity.class);

    @Test
    public void test_goto_SelectUserActivity() {

    }

}
