package chrono.mts.simple_chrono;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    EditText loopNumberInput;
    TextView timerText, currentTimerText, currentLoopText;

    Button start, reset;

    long millisecondTime, StartTime, timeBuffer, updateTime = 0L;

    Handler handler;

    int seconds, minutes;

    String[] ListElements = new String[]{};

    List<String> ListElementsArrayList;


    ArrayAdapter<String> adapter;

    boolean chronoIsRunning = false;

    MediaPlayer timerEndSound, circuitEndSound;

    // 1 circuit comprend plusieurs loop. 1 loop comprend plusieurs timers

    int loopNumber = 2;
    int currentLoop = 1;
    Integer[] timersList = new Integer[]{1, 2}; // chaque case du tableau correspond à la valeur
    // d'un timer (en secondes)
    int currentTimer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timerEndSound = MediaPlayer.create(this, R.raw.end_timer);
        circuitEndSound = MediaPlayer.create(this, R.raw.end_circuit);

        setContentView(R.layout.activity_main);

        timerText = findViewById(R.id.textView);
        currentLoopText = findViewById(R.id.currentLoop);
        currentTimerText = findViewById(R.id.currentTimer);

        loopNumberInput = findViewById(R.id.loopNumberInput);

        start = findViewById(R.id.startTimer);
        reset = findViewById(R.id.resetButton);

        handler = new Handler();

        ListElementsArrayList = new ArrayList<>(Arrays.asList(ListElements));

        adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1,
                ListElementsArrayList
        );


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!chronoIsRunning) {
                    currentTimerText.setText("1");
                    currentLoopText.setText("1");
                    if (loopNumberInput.getText() != null) {
                        loopNumber = Integer.getInteger(loopNumberInput.getText().toString(),
                                loopNumber);
                    }
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(circuitRunnable, 0);
                    reset.setEnabled(false);
                    ((TextView) findViewById(R.id.startTimer)).setText("Pause");
                    chronoIsRunning = true;
                } else {
                    timeBuffer += millisecondTime;
                    handler.removeCallbacks(circuitRunnable);
                    reset.setEnabled(true);
                    ((TextView) findViewById(R.id.startTimer)).setText("Start");
                    chronoIsRunning = false;
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
    }

    public Runnable circuitRunnable = new Runnable() {

        public void run() {
            millisecondTime = SystemClock.uptimeMillis() - StartTime;
            updateTime = timeBuffer + millisecondTime;
            int timerValue = timersList[currentTimer];
            int timeLeftInSeconds = timerValue - (int) (updateTime / 1000);

            if (timeLeftInSeconds < 0) {
                StartTime = SystemClock.uptimeMillis();
                timeBuffer = 0;
                currentTimer++;
                if (currentTimer == timersList.length) {
                    currentTimer = 0;
                    currentLoop++;
                    if (currentLoop > loopNumber) {
                        chronoIsRunning = false;
                        circuitEndSound.start();
                        reset();
                        return;
                    }
                    currentLoopText.setText(String.valueOf(currentLoop));
                }
                currentTimerText.setText(String.valueOf(currentTimer + 1));
                timerEndSound.start();
            } else {
                minutes = timeLeftInSeconds / 60;
                seconds = timeLeftInSeconds % 60;
                timerText.setText("" + minutes + ":" + String.format("%02d", seconds));
            }
            handler.postDelayed(this, 0);
        }
    };

    public void reset() {
        millisecondTime = 0L;
        StartTime = 0L;
        timeBuffer = 0L;
        updateTime = 0L;
        seconds = 0;
        minutes = 0;
        timerText.setText("00:00");
        ListElementsArrayList.clear();
        adapter.notifyDataSetChanged();
        ((TextView) findViewById(R.id.startTimer)).setText("Start");
    }
}