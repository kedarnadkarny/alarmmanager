package main.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Alarm_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("alarmstatus", "We are in thr receiver");

        // fetch extra strings from the intent
        String get_your_string = intent.getExtras().getString("extra");
        Log.e("key", get_your_string);

        // create an intent to the ringtone service
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        // pass the extra string from Main Activity to the RIngont Playing Service
        service_intent.putExtra("extra", get_your_string);

        // start the ringtone service
        context.startService(service_intent);
    }
}
