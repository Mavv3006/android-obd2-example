package de.deuschle.androidodb2example.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.deuschle.androidodb2example.Database.MyDatabase;
import de.deuschle.androidodb2example.Database.SessionDao;
import de.deuschle.androidodb2example.Database.SessionEntity;
import de.deuschle.androidodb2example.R;

public class SessionListActivity extends AppCompatActivity {

    private static final String TAG = SessionListActivity.class.getSimpleName();
    private LinearLayout linearLayout;
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
        LiveData<List<SessionEntity>> listLiveData = sessionDao.getAll();

        listLiveData.observe(this, sessionEntityList -> {
            listLiveData.removeObservers(this);

            Log.d(TAG, sessionEntityList.toString());
            if (sessionEntityList.isEmpty()) {
                showEmptyIndicator();
            } else {
                showSessionEntities(sessionEntityList);
            }
        });

    }

    private void showSessionEntities(List<SessionEntity> sessionEntityList) {
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

    private void showEmptyIndicator() {
        TextView textView = new TextView(this);
        textView.setText(R.string.session_list_empty_text);
        textView.setTypeface(textView.getTypeface(), Typeface.ITALIC);
        textView.setGravity(Gravity.CENTER);

        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_BEGINNING);
        linearLayout.setDividerDrawable(AppCompatResources.getDrawable(this, R.drawable.empty_tall_divider));
        linearLayout.addView(textView);
    }

    protected void onSessionLayoutClicked(View view) {
        String tag = (String) view.getTag();
        Intent intent = new Intent(this, DisplayDataActivity.class);
        intent.putExtra(DisplayDataActivity.extra, tag);
        startActivity(intent);
    }

}