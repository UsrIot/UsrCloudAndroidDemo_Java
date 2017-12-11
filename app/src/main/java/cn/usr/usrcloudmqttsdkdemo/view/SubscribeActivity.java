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

import static cn.usr.usrcloudmqttsdkdemo.utils.BaseUtils.bytes2hex01;


/**
 * Created by shizhiyuan on 2017/7/21.
 */

public class SubscribeActivity extends UsrBaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private TextView sub_tv_receiverdata;
    private TextView sub_tv_cleanmsg;
    private Button sub_btn_suresub;
    private Spinner sub_sp_submode;
    private EditText sub_et_submsg;


    private static String submode = "";
    private static String submsg;

    private OnSubscribeReceiver onSubscribeReceiver;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setListener();
        final Intent intent = new Intent(this, UsrCloudClientService.class);
        bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);


        onSubscribeReceiver = new OnSubscribeReceiver();
        //新添代码，在代码中注册广播接收程序
        IntentFilter filter = new IntentFilter();
        filter.addAction("onSubscribeAck");
        filter.addAction("onDisSubscribeAck");
        filter.addAction("onReceiveEvent");
        registerReceiver(onSubscribeReceiver, filter);
    }

    @Override
    public void initView() {
        super.initView();
        sub_sp_submode = (Spinner) findViewById(R.id.sub_sp_submode);
        sub_et_submsg = (EditText) findViewById(R.id.sub_et_submsg);
        sub_btn_suresub = (Button) findViewById(R.id.sub_btn_suresub);
        sub_tv_receiverdata = (TextView) findViewById(R.id.sub_tv_receiverdata);
        sub_tv_cleanmsg = (TextView) findViewById(R.id.sub_tv_cleanmsg);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        submode = sub_sp_submode.getSelectedItem().toString().trim();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public class OnSubscribeReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("onSubscribeAck")) {
                Bundle bundle = intent.getExtras();
                int messageId = bundle.getInt("messageId");
                String devId = bundle.getString("CliendID");
                int returnCode = bundle.getInt("returnCode");
                sub_tv_receiverdata.append("订阅回调显示：\n" + "MessageID为：" + messageId + "\nCliendID为：" + devId
                        +
                        "\n返回标识符为：" + returnCode + "\n");
            } else if (action.equals("onDisSubscribeAck")) {
                Bundle bundle = intent.getExtras();
                int messageId = bundle.getInt("messageId");
                String devId = bundle.getString("CliendID");
                int returnCode = bundle.getInt("returnCode");
                sub_tv_receiverdata.append("取消订阅回调显示：\n" + "MessageID为：" + messageId + "\nCliendID为: " + devId
                        + "\n返回标识符" + returnCode + "\n");
            } else if (action.equals("onReceiveEvent")) {
                Bundle bundle = intent.getExtras();
                int messageId = bundle.getInt("messageId");
                String topic = bundle.getString("topic");
                byte[] datas = bundle.getByteArray("data");
                sub_tv_receiverdata.append("接受消息回调显示：\n" + "MessageID为：" + messageId + "\nTopic为： " + topic + "\n收到的消息为(HEX)： " + bytes2hex01(datas) + "\n");
            }
        }
    }


    @Override
    public void setListener() {
        super.setListener();
        sub_btn_suresub.setOnClickListener(this);
        sub_sp_submode.setOnItemSelectedListener(this);
        sub_tv_cleanmsg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_btn_suresub:
                submsg = sub_et_submsg.getText().toString().trim();
                if (submode != null && !submode.equals("")) {
                    if (submode.equals("订阅单个设备($USR/DevTx/Id)")) {
                        if (!submsg.equals("")) {
                            myService.doSubscribeForDevId(submsg);
                        } else {
                            showToast("请先填写信息");
                        }
                    } else if (submode.equals("取消单个订阅($USR/DevTx/Id)")) {
                        if (!submsg.equals("")) {
                            myService.doDisSubscribeforDevId(submsg);
                        }
                    } else if (submode.equals("订阅账号下的全部设备($USR/Dev2App/帐号/+)")) {
                        myService.doSubscribeForUsername();
                    } else if (submode.equals("取消订阅账号下的全部设备($USR/Dev2App/帐号/+)")) {
                        myService.doDisSubscribeforuName();
                    }
                } else {
                    showToast("请先填写信息");
                }
                break;
            case R.id.sub_tv_cleanmsg:
                sub_tv_receiverdata.setText("");
                break;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unbindService(serviceConnection);
        this.unregisterReceiver(onSubscribeReceiver);
        if (sub_tv_receiverdata != null) {
            sub_tv_receiverdata.setText("");
        }
    }

}
