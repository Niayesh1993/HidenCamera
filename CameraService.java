package com.example.ezzati.hidencam;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class CameraService extends Service {

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getBaseContext(), "In CameraService", Toast.LENGTH_SHORT).show();
        CameraManager mgr = CameraManager.getInstance(CameraService.this);
        mgr.takePhoto();
        return START_NOT_STICKY;

    }
}
