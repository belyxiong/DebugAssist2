package com.bely.debugassist;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainService extends Service {

    private static final String TAG = "MainService";

    LinearLayout toucherLayout;
    TextView txt_timer;
    WindowManager.LayoutParams params;
    WindowManager windowManager;

    ImageButton imageButton1;
    private static final int MSG_UPDATE_TIMER = 1001;

    private long timeinms = 0;
    int statusBarHeight = -1;

    Handler msgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_UPDATE_TIMER) {
                updateTime();
                sendEmptyMessageDelayed(MSG_UPDATE_TIMER, Config.interval);
            }
        }
    };

    private void updateTime() {
        Date now = new Date(); 
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSZ");

        String time = dateFormat.format(now);
        if (txt_timer != null) {
            txt_timer.setText(time);
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.i(TAG,"MainService Created");
        createToucher();
        msgHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER, Config.interval);
    }

    private void createToucher()
    {

        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);

        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        params.format = PixelFormat.RGBA_8888;

        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;


        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 900;
        params.y = 630;


        params.width = 320;
        params.height = 70;

        LayoutInflater inflater = LayoutInflater.from(getApplication());

        toucherLayout = (LinearLayout) inflater.inflate(R.layout.activity_main,null);

        windowManager.addView(toucherLayout,params);



        toucherLayout.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);


        int resourceId = getResources().getIdentifier("status_bar_height","dimen","android");
        if (resourceId > 0)
        {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        txt_timer = (TextView) toucherLayout.findViewById(R.id.txt_timer);
        imageButton1 = (ImageButton) toucherLayout.findViewById(R.id.imageButton1);

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.uptimeMillis() - timeinms >= 1500)
                {
                    Toast.makeText(MainService.this,"click again to close",Toast.LENGTH_SHORT).show();
                }else
                {
                    stopSelf();
                }
                timeinms = SystemClock.uptimeMillis();
            }
        });

        imageButton1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                params.x = (int) event.getRawX();
                params.y = (int) event.getRawY();
                windowManager.updateViewLayout(toucherLayout,params);
                return false;
            }
        });
    }

    @Override
    public void onDestroy()
    {
        if (imageButton1 != null)
        {
            windowManager.removeView(toucherLayout);
        }
        super.onDestroy();
    }
}
