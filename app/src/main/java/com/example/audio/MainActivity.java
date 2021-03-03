package com.example.audio;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
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
    //    private File[] recentAudio;
    private int[] colors = new int[]{R.color.white_green, R.color.sea_green, R.color.off_white,
            R.color.brown, R.color.light_green, R.color.peach};
    //    private Button mButtonPause;
//    private Button play_button;
    int count = 0;
    //    AudioInterface audioInterface;
    Chronometer chrono;
    //    RecyclerView recyclerView;
//    SeekBar seekBar;
//    AudioAdaptor audioAdaptor;
    File file2;
    File myDir;
    //    ArrayList<File> files = new ArrayList<>();
//    TextView recording;
    MediaRecorder mediaRecorder;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();


        String status = Environment.getExternalStorageState();
        if (status.equals("mounted")) {
            myDir = new File(Environment.getExternalStorageDirectory() + "/" + "MyAudio/");
            if (!myDir.exists())
                myDir.mkdirs();
            String fname3 = getString(R.string.app_name) + "_" + DateFormat.format("MM-dd-yy hh-mm-ss", new Date().getTime()) + ".mp3";
            file2 = new File(myDir, fname3);
        }

        if(mediaRecorder ==  null) {
            initPlayer();
        }
//
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recentAudio = new File[myDir.listFiles().length];
//        recentAudio = myDir.listFiles();
//        Log.d("mainDir", String.valueOf(myDir.listFiles().length));
//
//        if (recentAudio.length != 0) {
//            for(int i=0; i < recentAudio.length; i++){
//                files.add(recentAudio[i]);
//            }
//            audioAdaptor = new AudioAdaptor(files, colors, MainActivity.this);
//            recyclerView.setAdapter(audioAdaptor);
//        }

    }


    private void initPlayer() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(file2.getAbsolutePath());

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initView() {
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        // recyclerView = findViewById(R.id.audio_recyclerview);
//        mButtonPause = findViewById(R.id.pause_button);
//        play_button = findViewById(R.id.play_button);
        chrono = (Chronometer) findViewById(R.id.text_view_countdown);

    }

    private void addRecording() {

//        mediaRecorder.release();
//        mediaRecorder = null;
        try {
            // mediaRecorder.setMaxDuration(10000);
               if(mediaRecorder != null) {
                   mediaRecorder.prepare();
                   mediaRecorder.start();
               }else {
                   initPlayer();

                   mediaRecorder.prepare();
                   mediaRecorder.start();
               }
            }


        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addRecording(View view) {
        chrono.setBase(SystemClock.elapsedRealtime());
        chrono.start();
        addRecording();
    }

    private void stopRecording() {

        try {
            ((Chronometer) findViewById(R.id.text_view_countdown)).stop();
            mediaRecorder.stop();

            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecording(View view) {

        stopRecording();
//        final AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle("Title")
//                .setMessage("Do You Want To Save Audio ?")
//                .setPositiveButton("YES", null)
//                .setNegativeButton("NO", null)
//                .show();
//
//        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//        positiveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                recentAudio = myDir.listFiles();
////                audioAdaptor = new AudioAdaptor(files, colors, MainActivity.this);
////                recyclerView.setAdapter(audioAdaptor);
//                Toast.makeText(MainActivity.this, "Audio Saved Successfully", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });
//        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//        negativeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(MainActivity.this, "Audio Deleted", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//
//            }
//        });
    }

    public void playAudio(View view) throws IOException {
        MediaPlayer mp = new MediaPlayer();
        mp.setDataSource(String.valueOf(file2));
        if (mp != null) {
            mp.prepare();
            mp.start();
        } else {
            Toast.makeText(this, "No Audio to Play..!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAudio(View view) {
        Uri uri = Uri.parse(file2.toString());
        File audioFiles = new File(uri.getPath());
        audioFiles.delete();
        chrono.setBase(SystemClock.elapsedRealtime());
        Toast.makeText(this, "Audio Deleted!", Toast.LENGTH_SHORT).show();
        // this.getContentResolver().delete(uri, null, null);


    }

    public void uploadAudio(View view) {
        Uri uri = Uri.parse(file2.toString());
        Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
//        Intent share = new Intent(Intent.ACTION_SEND);
//        share.setType("audio/*");
//        share.putExtra(Intent.EXTRA_STREAM, uri);
//        this.startActivity(Intent.createChooser(share, "Share Sound File"));
//
    }
}
