package com.example.chronoapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TimePicker;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private PausableChronometer chronometer;
    private Chronometer chronometerDurationView;
    private int duration = 60; // en seconde

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeChronometer();
        initializeButtons();
    }

    protected void onPause()
    {
        super.onPause();
        chronometer.stop();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose a color");
        menu.add(0, v.getId(), 0, "Yellow");
        menu.add(0, v.getId(), 0, "Gray");
        menu.add(0, v.getId(), 0, "Cyan");
    }

    private void initializeChronometer()
    {
        chronometer = findViewById(R.id.Chronometer);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer c) {
                Switch switchLoop = findViewById(R.id.SwitchLoop);
                if (chronometer.getTime() >= duration * 1000) // conversion en ms
                {
                    ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);
                    if (switchLoop.isChecked())
                    {
                        chronometer.reset();
                        chronometer.start();
                    }
                    else
                    {
                        chronometer.stop();
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initializeButtons()
    {
        Button buttonStart = findViewById(R.id.ButtonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (chronometer.getTime() >= duration)
                {
                    //chronometer.reset();
                }
                chronometer.start();
            }
        });

        Button buttonStop = findViewById(R.id.ButtonStop);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                chronometer.stop();
            }
        });

        Button buttonReset = findViewById(R.id.ButtonReset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                chronometer.reset();
            }
        });

        chronometerDurationView = findViewById(R.id.ChronometerForDuration);
        chronometerDurationView.setBase(SystemClock.elapsedRealtime() - duration * 1000);
        LayoutInflater inflater = getLayoutInflater();
        View layout = View.inflate(getApplicationContext(), R.layout.chronometer_time_picker, null);
        NumberPicker minute = layout.findViewById(R.id.NumberPickerMinute);
        NumberPicker second = layout.findViewById(R.id.NumberPickerSecond);
        minute.setMaxValue(59);
        second.setMaxValue(59);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Durée")
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which)
                    {
                        duration = 60 * minute.getValue() + second.getValue();
                        chronometerDurationView.setBase(SystemClock.elapsedRealtime() - duration * 1000);
                    }
                });
        builder.setView(layout);
        AlertDialog dialog = builder.create();

        Button buttonDuration = findViewById(R.id.ButtonDuration);
        buttonDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                chronometerDurationView.setBase(SystemClock.elapsedRealtime() - duration * 1000);
                dialog.show();
            }
        });
    }
}
