package cn.usr.usrcloudmqttsdkdemo.entity;

/**
 * Created by Administrator on 2017/12/1 0001.
 */

public class DevState {

    private String devName;

    private int status;

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DevState{" +
                "devName='" + devName + '\'' +
                ", status=" + status +
                '}';
    }
}
