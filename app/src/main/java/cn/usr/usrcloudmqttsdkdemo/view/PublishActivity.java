package cn.usr.usrcloudmqttsdkdemo.view;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import cn.usr.usrcloudmqttsdkdemo.R;
import cn.usr.usrcloudmqttsdkdemo.base.UsrBaseActivity;
import cn.usr.usrcloudmqttsdkdemo.business.UsrCloudClientService;

import static cn.usr.usrcloudmqttsdkdemo.base.UsrApplication.USERNAME;


/**
 * Created by shizhiyuan on 2017/7/21.
 */

public class PublishActivity extends UsrBaseActivity {
    private EditText pub_et_data;
    private EditText pub_et_modemsg;
    private Button pub_btn_pubdata;
    private Spinner pub_sp_mode;
    private TextView pub_tv_showmsg;
    private TextView pub_tv_cleanmsg;
    private static String mode;
    private UsrCloudClientService myService;
    private OnPublishReceiver onPublishReceiver;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setListener();
        final Intent intent = new Intent(this, UsrCloudClientService.class);
        bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
        onPublishReceiver = new OnPublishReceiver();
        //新添代码，在代码中注册广播接收程序
        IntentFilter filter = new IntentFilter();
        filter.addAction("onPublishDataAck");
        filter.addAction("onPublishDataResult");
        registerReceiver(onPublishReceiver, filter);


    }

    @Override
    public void initView() {
        super.initView();
        pub_et_data = (EditText) findViewById(R.id.pub_et_data);
        pub_et_modemsg = (EditText) findViewById(R.id.pub_et_modemsg);
        pub_btn_pubdata = (Button) findViewById(R.id.pub_btn_pubdata);
        pub_sp_mode = (Spinner) findViewById(R.id.pub_sp_mode);
        pub_tv_showmsg = (TextView) findViewById(R.id.pub_tv_showmsg);
        pub_tv_cleanmsg = (TextView) findViewById(R.id.pub_tv_cleanmsg);
    }

    @Override
    public void setListener() {
        super.setListener();

        pub_btn_pubdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String modemsg = pub_et_modemsg.getText().toString();
                String data = pub_et_data.getText().toString();
                if (mode.equals("设备")) {
                    if (!modemsg.equals("") && !data.equals("")) {
                        myService.publishForDevId(modemsg, data.getBytes());
                    } else {
                        showToast(PublishActivity.this.getString(R.string.alertinfo));
                    }
                } else {
                    myService.publishForuName(data.getBytes());
                }

            }
        });
        pub_sp_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mode = pub_sp_mode.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        pub_tv_cleanmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pub_tv_showmsg != null) {
                    pub_tv_showmsg.setText("");
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unbindService(serviceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class OnPublishReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("onPublishDataAck")) {
                Bundle bundle = intent.getExtras();
                int messageId = bundle.getInt("messageId");
                boolean isSuccess = bundle.getBoolean("isSuccess");
                pub_tv_showmsg.append("发布回调结果：\n" + "MessageID为：" + messageId + "\n发布结果：" + isSuccess + "\n----------------------------------->\n");
            } else if (action.equals("onPublishDataResult")) {
                Bundle bundle = intent.getExtras();
                pub_tv_showmsg.append("本次推送结果回调：\n" + "MessageID为：" + bundle.getInt("messageId") + "\ntopic为：" + bundle.getString("topic")
                        + "\n------------------------------->\n");
            }
        }
    }
}
