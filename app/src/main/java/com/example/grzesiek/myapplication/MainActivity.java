package com.example.grzesiek.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    public final String TAG = "Main";

    private SeekBar elevation;
    private TextView debug;
    private TextView status;
    private Bluetooth bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //debug = (TextView) findViewById(R.id.textDebug);
        status = (TextView) findViewById(R.id.textStatus);

        findViewById(R.id.Connect).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connectService();
            }
        });
        /**
        //elevation = (SeekBar) findViewById(R.id.seekBar);
        elevation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("Seekbar","onStopTrackingTouch ");
                int progress = seekBar.getProgress();
                String p = String.valueOf(progress);
                debug.setText(p);
                bt.sendMessage(p);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("Seekbar","onStartTrackingTouch ");
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Log.d("Seekbar", "onProgressChanged " + progress);
            }
        });*/

        bt = new Bluetooth(this, mHandler);
        connectService();
    }

    public static final String CMDTOGGLEPAUSE = "togglepause";
    public static final String CMDPAUSE = "pause";
    public static final String CMDPREVIOUS = "previous";
    public static final String CMDNEXT = "next";
    public static final String SERVICECMD = "com.android.music.musicservicecommand";
    public static final String CMDNAME = "command";
    public static final String CMDSTOP = "stop";
    public static final String CMDPLAY = "play";

    public void volUp(View view){
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume +1, 5);
    }

    public void volDown(View view){
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume -1, 5);
    }

    public void stopM(View view){
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if(mAudioManager.isMusicActive()) {
            Intent i = new Intent(SERVICECMD);
            i.putExtra(CMDNAME , CMDSTOP );
            MainActivity.this.sendBroadcast(i);
        }
    }

    public void playM(View view) {
        Intent i = new Intent(SERVICECMD);
        i.putExtra(CMDNAME, CMDPLAY);
        MainActivity.this.sendBroadcast(i);

    }

    public void pauseM(View view) {
        Intent i = new Intent(SERVICECMD);
        i.putExtra(CMDNAME, CMDPAUSE);
        MainActivity.this.sendBroadcast(i);
    }

    public void nextM(View view) {

        Intent i = new Intent(SERVICECMD);
        i.putExtra(CMDNAME, CMDNEXT);
        MainActivity.this.sendBroadcast(i);
    }


    public void connectService(){  ///łaczenie, rekacja na przycisk
        try {
            status.setText("Connecting...");
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                bt.start();
                bt.connectDevice("HC-06");
                Log.d(TAG, "Btservice started - listening");
                status.setText("Connected");
            } else {
                Log.w(TAG, "Btservice started - bluetooth is not enabled");
                status.setText("Bluetooth Not enabled");
            }
        } catch(Exception e){
            Log.e(TAG, "Unable to start bt ",e);
            status.setText("Unable to connect " +e);
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Bluetooth.MESSAGE_STATE_CHANGE:
                    Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    break;
                case Bluetooth.MESSAGE_WRITE:
                    Log.d(TAG, "MESSAGE_WRITE ");
                    break;
                case Bluetooth.MESSAGE_READ:
                    Log.d(TAG, "MESSAGE_READ ");
                    break;
                case Bluetooth.MESSAGE_DEVICE_NAME:
                    Log.d(TAG, "MESSAGE_DEVICE_NAME "+msg);
                    break;
                case Bluetooth.MESSAGE_TOAST:
                    Log.d(TAG, "MESSAGE_TOAST "+msg);
                    break;
            }
        }
    };





}


