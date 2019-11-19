package com.example.weise.androidhc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weise.androidhc.network.NetworkController;

public class MainActivity extends AppCompatActivity {

    NetworkController networkController;
    private SeekBar seekBar;
    private EditText editTextIpAddress;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        editTextIpAddress = (EditText) findViewById(R.id.editText_enter_ip);
        initSeekbar();
    }

    private void connectNetwork(){
        if(preferences.contains("ipServer")) {
            String ip = preferences.getString("ipServer", "");
            editTextIpAddress.setText(ip);
            networkController = new NetworkController(ip);
        }
    }

    private void initSeekbar(){
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double volume = (double) seekBar.getProgress() / seekBar.getMax();
                double transformedVolume = Math.pow(volume, 2.5);
                if(networkController != null) {
                    networkController.setVolume(transformedVolume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void play(View view) {
        if(networkController != null) {
            networkController.playSong();
            Toast.makeText(this, "Play Song", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(
                    this, "NetworkController not initialised",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    public void stop(View view) {
        if(networkController != null) {
            networkController.stopSong();
            Toast.makeText(this, "Stop Song", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(
                    this, "NetworkController not initialised",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    public void next(View view) {
        if(networkController != null) {
            networkController.nextSong();
            Toast.makeText(this, "Next Song", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(
                    this, "NetworkController not initialised",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    public void connect(View view) {
        String ip = editTextIpAddress.getText().toString();
        editor.putString("ipServer", ip);
        editor.apply();
        connectNetwork();
    }
}
