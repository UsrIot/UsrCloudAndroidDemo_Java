package cn.usr.usrcloudmqttsdkdemo.view;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.view.View;
import android.widget.Button;

import org.eclipse.paho.client.mqttv3.MqttException;

import cn.usr.usrcloudmqttsdkdemo.R;
import cn.usr.usrcloudmqttsdkdemo.base.UsrBaseActivity;
import cn.usr.usrcloudmqttsdkdemo.business.UsrCloudClientService;


public class MainActivity extends UsrBaseActivity implements View.OnClickListener {
    private Button main_btn_publish;
    private Button main_btn_subscribe;
    private Button main_btn_disconnent;
    private Button main_btn_subscribe_parse;
    private Button main_btn_publish_parse;

    private UsrCloudClientService myService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = ((UsrCloudClientService.MyBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        setListener();
        final Intent intent = new Intent(this, UsrCloudClientService.class);
        bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void initView() {
        super.initView();
        main_btn_publish = (Button) findViewById(R.id.main_btn_publish);
        main_btn_subscribe = (Button) findViewById(R.id.main_btn_subscribe);
        main_btn_disconnent = (Button) findViewById(R.id.main_btn_disconnent);
        main_btn_subscribe_parse = (Button) findViewById(R.id.main_btn_subscribe_parse);
        main_btn_publish_parse = (Button) findViewById(R.id.main_btn_publish_parse);
    }

    @Override
    public void setListener() {
        super.setListener();
        main_btn_publish.setOnClickListener(this);
        main_btn_subscribe.setOnClickListener(this);
        main_btn_disconnent.setOnClickListener(this);
        main_btn_publish_parse.setOnClickListener(this);
        main_btn_subscribe_parse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_subscribe:
                startActivity(SubscribeActivity.class);
                break;
            case R.id.main_btn_publish:
                startActivity(PublishActivity.class);
                break;
            case R.id.main_btn_subscribe_parse:
                startActivity(SubscribeParseActivity.class);
                break;
            case R.id.main_btn_publish_parse:
                startActivity(PublishParseActivity.class);
                break;
            case R.id.main_btn_disconnent:
                try {
                    if (myService.DisConnectUnCheck()) {
                        Intent intent = new Intent(this, UsrCloudClientService.class);
                        stopService(intent);
                        Process.killProcess(Process.myPid());
                        finish();
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (myService.doDisConnect()) {
            Intent intent = new Intent(this, UsrCloudClientService.class);
            stopService(intent);
            Process.killProcess(Process.myPid());
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.unbindService(serviceConnection);
    }
}
