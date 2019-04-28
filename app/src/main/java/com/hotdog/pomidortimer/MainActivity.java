package com.hotdog.pomidortimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button study;
    TextView clock;
    TextView topText;
    CountDownTimerPausable timer;

    final long twentyFiveMinutes = 60 * 25 * 1000;
    final long fiveMinutes = 60 * 5 * 1000;
    final long INTERVAL = 1000;
    boolean initialClick = true;
    boolean studying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        study = findViewById(R.id.study);
        clock = findViewById(R.id.clock);
        topText = findViewById(R.id.topText);

        run();
    }

    private void run() {
        study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!initialClick && !timer.isPaused) {
                    timer.pause();
                    study.setText("Resume");
                } else if (!initialClick) {
                   timer.start();
                   study.setText("Pause");
                } else {
                    initialClick = false;
                    newTimer(twentyFiveMinutes);
                    topText.setText("Study time!");
                    study.setText("Pause");
                    timer.start();
                }
            }
        });
    }

    private void newTimer(long time) {
        timer = new CountDownTimerPausable(time, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                clock.setText("" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
            }

            @Override
            public void onFinish() {
                timer.cancel();

                if (studying) {
                    newTimer(fiveMinutes);
                    timer.start();
                    topText.setText("Break time!");
                } else {
                    newTimer(twentyFiveMinutes);
                    timer.start();
                    topText.setText("Study time!");
                }
                studying = !studying;
            }
        };
    }


}
