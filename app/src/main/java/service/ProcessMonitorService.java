package service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.widget.Toast;

import hxkong.msd.MSDService;

public class ProcessMonitorService extends Service {


	private Context mContext;
	private Handler handler = new Handler();

	//
	AlarmManager manager;
	PendingIntent pi;
	//
	/* app 更改和安装消息 */
	private final BroadcastReceiver apkInstallListener = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
				Toast.makeText(context,"应用被安装", Toast.LENGTH_LONG).show();
			} else if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
				System.out.println("*****应用被删除");
			}else if (Intent.ACTION_PACKAGE_CHANGED.equals(intent.getAction())) {
			}else if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
				System.out.println("****应用被替换");
			}else if (Intent.ACTION_PACKAGE_RESTARTED.equals(intent.getAction())) {
			}
		}
	};

	// 注册监听
	@SuppressWarnings("deprecation")
	private void registerApkInstallListener() {
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
		intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
		intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
		intentFilter.addDataScheme("package");
		registerReceiver(apkInstallListener, intentFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(apkInstallListener) ;
		manager.cancel(pi);
	}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*进程被移除*/
	@Override
	public void onTaskRemoved(Intent rootIntent) {
		super.onTaskRemoved(rootIntent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		//1秒激活一次线程
		long triggerAtTime = SystemClock.elapsedRealtime()+1000;
		Intent i = new Intent(mContext,AlarmReceiver.class);
		pi = PendingIntent.getBroadcast(this,0,i,0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = ProcessMonitorService.this;
		Toast.makeText(mContext,"后台服务启动 ProcessMonitorService", Toast.LENGTH_SHORT).show();
		handler.post(mRunnable);
		registerApkInstallListener() ;
	}

	//后台线程
	Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			//延时
			handler.postDelayed(mRunnable, 1000 * 5);
			//
			Toast.makeText(mContext,"我是后台", Toast.LENGTH_SHORT).show();
		}
	};

}
