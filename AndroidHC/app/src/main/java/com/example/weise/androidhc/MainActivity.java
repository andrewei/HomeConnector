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

import com.example.weise.androidhc.R;
import com.example.weise.androidhc.network.Network;
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
        initSeekbar();
        initEditText();
        connectNetwork();
    }

    private void connectNetwork(){
        if(preferences.contains("ipServer")) {
            String ip = preferences.getString("ipServer", "xxx.xxx.xxx.xxx");
            editTextIpAddress.setText(ip);
            networkController = new NetworkController(ip);
        }
    }

    private void initEditText() {
        editTextIpAddress = (EditText) findViewById(R.id.editText_enter_ip);
        editTextIpAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE){
                    //deselect editText and hide keyboard and get ip value
                    Toast.makeText(getApplicationContext(),"Ip changed",Toast.LENGTH_SHORT).show();
                    View view = findViewById(R.id.mainLayout);
                    view.requestFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    String ip = (String) textView.getText().toString();
                    editor.putString("ipServer", ip);
                    editor.apply();
                    connectNetwork();
                }
                return false;
            }
        });
    }

    private void initSeekbar(){
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double volume = (double) seekBar.getProgress() / seekBar.getMax();
                double transformedVolume = Math.pow(volume, 2.5);
                networkController.setVolume(transformedVolume);
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
        networkController.playSong();
        Toast.makeText(this, "Play Song", Toast.LENGTH_SHORT).show();
    }

    public void stop(View view) {
        networkController.stopSong();
        Toast.makeText(this, "Stop Song", Toast.LENGTH_SHORT).show();
    }

    public void next(View view) {
        networkController.nextSong();
        Toast.makeText(this, "Next Song", Toast.LENGTH_SHORT).show();
    }
}
