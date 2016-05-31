package pt.iscte.row_timer.android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pt.iscte.row_timer.android.RowTimerApplication;

public class SettingsActivity extends AppCompatActivity {

    private RowTimerApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        application = (RowTimerApplication) getApplication();
        final TextView tvString;
        final Button btnLogout;
        final Button btnLogin;

        if (application.getLogin() != null){
            tvString = (TextView) findViewById(R.id.tvUsername);
            tvString.setText(application.getLogin().getUsername());
            btnLogout = (Button) findViewById(R.id.btnLogout);
            btnLogout.setVisibility(View.VISIBLE);
            btnLogin = (Button) findViewById(R.id.btnLogin);
            btnLogout.setVisibility(View.INVISIBLE);
        }else {
            tvString = (TextView) findViewById(R.id.tvUsername);
            tvString.setText("You are not currently logged in!");
            btnLogout = (Button) findViewById(R.id.btnLogout);
            btnLogout.setVisibility(View.INVISIBLE);
            btnLogin = (Button) findViewById(R.id.btnLogin);
            btnLogout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, EventsListActivity.class);
        this.startActivity(intent);
        return true;
    }
}
