package android.support.v17.leanback.supportleanbackshowcase.recommendations;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/*
 * This class extends BroadcastReceiver and publishes Recommendations when received.
 */
public class RecommendationReceiver extends BroadcastReceiver {
    private static final long INITIAL_DELAY = 5000;
    private static final String TAG = "RecommendationReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            scheduleRecommendationUpdate(context);
            Log.d(TAG,"Receive intent boot completed");
        }
        Log.d(TAG,"Receive intent");
    }

    private void scheduleRecommendationUpdate(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent recommendationIntent = new Intent(context, UpdateRecommendationsService.class);
        PendingIntent alarmIntent = PendingIntent.getService(context, 0, recommendationIntent, 0);

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, INITIAL_DELAY,
                AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
    }
}
