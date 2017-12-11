package cn.usr.usrcloudmqttsdkdemo.entity;

/**
 * Created by Administrator on 2017/12/1 0001.
 */

public class ReceiveDevAlarm {

    private DevAlarm devAlarm;

    public DevAlarm getDevAlarm() {
        return devAlarm;
    }

    public void setDevAlarm(DevAlarm devAlarm) {
        this.devAlarm = devAlarm;
    }

    @Override
    public String toString() {
        return "ReceiveDevAlarm{" +
                "devAlarm=" + devAlarm +
                '}';
    }
}
