package de.deuschle.androidodb2example.Activities;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Database;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.deuschle.androidodb2example.Activities.DisplayDataActivity;
import de.deuschle.androidodb2example.Database.AmbientTemperatureDao;
import de.deuschle.androidodb2example.Database.MyDatabase;
import de.deuschle.androidodb2example.Database.SessionDao;
import de.deuschle.androidodb2example.Database.SessionEntity;
import de.deuschle.androidodb2example.R;

public class SessionListActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    final Map<String, SessionEntity> sessionList = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_list);

        linearLayout = findViewById(R.id.sessionList);

        Toolbar toolbar = findViewById(R.id.sessionListToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyDatabase db = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, getString(R.string.database_name)).build();
        SessionDao sessionDao = db.getSessionDao();
        List<SessionEntity> sessionEntityList = sessionDao.getAll();

        for (int i = 0; i < sessionEntityList.size(); i++) {
            SessionEntity session = sessionEntityList.get(i);
            LinearLayout sessionLayout = new LinearLayout(this);
            TextView sessionIdTextView = new TextView(this);
            TextView sessionDateTextView = new TextView(this);

            sessionIdTextView.setText(session.sessionId);
            sessionDateTextView.setText(session.date.toString());

            sessionLayout.setWeightSum(2);
            sessionLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            sessionLayout.setDividerDrawable(AppCompatResources.getDrawable(this, R.drawable.empty_tall_divider));
            sessionLayout.addView(sessionIdTextView);
            sessionLayout.addView(sessionDateTextView);

            String sessionId = String.valueOf(session.sessionId);
            sessionList.put(sessionId, session);
            sessionLayout.setTag(sessionId);
            sessionLayout.setOnClickListener(this::onSessionLayoutClicked);
            linearLayout.addView(sessionLayout);
        }
    }

    protected void onSessionLayoutClicked(View view) {
        String tag = (String) view.getTag();
        Intent intent = new Intent(this, DisplayDataActivity.class);
        intent.putExtra(DisplayDataActivity.extra, tag);
        startActivity(intent);
    }

}