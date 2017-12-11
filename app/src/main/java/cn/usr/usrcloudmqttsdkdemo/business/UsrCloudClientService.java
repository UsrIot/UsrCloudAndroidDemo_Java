package cn.usr.usrcloudmqttsdkdemo.business;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import cn.usr.UsrCloudMqttCallbackAdapter;

import static android.content.ContentValues.TAG;

/**
 * Created by shizhiyuan on 2017/7/21.
 */

public class UsrCloudClientService extends Service {


    private static String uName = "";
    private static String uPW = "";
    private UsrCloudClient usrCloudClient;
    private UsrCloudClientCallback usrCloudClientCallback;
    private MyBinder mBinder = new MyBinder();

    public UsrCloudClientService getInstance() {
        return UsrCloudClientService.this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        usrCloudClientCallback = new UsrCloudClientCallback();
        usrCloudClient = new UsrCloudClient();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand");
        Bundle bundle = intent.getExtras();
        uName = bundle.getString("uname");
        uPW = bundle.getString("upw");
        doClientConnection(uName, uPW);

        return super.onStartCommand(intent, flags, startId);
    }

    private void doClientConnection(String uname, String upw) {
        if (isConnectIsNomarl()) {
            try {
                usrCloudClient.setUsrCloudMqttCallback(usrCloudClientCallback);
                usrCloudClient.Connect(uname, upw);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void doSubscribeForDevId(String devId) {
        if (isConnectIsNomarl()) {
            try {
                usrCloudClient.setUsrCloudMqttCallback(usrCloudClientCallback);
                usrCloudClient.SubscribeForDevId(devId);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void doSubscribeForUsername() {
        if (isConnectIsNomarl()) {
            try {
                usrCloudClient.setUsrCloudMqttCallback(usrCloudClientCallback);
                usrCloudClient.SubscribeForUsername();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }


    public void doSubscribeParsedByDevId(String devId) {
        if (isConnectIsNomarl()) {
            try {
                usrCloudClient.setUsrCloudMqttCallback(usrCloudClientCallback);
                usrCloudClient.SubscribeParsedByDevId(devId);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void doSubscribeParsedForUsername() {
        if (isConnectIsNomarl()) {
            try {
                usrCloudClient.setUsrCloudMqttCallback(usrCloudClientCallback);
                usrCloudClient.SubscribeParsedForUsername();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void doDisSubscribeforDevId(String devId) {
        if (isConnectIsNomarl()) {
            try {
                usrCloudClient.setUsrCloudMqttCallback(usrCloudClientCallback);
                usrCloudClient.DisSubscribeforDevId(devId);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    public void doDisSubscribeforuName() {
        if (isConnectIsNomarl()) {
            try {
                usrCloudClient.setUsrCloudMqttCallback(usrCloudClientCallback);
                usrCloudClient.DisSubscribeforuName();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    public void doDisSubscribeParsedforDevId(String devId) {
        if (isConnectIsNomarl()) {
            try {
                usrCloudClient.setUsrCloudMqttCallback(usrCloudClientCallback);
                usrCloudClient.DisSubscribeParsedforDevId(devId);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void doDisSubscribeParsedForUsername() {
        if (isConnectIsNomarl()) {
            try {
                usrCloudClient.setUsrCloudMqttCallback(usrCloudClientCallback);
                usrCloudClient.DisSubscribeParsedForUsername();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    public void publishForDevId(String devId, byte[] data) {
        if (isConnectIsNomarl()) {
            try {
                usrCloudClient.setUsrCloudMqttCallback(usrCloudClientCallback);
                usrCloudClient.publishForDevId(devId, data);
            } catch (MqttException e) {
                e.printStackTrace();

            }
        }
    }

    public void publishParsedQueryDataPoint(String devId, String pointId) {
        if (isConnectIsNomarl()) {
            try {
                usrCloudClient.setUsrCloudMqttCallback(usrCloudClientCallback);
                usrCloudClient.publishParsedQueryDataPoint(devId, pointId);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void publishParsedSetDataPoint(String devId, String pointId, String value) {
        if (isConnectIsNomarl()) {
            try {
                usrCloudClient.setUsrCloudMqttCallback(usrCloudClientCallback);
                usrCloudClient.publishParsedSetDataPoint(devId, pointId, value);
            } catch (MqttException e) {
                e.printStackTrace();

            }
        }
    }

    public void publishForuName(byte[] data) {
        if (isConnectIsNomarl()) {
            try {
                usrCloudClient.setUsrCloudMqttCallback(usrCloudClientCallback);
                usrCloudClient.publishForuName(data);
            } catch (MqttException e) {
                e.printStackTrace();

            }
        }
    }

    public boolean doDisConnect() {
        if (isConnectIsNomarl()) {
            return usrCloudClient.DisConnect();
        }
        return false;
    }
    public boolean DisConnectUnCheck() throws MqttException {
        if (isConnectIsNomarl()) {
            return usrCloudClient.DisConnectUnCheck();
        }
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "UsrCloudClientService   is   onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public class MyBinder extends Binder {
        public UsrCloudClientService getService() {
            return UsrCloudClientService.this;
        }
    }

    /**
     * 判断网络是否连接
     */
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "MQTT当前网络名称：" + name);
            return true;
        } else {
            Log.i(TAG, "MQTT 没有可用网络");
            return false;
        }
    }

}
