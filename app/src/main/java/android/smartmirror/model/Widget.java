package android.smartmirror.model;

import android.smartmirror.R;

/**
 * Created by jannik on 04.03.18.
 */

public enum Widget {
    WEATHER_TODAY(R.drawable.ic_dashboard_black_24dp),
    WEATHER_WEEK(R.drawable.ic_dashboard_black_24dp),
    CLOCK(R.drawable.ic_dashboard_black_24dp),
    JOKE(R.drawable.ic_dashboard_black_24dp),
    HEADLINE_NEWS(R.drawable.ic_dashboard_black_24dp);

    private int drawable;

    Widget(int drawable) {
        this.drawable = drawable;
    }

    public int getDrawable() {
        return drawable;
    }



}
