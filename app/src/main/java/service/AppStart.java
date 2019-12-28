package service;

import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.util.Log;
import android.zh.xmappad.MainActivity;

public class AppStart extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mBootIntent = new Intent(context, MainActivity.class);
        mBootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mBootIntent);

    }
}
