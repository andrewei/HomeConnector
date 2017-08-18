package com.example.weise.androidhc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.weise.androidhc.R;
import com.example.weise.androidhc.network.Network;
import com.example.weise.androidhc.network.NetworkController;

public class MainActivity extends AppCompatActivity {

    NetworkController networkController;
    private SeekBar seekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkController = new NetworkController();

        initSeekbar();
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
