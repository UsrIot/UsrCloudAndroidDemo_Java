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

import org.apache.commons.lang.StringUtils;

import cn.usr.usrcloudmqttsdkdemo.R;
import cn.usr.usrcloudmqttsdkdemo.base.UsrBaseActivity;
import cn.usr.usrcloudmqttsdkdemo.business.UsrCloudClientService;
import cn.usr.usrcloudmqttsdkdemo.entity.DataPoints;
import cn.usr.usrcloudmqttsdkdemo.entity.OptionResponse;
import cn.usr.usrcloudmqttsdkdemo.entity.ReceiveDataPointsMsg;
import cn.usr.usrcloudmqttsdkdemo.entity.ReceiveDataPointsResponse;
import cn.usr.usrcloudmqttsdkdemo.entity.ReceiveDevAlarm;
import cn.usr.usrcloudmqttsdkdemo.entity.ReceiveDevStateMsg;

import static cn.usr.usrcloudmqttsdkdemo.utils.BaseUtils.bytes2hex01;

/**
 * Created by Administrator on 2017/11/30 0030.
 */

public class PublishParseActivity extends UsrBaseActivity implements View.OnClickListener {

    private EditText pub_et_parse_set_pointid;
    private EditText pub_et_parse_set_pointvalue;
    private EditText pub_et_parse_query_pointid;
    private EditText pub_et_parse_devid;
    private EditText pub_et_parse_slaveIndex;


    private Button pub_btn_parse_query_pubdata;
    private Button pub_btn_parse_set_pubdata;

    private TextView pub_tv_parse_showmsg;
    private TextView pub_tv_parse_cleanmsg;


    private UsrCloudClientService myService;
    private OnPublishParseReceiver onPublishParseReceiver;
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


    private String sts = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_parse);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setListener();


        final Intent intent = new Intent(this, UsrCloudClientService.class);
        bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
        onPublishParseReceiver = new OnPublishParseReceiver();
        //新添代码，在代码中注册广播接收程序
        IntentFilter filter = new IntentFilter();
        filter.addAction("onPublishDataAck");
        filter.addAction("onPublishDataResult");
        filter.addAction("onReceiveParsedEvent");
        registerReceiver(onPublishParseReceiver, filter);
    }


    @Override
    public void initView() {
        super.initView();
        pub_et_parse_devid = (EditText) findViewById(R.id.pub_et_parse_devid);
        pub_et_parse_set_pointid = (EditText) findViewById(R.id.pub_et_parse_set_pointid);
        pub_et_parse_set_pointvalue = (EditText) findViewById(R.id.pub_et_parse_set_pointvalue);
        pub_et_parse_query_pointid = (EditText) findViewById(R.id.pub_et_parse_query_pointid);

        pub_et_parse_slaveIndex = (EditText) findViewById(R.id.pub_et_parse_slaveIndex);

        pub_btn_parse_query_pubdata = (Button) findViewById(R.id.pub_btn_parse_query_pubdata);
        pub_btn_parse_set_pubdata = (Button) findViewById(R.id.pub_btn_parse_set_pubdata);

        pub_tv_parse_showmsg = (TextView) findViewById(R.id.pub_tv_parse_showmsg);
        pub_tv_parse_cleanmsg = (TextView) findViewById(R.id.pub_tv_parse_cleanmsg);
    }


    @Override
    public void setListener() {
        pub_btn_parse_query_pubdata.setOnClickListener(this);
        pub_btn_parse_set_pubdata.setOnClickListener(this);
        pub_tv_parse_cleanmsg.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pub_btn_parse_query_pubdata:
                String devid = pub_et_parse_devid.getText().toString().trim();
                String slaveIndex = pub_et_parse_slaveIndex.getText().toString().trim();

                String pointId = pub_et_parse_query_pointid.getText().toString().trim();
                if (StringUtils.isEmpty(devid) || StringUtils.isEmpty(slaveIndex) || StringUtils.isEmpty(pointId)) {
                    showToast("请先填写信息");
                } else {
                    if (!StringUtils.isEmpty(sts)) {
                        myService.doDisSubscribeParsedforDevId(sts);
                    }
                    myService.publishParsedQueryDataPoint(devid, slaveIndex, pointId);
                    sts = devid;
                    myService.doSubscribeParsedByDevId(devid);
                }
                break;
            case R.id.pub_btn_parse_set_pubdata:
                String set_devid = pub_et_parse_devid.getText().toString().trim();
                String set_slaveIndex = pub_et_parse_slaveIndex.getText().toString().trim();
                String set_pointId = pub_et_parse_set_pointid.getText().toString().trim();
                String set_pointValue = pub_et_parse_set_pointvalue.getText().toString().trim();
                if (StringUtils.isEmpty(set_devid) || StringUtils.isEmpty(set_slaveIndex) || StringUtils.isEmpty(set_pointId) || StringUtils.isEmpty(set_pointValue)) {
                    showToast("请先填写信息");
                } else {
                    if (!StringUtils.isEmpty(sts)) {
                        myService.doDisSubscribeParsedforDevId(sts);
                    }
                    myService.doSubscribeParsedByDevId(set_devid);
                    myService.publishParsedSetDataPoint(set_devid, set_slaveIndex, set_pointId, set_pointValue);
                    sts = set_devid;
                    myService.doSubscribeParsedByDevId(set_devid);
                }
                break;
            case R.id.pub_tv_parse_cleanmsg:
                pub_tv_parse_showmsg.setText("");
                break;

        }
    }

    public class OnPublishParseReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("onPublishDataAck")) {
                Bundle bundle = intent.getExtras();
                pub_tv_parse_showmsg.append("\n发布回调结果：\n"
                        + "MessageID为：" + bundle.getInt("messageId")
                        + "\n已发布topic：" + bundle.getString("topic")
                        + "\n发布结果：" + (bundle.getBoolean("isSuccess") ? "成功" : "失败")
                        + "\n-------------------------------------------------------------------->\r\n");
            } else if (action.equals("onPublishDataResult")) {
                Bundle bundle = intent.getExtras();
                pub_tv_parse_showmsg.append("\n本次结果发送回调：\n"
                        + "MessageID为：" + bundle.getInt("messageId")
                        + "\n本次发布topic：" + bundle.getString("topic")
                        + "\n------------------------------------------------------------------->\r\n");

            } else if (action.equals("onReceiveParsedEvent")) {
                Bundle bundle = intent.getExtras();
                String msg = "";
                int messageId = bundle.getInt("messageId");
                String topic = bundle.getString("topic");
                String jsondata = bundle.getString("jsonData");
                if (jsondata.contains("dataPoints")) {
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
                }
                pub_tv_parse_showmsg.append(msg);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unbindService(serviceConnection);
        this.unregisterReceiver(onPublishParseReceiver);
        if (pub_tv_parse_showmsg != null) {
            pub_tv_parse_showmsg.setText("");
        }
        if (!sts.equals("")) {
            myService.doDisSubscribeParsedforDevId(sts);
        }
    }
}
