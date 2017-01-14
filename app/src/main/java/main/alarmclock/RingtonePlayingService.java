package main.alarmclock;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class RingtonePlayingService extends Service {

    MediaPlayer media_song;
    int startId;
    boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        // fetch the extra string values
        String state = intent.getExtras().getString("extra");

        Log.e("Ringtine state", state);

        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                Log.e("Ringtine state", state);
                break;
            default:
                startId = 0;
                break;
        }

        // if there is no music playing, and the user presses "alarm on"
        if(!this.isRunning && startId == 1) {
            Log.e("Ringtone state", "No music. You want to play");
            // create and instance of the media player
            media_song = MediaPlayer.create(this, R.raw.ringingclock);
            media_song.start();
            this.isRunning = true;
            this.startId = 0;
        }

        // if there is music playing, and the user presses "alarm off"
        else if(this.isRunning && startId == 0) {
            Log.e("Ringtone state", "Music. You want to end");

            // stop the ringtone
            media_song.stop();
            media_song.reset();
            this.isRunning = false;
            this.startId = 0;
        }

        // these are if the user presses random buttons
        // just to bug-proof the app
        // if there is no music playing and the user presses "alrm off"
        // do nothing
        else if(!this.isRunning && startId == 0) {
            Log.e("Ringtone state", "No music. You want to end");

            this.isRunning = false;
            this.startId = 0;
        }

        // if there is music playing and the user presses "alarm on"
        // do nothing
        else if(this.isRunning && startId == 1) {
            Log.e("Ringtone state", "Music. You want to play");

            this.isRunning = true;
            this.startId = 1;
        }

        // can't think of anything else, just to catch the odd event
        else {
            Log.e("Ringtone state", "weird");
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        Toast.makeText(this, "On Destroy called", Toast.LENGTH_SHORT).show();
    }
}
