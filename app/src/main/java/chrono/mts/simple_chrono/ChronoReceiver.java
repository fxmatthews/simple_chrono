package chrono.mts.simple_chrono;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ChronoReceiver extends BroadcastReceiver {
    private static final int PERIOD = 1000;

    @Override
    public void onReceive(Context ctxt, Intent i) {
        scheduleAlarms(ctxt);
    }

    static void scheduleAlarms(Context ctxt) {
        AlarmManager mgr = (AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(ctxt, AlarmReceiver.class);

        PendingIntent pi = PendingIntent.getService(ctxt, 0, i, 0);

        mgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + PERIOD, PERIOD, pi);
    }
}
