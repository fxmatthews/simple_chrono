package chrono.mts.simple_chrono;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;


public class ChronoService extends IntentService {
    public ChronoService() {
        super("ChronoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.err.println("-----------------------------------");
        Log.e(getClass().getSimpleName(), "I ran!");
    }
}
