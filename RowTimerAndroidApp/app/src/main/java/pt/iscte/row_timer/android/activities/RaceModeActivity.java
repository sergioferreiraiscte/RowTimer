package pt.iscte.row_timer.android.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RaceModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_mode);
        setTitle(R.string.title_activity_race_mode);
    }
}
