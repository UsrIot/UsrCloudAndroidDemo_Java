package cn.usr.usrcloudmqttsdkdemo.business;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.usr.UsrCloudMqttCallbackAdapter;
import cn.usr.usrcloudmqttsdkdemo.base.UsrApplication;


import static android.content.ContentValues.TAG;
import static cn.usr.usrcloudmqttsdkdemo.utils.BaseUtils.bytes2hex01;

/**
 * Created by shizhiyuan on 2017/7/21.
 */

public class UsrCloudClientCallback extends UsrCloudMqttCallbackAdapter {

    private Context mcontext = UsrApplication.getInstance();

    @Override
    public void onConnectAck(int returnCode, String description) {
        super.onConnectAck(returnCode, description);
        Log.d(TAG, returnCode + "\n" + description);
        Intent intent = new Intent();
        intent.setAction("onConnectAck");//用隐式意图来启动广播
        intent.putExtra("onConnectAckreturnCode", returnCode);
        intent.putExtra("onConnectAckdescription", description);
        mcontext.sendBroadcast(intent);
    }



    @Override
    public void onSubscribeAck(int messageId, String clientId, String topics, int returnCode) {
        super.onSubscribeAck(messageId, clientId, topics, returnCode);
        Log.d(TAG, "messageId:{}" + messageId + "\nclientId:" + clientId + "\ntopics:" + topics + "\nreturnCode" + returnCode);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        intent.setAction("onSubscribeAck");//用隐式意图来启动广播
        bundle.putInt("messageId", messageId);
        bundle.putString("CliendID", clientId);
        bundle.putString("topics", topics);
        bundle.putInt("returnCode", returnCode);
        intent.putExtras(bundle);
        mcontext.sendBroadcast(intent);
    }

    @Override
    public void onReceiveParsedEvent(int messageId, String topic, String jsonData) {
        Log.d(TAG, "messageId:" + messageId + "\ntopic:" + topic + "\njsonData" + jsonData);
        Intent intent = new Intent();
        intent.setAction("onReceiveParsedEvent");//用隐式意图来启动广播
        Bundle bundle = new Bundle();
        bundle.putInt("messageId", messageId);
        bundle.putString("topic", topic);
        bundle.putString("jsonData", jsonData);
        intent.putExtras(bundle);
        mcontext.sendBroadcast(intent);
    }

    @Override
    public void onDisSubscribeAck(int messageId, String clientId, String topics, int returnCode) {
        Log.d(TAG, "messageId:{}" + messageId + "\nclientId:" + clientId + "\ntopics:" + topics + "\nreturnCode" + returnCode);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        intent.setAction("onDisSubscribeAck");//用隐式意图来启动广播
        bundle.putInt("messageId", messageId);
        bundle.putString("CliendID", clientId);
        bundle.putString("topics", topics);
        bundle.putInt("returnCode", returnCode);
        intent.putExtras(bundle);
        mcontext.sendBroadcast(intent);
    }

    @Override
    public void onPublishDataAck(int messageId, String topic, boolean isSuccess) {
        Log.d(TAG, "messageId:" + messageId + "\ntopic\n" + topic + "isSuccess\n" + isSuccess);
        Intent intent = new Intent();
        intent.setAction("onPublishDataAck");//用隐式意图来启动广播
        Bundle bundle = new Bundle();
        bundle.putInt("messageId", messageId);
        bundle.putString("topic", topic);
        bundle.putBoolean("isSuccess", isSuccess);
        intent.putExtras(bundle);
        mcontext.sendBroadcast(intent);
    }

    @Override
    public void onPublishDataResult(int messageId, String topic) {
        Log.d(TAG, "messageId:" + messageId + "\ntopic\n" + topic);
        Intent intent = new Intent();
        intent.setAction("onPublishDataResult");//用隐式意图来启动广播
        Bundle bundle = new Bundle();
        bundle.putInt("messageId", messageId);
        bundle.putString("topic", topic);
        intent.putExtras(bundle);
        mcontext.sendBroadcast(intent);
    }


    @Override
    public void onReceiveEvent(int messageId, String topic, byte[] data) {
        Log.d(TAG, messageId + "\n" + bytes2hex01(data));
        Intent intent = new Intent();
        intent.setAction("onReceiveEvent");//用隐式意图来启动广播
        Bundle bundle = new Bundle();
        bundle.putInt("messageId", messageId);
        bundle.putString("topic", topic);
        bundle.putByteArray("data", data);
        intent.putExtras(bundle);
        mcontext.sendBroadcast(intent);
    }






}
