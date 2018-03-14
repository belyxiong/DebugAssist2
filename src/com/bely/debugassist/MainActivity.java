package com.bely.debugassist;

import android.app.AppOpsManager;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import android.os.UserHandle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent startIntent = getIntent();

        if (startIntent != null) {
            int interval = startIntent.getIntExtra(Config.KEY_INTERVAL, Config.interval);
            Config.interval = interval;
        }
        AppOpsManager appOps = this.getSystemService(AppOpsManager.class);
        PackageManager mPackageManager = this.getPackageManager();

        int uid = 0;
        try{
            uid = mPackageManager.getPackageUid("com.bely.debugassist", UserHandle.USER_ALL);
        }catch(PackageManager.NameNotFoundException e) {
            
        }

        appOps.setMode(AppOpsManager.OP_SYSTEM_ALERT_WINDOW,
                uid, "com.bely.debugassist", AppOpsManager.MODE_ALLOWED);

        if (Settings.canDrawOverlays(MainActivity.this))
        {
            Intent intent = new Intent(MainActivity.this,MainService.class);
            Toast.makeText(MainActivity.this,"DebugAssist enabled.",Toast.LENGTH_SHORT).show();
            startService(intent);
            finish();
        }else
        {
            Toast.makeText(MainActivity.this,"Some error happens, cannot start.",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
