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

import com.alibaba.fastjson.JSON;


import cn.usr.usrcloudmqttsdkdemo.R;
import cn.usr.usrcloudmqttsdkdemo.base.UsrBaseActivity;
import cn.usr.usrcloudmqttsdkdemo.business.UsrCloudClientService;
import cn.usr.usrcloudmqttsdkdemo.entity.DataPoints;
import cn.usr.usrcloudmqttsdkdemo.entity.DevState;
import cn.usr.usrcloudmqttsdkdemo.entity.OptionResponse;
import cn.usr.usrcloudmqttsdkdemo.entity.ReceiveDataPointsMsg;
import cn.usr.usrcloudmqttsdkdemo.entity.ReceiveDataPointsResponse;
import cn.usr.usrcloudmqttsdkdemo.entity.ReceiveDevAlarm;
import cn.usr.usrcloudmqttsdkdemo.entity.ReceiveDevStateMsg;


/**
 * Created by Administrator on 2017/11/30 0030.
 */

public class SubscribeParseActivity extends UsrBaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private String submode = "";
    private String submsg = "";
    private TextView sub_tv_receiverdata_parse;
    private TextView sub_tv_cleanmsg_parse;
    private Button sub_btn_suresub_parse;
    private Spinner sub_sp_submode_parse;
    private EditText sub_et_submsg_parse;

    private OnSubscribeParseReceiver onSubscribeParseReceiver;
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
        setContentView(R.layout.activity_subscribe_parse);
        initView();
    }

    @Override
    public void initView() {

        sub_sp_submode_parse = (Spinner) findViewById(R.id.sub_sp_submode_parse);
        sub_et_submsg_parse = (EditText) findViewById(R.id.sub_et_submsg_parse);
        sub_btn_suresub_parse = (Button) findViewById(R.id.sub_btn_suresub_parse);
        sub_tv_receiverdata_parse = (TextView) findViewById(R.id.sub_tv_receiverdata_parse);
        sub_tv_cleanmsg_parse = (TextView) findViewById(R.id.sub_tv_cleanmsg_parse);
    }


    @Override
    protected void onStart() {
        super.onStart();
        setListener();
        final Intent intent = new Intent(this, UsrCloudClientService.class);
        bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);


        onSubscribeParseReceiver = new OnSubscribeParseReceiver();
        //新添代码，在代码中注册广播接收程序
        IntentFilter filter = new IntentFilter();
        //TODO
        filter.addAction("onSubscribeAck");
        filter.addAction("onDisSubscribeAck");
        filter.addAction("onReceiveParsedEvent");
        registerReceiver(onSubscribeParseReceiver, filter);
    }

    @Override
    public void setListener() {
        sub_btn_suresub_parse.setOnClickListener(this);
        sub_sp_submode_parse.setOnItemSelectedListener(this);
        sub_tv_cleanmsg_parse.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sub_btn_suresub_parse:
                submsg = sub_et_submsg_parse.getText().toString().trim();
                if (submode != null && !submode.equals("")) {
                    if (submode.equals("订阅指定设备JSON格式数据($USR/DevJsonTx/Id)")) {
                        if (!submsg.equals("")) {
                            myService.doSubscribeParsedByDevId(submsg);
                        } else {
                            showToast("请先填写信息");
                        }
                    } else if (submode.equals("取消订阅设备JSON格式数据($USR/DevJsonTx/Id)")) {
                        if (!submsg.equals("")) {
                            myService.doDisSubscribeParsedforDevId(submsg);
                        } else {
                            showToast("请先填写信息");
                        }
                    } else if (submode.equals("订阅账号下全部设备监控状态($USR/JsonTx/帐号/+)")) {
                        myService.doSubscribeParsedForUsername();
                    } else if (submode.equals("取消订阅账号下的全部设备监控状态($USR/JsonTx/帐号/+)")) {
                        myService.doDisSubscribeParsedForUsername();
                    }
                } else {
                    showToast("请先填写信息");
                }
                break;
            case R.id.sub_tv_cleanmsg_parse:
                sub_tv_receiverdata_parse.setText("");
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        submode = sub_sp_submode_parse.getSelectedItem().toString().trim();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public class OnSubscribeParseReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("onSubscribeAck")) {
                Bundle bundle = intent.getExtras();
                int messageId = bundle.getInt("messageId");
                String cliendID = bundle.getString("CliendID");
                String topics = bundle.getString("topics");
                int returnCode = bundle.getInt("returnCode");
                sub_tv_receiverdata_parse.append("订阅回调显示："
                        + "\nMessageID为：" + messageId
                        + "\nCliendID为：" + cliendID
                        + "\ntopics为：" + topics
                        + "\n订阅结果：" + (returnCode==0?"成功":"失败")
                        + "\n---------------------------------------------------------end>\r\n");
            } else if (action.equals("onDisSubscribeAck")) {
                Bundle bundle = intent.getExtras();
                int messageId = bundle.getInt("messageId");
                String cliendID = bundle.getString("CliendID");
                int returnCode = bundle.getInt("returnCode");
                String topics = bundle.getString("topics");
                sub_tv_receiverdata_parse.append("订阅回调显示："
                        + "\nMessageID为：" + messageId
                        + "\nCliendID为：" + cliendID
                        + "\ntopics为：" + topics
                        + "\n订阅结果：" + (returnCode==0?"成功":"失败")
                        + "\n---------------------------------------------------------end>\r\n");
            } else if (action.equals("onReceiveParsedEvent")) {
                Bundle bundle = intent.getExtras();
                String msg = "";
                int messageId = bundle.getInt("messageId");
                String topic = bundle.getString("topic");
                String jsondata = bundle.getString("jsonData");
                if (jsondata.contains("devStatus")) {
                    ReceiveDevStateMsg devState = JSON.parseObject(jsondata, ReceiveDevStateMsg.class);
                    msg = "\n接受解析消息回调显示：\n"
                            + "MessageID为：" + messageId
                            + "\nTopic为： " + topic
                            + "\n收到设备状态变化"
                            + "\n设备名称为：" + devState.getDevStatus().getDevName()
                            + "\n状态为：" + (devState.getDevStatus().getStatus() == 1 ? "上线" : "下线"
                            + "\n------------------------------------------------------------end>\r\n");
                } else if (jsondata.contains("optionResponse")) {
                    ReceiveDataPointsResponse receiveResponse = JSON.parseObject(jsondata, ReceiveDataPointsResponse.class);
                    msg = "接受解析消息回调显示：\n"
                            + "MessageID为：" + messageId
                            + "\nTopic为： " + topic
                            + "\n收到数据点操作应答"
                            + "\n设备名称为：" + receiveResponse.getDevName();
                    StringBuilder optionr = new StringBuilder();
                    for (OptionResponse or : receiveResponse.getOptionResponse()) {
                        optionr.append("\n操作结果：" + (or.getResult().equals("1") ? "成功" : "失败")
                                + "\n数据点ID:" + or.getDataId()
                                + "\n操作:" + (or.getOption().equals("1") ? "待发送" : "已发送"));
                    }
                    optionr.append("\n------------------------------------------------------------end>\r\n");
                    msg += optionr;
                } else if (jsondata.contains("dataPoints")) {
                    ReceiveDataPointsMsg receiveDataPointsMsg = JSON.parseObject(jsondata, ReceiveDataPointsMsg.class);
                    msg = "接受解析消息回调显示：\n"
                            + "MessageID为：" + messageId
                            + "\nTopic为： " + topic
                            + "\n收到数据点数据:"
                            + "\n设备名称为：" + receiveDataPointsMsg.getDevName();
                    StringBuilder optionr = new StringBuilder();
                    for (DataPoints or : receiveDataPointsMsg.getDataPoints()) {
                        optionr.append("\n数据点List============"
                                + "\n数据点ID:" + or.getPointId()
                                + "\n数值：" + or.getValue()
                                + "\n=======================");
                    }
                    optionr.append("\n------------------------------------------------------------end>\r\n");
                    msg += optionr;
                } else if (jsondata.contains("devAlarm")) {
                    ReceiveDevAlarm receiveDevAlarm = JSON.parseObject(jsondata, ReceiveDevAlarm.class);
                    msg = "接受解析消息回调显示：\n"
                            + "MessageID为：" + messageId
                            + "\nTopic为： " + topic
                            + "\n收到设备报警推送:"
                            + "\n设备名称为:：" + receiveDevAlarm.getDevAlarm().getDevName()
                            + "\n数据点ID:" + receiveDevAlarm.getDevAlarm().getPointId()
                            + "\n数据点名称:" + receiveDevAlarm.getDevAlarm().getDataName()
                            + "\n触发报警的值:" + receiveDevAlarm.getDevAlarm().getValue()
                            + "\n设定的报警值:" + receiveDevAlarm.getDevAlarm().getAlarmValue()
                            + "\n设定的报警值:" + (receiveDevAlarm.getDevAlarm().getAlarmState().equals("1")?"开始报警":"恢复正常")
                            + "\n------------------------------------------------------------end>\r\n";
                }
                sub_tv_receiverdata_parse.append(msg);
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        this.unbindService(serviceConnection);
        this.unregisterReceiver(onSubscribeParseReceiver);
        if (sub_tv_receiverdata_parse != null) {
            sub_tv_receiverdata_parse.setText("");
        }
    }
}
