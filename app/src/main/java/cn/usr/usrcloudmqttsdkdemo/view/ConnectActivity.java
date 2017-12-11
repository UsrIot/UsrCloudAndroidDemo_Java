package cn.usr.usrcloudmqttsdkdemo.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.usr.usrcloudmqttsdkdemo.R;
import cn.usr.usrcloudmqttsdkdemo.base.UsrBaseActivity;
import cn.usr.usrcloudmqttsdkdemo.business.UsrCloudClientService;

import static cn.usr.usrcloudmqttsdkdemo.base.UsrApplication.USERNAME;
/**
 * Created by shizhiyuan on 2017/7/21.
 */

public class ConnectActivity extends UsrBaseActivity {


    private Button con_btn_connect;
    private EditText con_et_uname;
    private EditText con_et_upw;
    private Receiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
        setListener();
        receiver = new Receiver();
        //新添代码，在代码中注册广播接收程序
        IntentFilter filter = new IntentFilter();
        filter.addAction("onConnectAck");
        registerReceiver(receiver, filter);
    }

    @Override
    public void initView() {
        super.initView();
        con_btn_connect = (Button) findViewById(R.id.con_btn_connect);
        con_et_uname = (EditText) findViewById(R.id.con_et_uname);
        con_et_upw = (EditText) findViewById(R.id.con_et_upw);
    }

    @Override
    public void setListener() {
        super.setListener();
        con_btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = con_et_uname.getText().toString();
                String upw = con_et_upw.getText().toString();
                if (uname.equals("") | upw.equals("")) {
                    showToast(ConnectActivity.this.getString(R.string.alertinfo));
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("uname", uname);
                    bundle.putString("upw", upw);
                    USERNAME=uname;
                    startServiceWithParm(UsrCloudClientService.class, bundle);
                }
            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public class Receiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra("onConnectAckreturnCode", 1) == 0) {
                startActivity(new Intent(ConnectActivity.this, MainActivity.class));
            } else if (intent.getIntExtra("onConnectAckreturnCode", 1) == 1) {
                Toast.makeText(ConnectActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
