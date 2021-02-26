package com.example.audio;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AudioInterface {
    private Chronometer mTextViewCountDown;
    private File[] recentAudio;
    private int[] colors = new int[]{R.color.white_green, R.color.sea_green, R.color.off_white,
            R.color.brown, R.color.light_green, R.color.peach};
    private Button mButtonPause;
    private Button play_button;
    int count = 0;
    AudioInterface audioInterface;
    Chronometer chrono;
    RecyclerView recyclerView;
    SeekBar seekBar;
    AudioAdaptor audioAdaptor;
    File file2;
    File myDir;
    ArrayList<File> files   = new ArrayList<>();
    TextView recording;
    MediaRecorder mediaRecorder;
//    File myDir = new File(Environment.getExternalStorageDirectory() + "/" + "MyAudio/");
//    String audiFilePath = "DemoApp" + "_" + DateFormat.format("MM-dd-yy hh-mm-ss", new Date().getTime()) + ".mp3";

    // public static String filename = "recorded.3gp";
    // String file = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListeners();

        String status = Environment.getExternalStorageState();
        if (status.equals("mounted")) {
            myDir = new File(Environment.getExternalStorageDirectory() + "/" + "MyAudio/");
            if (!myDir.exists())
                myDir.mkdirs();
            String fname3 = getString(R.string.app_name) + "_" + DateFormat.format("MM-dd-yy hh-mm-ss", new Date().getTime()) + ".mp3";
            file2 = new File(myDir, fname3);
        }

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(file2.getAbsolutePath());

//        String status = Environment.getExternalStorageState();
//        if(status.equals("mounted")){
//            if (!myDir.exists())
//                myDir.mkdirs();
//            mediaRecorder = new MediaRecorder();
//
//            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//            mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//            mediaRecorder.setOutputFile(myDir);
//
//
//
//
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recentAudio = new File[myDir.listFiles().length];
        recentAudio = myDir.listFiles();
        Log.d("mainDir", String.valueOf(myDir.listFiles().length));

        if (recentAudio.length != 0) {
            for(int i=0; i < recentAudio.length; i++){
                files.add(recentAudio[i]);
            }
            audioAdaptor = new AudioAdaptor(files, colors, MainActivity.this);
            recyclerView.setAdapter(audioAdaptor);
        }

    }


    private void initListeners() {

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initView() {
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        recyclerView = findViewById(R.id.audio_recyclerview);
        mButtonPause = findViewById(R.id.pause_button);
        play_button = findViewById(R.id.play_button);
        chrono = (Chronometer) findViewById(R.id.text_view_countdown);

    }



    private void stopRecording() {

        try {
            ((Chronometer) findViewById(R.id.text_view_countdown)).stop();
            mediaRecorder.reset();
            mediaRecorder.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRecording() {
        mediaRecorder.setMaxDuration(10000);
        try {

            mediaRecorder.prepare();
            mediaRecorder.start();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void addRecording(View view) {
        chrono.setBase(SystemClock.elapsedRealtime());
        chrono.start();
        addRecording();
    }

//    public void (View view) {
//
//    }

    public void stopRecording(View view) {

        stopRecording();
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Title")
                .setMessage("Do You Want To Save Audio ?")
                .setPositiveButton("YES", null)
                .setNegativeButton("NO", null)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recentAudio = myDir.listFiles();
                audioAdaptor = new AudioAdaptor(files, colors, MainActivity.this);
                recyclerView.setAdapter(audioAdaptor);
                Toast.makeText(MainActivity.this, "Audio Saved Successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Audio Deleted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });
    }
}