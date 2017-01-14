package main.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Button button_set, button_unset;
    Context context;
    PendingIntent pending_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;

        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);
        update_text = (TextView) findViewById(R.id.update_text);
        button_set = (Button) findViewById(R.id.button_set);
        button_unset = (Button) findViewById(R.id.button_unset);
        final Calendar calendar = Calendar.getInstance();

        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);


        button_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.set(java.util.Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(java.util.Calendar.MINUTE, alarm_timepicker.getMinute());

                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);


                if(hour > 12) {
                    hour_string = String.valueOf(hour-12);
                }
                if(minute < 10) {
                    minute_string = "0" + String.valueOf(minute);
                }

                update_text.setText("ALARM SET FOR " + hour_string + ":"+minute_string);

                // put in extra string into my_intent
                // that tells the clock that you pressed the "alarm on" button
                my_intent.putExtra("extra", "alarm yes");

                // create a pending intent that delays the intent until the specified calendar time
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // set the alarm manger
                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
            }
        });

        button_unset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_text.setText("ALARM OFF!");

                // cancel the alarm
                alarm_manager.cancel(pending_intent);

                // put in extra string into my_intent
                // that tells the clock that you pressed the "alarm off" button
                my_intent.putExtra("extra", "alarm off");

                // stop the ringtone
                sendBroadcast(my_intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
