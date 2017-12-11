package cn.usr.usrcloudmqttsdkdemo.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * usr 异常处理类
 *
 *
 * Created by shizhiyuan on 2017/7/19.
 */

public class UsrCrashHandler implements Thread.UncaughtExceptionHandler {
    //上下文
    private Context mContext;
    public static final String TAG = "CrashHandler";
    private static UsrCrashHandler mCrashHandler = null;

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //存储 设备信息和异常信息
    private Map<String, String> infos = new HashMap<>();
    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    //提示用户的话语
    private static final String ALERT = "很抱歉,程序出现异常,即将退出";

    //私有化构造函数
    private UsrCrashHandler() {

    }

    /**
     * 单例模式
     *
     * @return
     */
    public static UsrCrashHandler getmusrCrashHandler() {
        if (mCrashHandler == null) {
            synchronized (UsrCrashHandler.class) {
                if (mCrashHandler == null) {
                    mCrashHandler = new UsrCrashHandler();
                }
            }
        }
        return mCrashHandler;
    }


    /**
     * 初始化 默认异常处理器
     *
     * @param context
     */
    public void initCrashHandler(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * Method invoked when the given thread terminates due to the
     * given uncaught exception.
     * <p>Any exception thrown by this method will be ignored by the
     * Java Virtual Machine.
     *
     * @param t the thread
     * @param e the exception
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!handleException(e)) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            if (mDefaultHandler != null && mDefaultHandler != null) {
                mDefaultHandler.uncaughtException(t, e);
            }
        } else {
            //已经人为处理
            //休眠3 秒钟后强行自动退出
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            Process.killProcess(Process.myPid());
            System.exit(1);
        }

    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param e
     * @return true 为已经处理  false 没有处理
     */
    private boolean handleException(Throwable e) {
        if (e == null) {
            return false;
        }

        //toast提示
        new Thread() {
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                Toast.makeText(mContext, ALERT, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        //收集错误信息
        collectErrorInfo();
        //保存错误信息
        saveErrorInfo(e);

        return true;
    }

    /**
     * 收集设备参数 和 错误信息
     */
    private void collectErrorInfo() {
        try {
            //1.判断当前的包名是不是我当前应用的包名
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = TextUtils.isEmpty(pi.versionName) ? "未设置版本名称" : pi.versionName;
                String versionCode = pi.versionCode + "";
                String versionModel = Build.MODEL;
                String versionSDK = Build.VERSION.SDK;
                String versionsystem = Build.VERSION.RELEASE;
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
                infos.put("versionModel", versionModel);
                infos.put("versionSDK", versionSDK);
                infos.put("versionsystem", versionsystem);
            }
            //利用反射
            Field[] fields = Build.class.getFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    field.setAccessible(true);//AccessibleTest类中的成员变量为private,故必须进行此操作 设置为可以访问
                    infos.put(field.getName(), field.get(null).toString());
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "an error occured when collect crash info", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e(TAG, "an error occured when collect crash info", e);
        }

    }

    /**
     * 保存日志文件
     *
     * @param e
     */
    private String saveErrorInfo(Throwable e) {
        //取出map里的信息  并 以文件的形式储存到SD卡
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String keyName = entry.getKey();
            String value = entry.getValue();
            stringBuilder.append(keyName + "=" + value + "\n");
        }

        //写入到sd卡中
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = e.getCause();
        }
        printWriter.close();

        //将输出结果也添加到字符串里
        String result = writer.toString();
        stringBuilder.append(result);

        long curTime = System.currentTimeMillis();
        String time = formatter.format(new Date());
        String fileName = "crash-" + time + "-" + curTime + ".log";

        //判断有没有sd卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //如果有
            String path = "/sdcard/crash/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(path + fileName);
                fos.write(stringBuilder.toString().getBytes());
                return fileName;
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            Log.e(TAG, "未安装SD卡");
        }
        return null;
    }
}
