package com.example.audiorecorder;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    ImageView record, stop_record, design, play, pause;
    TextView tvTime, tvRecordingPath;
    Handler handler;
    ExecutorService executorService = Executors.newSingleThreadExecutor();


    int seconds = 0;
    String path = null;
    //  LottieAnimationView lavPlaiyng;
    int dummySeconds = 0;
    int playableSeconds = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pause = findViewById(R.id.pause);
        play = findViewById(R.id.ib_play);
        record = findViewById(R.id.record);
        design = findViewById(R.id.iv_simple_bg);
        stop_record = findViewById(R.id.stop_record);
        stop_record.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.INVISIBLE);

        if (micPresent()) {
            requestPermission();
        }
        record.setOnClickListener(view -> {
            stop_record.setVisibility(View.VISIBLE);
            record.setVisibility(View.INVISIBLE);
            try {
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFile(filePath());
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mediaRecorder.prepare();
                mediaRecorder.start();
                Toast.makeText(MainActivity.this, "recording is started", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        stop_record.setOnClickListener(view -> {
            stop_record.setVisibility(View.INVISIBLE);
            record.setVisibility(View.VISIBLE);
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            Toast.makeText(MainActivity.this, "recording is stopped", Toast.LENGTH_LONG).show();
        });

        play.setOnClickListener(view -> {
            play.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.VISIBLE);
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(filePath());
                mediaPlayer.prepare();
                mediaPlayer.start();
                Toast.makeText(MainActivity.this, "recording is played", Toast.LENGTH_LONG).show();
                runTimer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);

                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                Toast.makeText(MainActivity.this, "recording is paused", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission
                (this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions
                    (this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            2);
        }
    }

    private boolean micPresent() {
        return this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    @NonNull
    private String filePath() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File recoed_file = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(recoed_file, " - record" + ".mp3");
        return file.getPath();
    }

    private void runTimer() {
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = (seconds % 360) / 60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs);
                tvTime.setText(time);
                try {
                    seconds++;
                    playableSeconds--;

                    if (playableSeconds == -1) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                        mediaPlayer = new MediaPlayer();
                        playableSeconds = dummySeconds;
                        seconds = 0;
                        handler.removeCallbacksAndMessages(null);
                        pause.setVisibility(View.VISIBLE);
                        play.setVisibility(View.VISIBLE);
                        // lavPlaiyng.setVisibility(View.GONE);

                        return;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 1000);

            }
        });
    }
}