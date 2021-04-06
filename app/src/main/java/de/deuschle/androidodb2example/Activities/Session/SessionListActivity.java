package de.deuschle.androidodb2example.Activities.Session;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.deuschle.androidodb2example.Database.StreamingDataDatabase.SessionDao;
import de.deuschle.androidodb2example.Database.StreamingDataDatabase.SessionEntity;
import de.deuschle.androidodb2example.Database.StreamingDataDatabase.StreamingDataDatabase;
import de.deuschle.androidodb2example.R;

public class SessionListActivity extends AppCompatActivity {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM);
    private static final String TAG = SessionListActivity.class.getSimpleName();
    private LinearLayout linearLayout;
    final Map<String, SessionEntity> sessionList = new HashMap<>();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_list);

        Toolbar toolbar = findViewById(R.id.sessionListToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayout = findViewById(R.id.sessionList);

        StreamingDataDatabase db = Room.databaseBuilder(getApplicationContext(), StreamingDataDatabase.class, getString(R.string.streaming_data_database_name)).build();
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
        Log.d(TAG, sessionEntityList.toString());
        for (int i = 0; i < sessionEntityList.size(); i++) {
            if (sessionEntityList.get(i).date == null) continue;

            SessionEntity session = sessionEntityList.get(i);
            LinearLayout sessionLayout = new LinearLayout(this);
            TextView sessionIdTextView = new TextView(this);
            TextView sessionDateTextView = new TextView(this);

            String text = "id: " + session.sessionId;
            sessionIdTextView.setText(text);
            text = "date: " + session.date.format(formatter);
            sessionDateTextView.setText(text);

            sessionDateTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3));
            sessionIdTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            sessionLayout.setWeightSum(4);
            sessionLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            sessionLayout.setDividerDrawable(AppCompatResources.getDrawable(this, R.drawable.vertical_divider_keyline_1));
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
        linearLayout.setDividerDrawable(AppCompatResources.getDrawable(this, R.drawable.vertical_divider_keyline_1));
        linearLayout.addView(textView);
    }

    protected void onSessionLayoutClicked(View view) {
        String tag = (String) view.getTag();
        Intent intent = new Intent(this, DisplayDataActivity.class);
        intent.putExtra(DisplayDataActivity.extra, tag);
        startActivity(intent);
    }

}