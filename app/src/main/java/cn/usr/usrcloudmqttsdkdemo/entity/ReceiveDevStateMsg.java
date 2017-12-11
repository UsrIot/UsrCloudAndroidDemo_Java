package cn.usr.usrcloudmqttsdkdemo.entity;

/**
 * Created by Administrator on 2017/12/1 0001.
 */

public class ReceiveDevStateMsg {

    private DevState devStatus;

    public DevState getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(DevState devStatus) {
        this.devStatus = devStatus;
    }

    @Override
    public String toString() {
        return "ReceiveDevStateMsg{" +
                "devStatus=" + devStatus +
                '}';
    }
}
