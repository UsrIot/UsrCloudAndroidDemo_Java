package cn.usr.usrcloudmqttsdkdemo.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * Created by shizhiyuan on 2017/7/19.
 */

public class UsrApplication extends Application {

    private UsrCrashHandler usrCrashHandler;
    private static UsrApplication instance;
    private static Toast toast;
    public   static  String USERNAME="";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //initUsrCrashHandler();
    }


    //Application 的回掉方法
    public static final UsrApplication getInstance() {
        return instance;
    }

    /**
     * 初始化异常处理类
     */
    private void initUsrCrashHandler() {
        usrCrashHandler = UsrCrashHandler.getmusrCrashHandler();
        if (usrCrashHandler != null) {
            usrCrashHandler.initCrashHandler(this);
        }
    }

    @Override
    public void onTerminate() {
        //程序终止的时候执行
        Log.d(TAG, "EpcApplication-----------onTerminate程序终止的时候执行");
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        //低内存的时候执行
        Log.d(TAG, "EpcApplication-----------onLowMemory低内存的时候执行");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        Log.d(TAG, "EpcApplication-----------onTrimMemory 程序在内存清理的时候执行");
        super.onTrimMemory(level);
    }

    public static void showToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}
