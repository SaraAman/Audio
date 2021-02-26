package com.example.audio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;


public class AudioAdaptor extends RecyclerView.Adapter<AudioAdaptor.ViewHolder> {

    private ArrayList<File> recentAudio;
    MediaPlayer mediaPlayer = new MediaPlayer();
    private int[] rowColors;
    private AudioInterface audioInterface;
    MainActivity mainActivity;
    private Context context;
    private double timeElapsed = 0, finalTime = 0;

    public AudioAdaptor(ArrayList<File> recentAudio, int[] colors, MainActivity mainActivity) {
        this.recentAudio = recentAudio;
        this.rowColors = colors;
        this.context = mainActivity;
        this.mainActivity = mainActivity;
        audioInterface = (AudioInterface) mainActivity;
    }

    // Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_recycler_view_layout, parent, false);
        return new ViewHolder(view, audioInterface);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemView.setBackgroundColor(context.getResources().getColor(rowColors[position]));
        holder.filename.setText(recentAudio.get(position).lastModified() + "");

    }


    @Override
    public int getItemCount() {
        return recentAudio.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        Button pause;
        Button play;
        Button share;
        Button delete;
        TextView filename;
        SeekBar seekBar;

        public ViewHolder(@NonNull View itemView, AudioInterface audioInterface) {
            super(itemView);
            pause = itemView.findViewById(R.id.pause_button);
            play = itemView.findViewById(R.id.play_button);
            share = itemView.findViewById(R.id.share_audio);
            delete = itemView.findViewById(R.id.delete_audio);
            filename = itemView.findViewById(R.id.audio_name);

            play.setVisibility(View.VISIBLE);
            seekBar = itemView.findViewById(R.id.seekbar);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekBar.setProgress(progress);
//                    if(mediaPlayer != null && fromUser){
//                        mediaPlayer.seekTo(progress * 1000);
//                    }


//                    if(mediaPlayer != null){
//                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;

//                    }


//
//                    timeElapsed = mediaPlayer.getCurrentPosition();
//                    seekBar.setProgress((int) timeElapsed);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareRecording(recentAudio.get(getAdapterPosition()));
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recentAudio.get(getAdapterPosition()).exists()){
                        if(recentAudio.get(getAdapterPosition()).delete()){
                            recentAudio.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyDataSetChanged();
                        }
                    }


//                    list.remove(position);
//                    recycler.removeViewAt(position);
//                    mAdapter.notifyItemRemoved(position);
//                    mAdapter.notifyItemRangeChanged(position, list.size());
//


                }
            });
            play.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    play.setVisibility(View.INVISIBLE);
                    pause.setVisibility(View.VISIBLE);

                    playRecording(recentAudio.get(getAdapterPosition()));
                    seekBar.setMax(mediaPlayer.getDuration() / 1000);

                    final int duration = mediaPlayer.getDuration();
                    final int amoungToupdate = duration / 1000;
                    Timer mTimer = new Timer();
                    mTimer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            mainActivity.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    int duration = mediaPlayer.getDuration() / 1000;
                                    seekBar.setProgress(duration);
                                    Log.d("Duration: ", String.valueOf(duration));
//                                    if (!(amoungToupdate * seekBar.getProgress() >= duration)) {
//                                        int p = seekBar.getProgress();
//                                        p += 1;
//                                        seekBar.setProgress(p);
//                                    }
                                }
                            });
                        }

                        ;
                    }, 1000);


//                     Handler mHandler = new Handler();
////Make sure you update Seekbar on UI thread
//                    mainActivity.runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            if(mediaPlayer != null){
//                                int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
//                                seekBar.setProgress(mCurrentPosition);
//                            }
//                            mHandler.postDelayed(this, 1000);
//                        }
//                    });

                }
            });
            pause.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    pause.setVisibility(View.INVISIBLE);
                    play.setVisibility(View.VISIBLE);
                    stopPalying();
                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    private void stopPalying() {

        try {

            if(mediaPlayer!= null && mediaPlayer.isPlaying()){
                mediaPlayer.pause();
               // mediaPlayer.stop();
                mediaPlayer = null;
            }else{
                mediaPlayer = null;
            }




//            mediaPlayer.stop();
//            mediaPlayer.release();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void playRecording(File file) {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();}
        else{

        try {

            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     }


    private void shareRecording(File file) {
       // String sharePath =  Environment.getExternalStorageDirectory() + "/" + "MyAudio/";
        Uri uri = Uri.parse(file.toString());
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("audio/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        mainActivity.startActivity(Intent.createChooser(share, "Share Sound File"));
    }
    private void deleteRecording(File file) {


    }

}