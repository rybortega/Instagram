package com.example.instagram;

import android.os.Handler;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public abstract class DoubleClickListener implements View.OnClickListener {

    private Timer timer = null;  //at class level;
    private int DELAY   = 200;

    private static final long DOUBLE_CLICK_TIME_DELTA = 200;//milliseconds

    long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
            processDoubleClickEvent(v);
        } else {
            processSingleClickEvent(v);
        }
        lastClickTime = clickTime;
    }

    public void processSingleClickEvent(final View v){

        final Handler handler=new Handler();
        final Runnable mRunnable=new Runnable(){
            public void run(){
                onSingleClick(v);
            }
        };

        TimerTask timertask=new TimerTask(){
            @Override
            public void run(){
                handler.post(mRunnable);
            }
        };
        timer=new Timer();
        timer.schedule(timertask,DELAY);

    }

    public void processDoubleClickEvent(View v){
        if(timer!=null)
        {
            timer.cancel();
            timer.purge();
        }
        onDoubleClick(v);
    }

    public abstract void onSingleClick(View v);

    public abstract void onDoubleClick(View v);
}