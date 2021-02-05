package com.example.chronoapp;

import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.Chronometer;

import androidx.annotation.RequiresApi;

public class PausableChronometer extends Chronometer
{

    private long elapsedTimeAtStop = 0;

    public PausableChronometer(Context context)
    {
        super(context);
    }

    public PausableChronometer(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public PausableChronometer(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PausableChronometer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void start()
    {
        setBase(SystemClock.elapsedRealtime() - elapsedTimeAtStop);
        super.start(); // la méthode start() du chrono inclut le temps en mode sleep !
    }

    @Override
    public void stop()
    {
        super.stop(); // la méthode stop() du chrono n'arrête que l'update du texte
        elapsedTimeAtStop = SystemClock.elapsedRealtime() - getBase();
    }

    public void reset()
    {
        stop();
        setBase(SystemClock.elapsedRealtime());
        elapsedTimeAtStop = 0;
    }

    public long getTime()
    {
        return SystemClock.elapsedRealtime() - getBase();
    }

}
