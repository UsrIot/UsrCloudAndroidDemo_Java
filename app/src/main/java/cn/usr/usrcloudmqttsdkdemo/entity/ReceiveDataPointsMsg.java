package cn.usr.usrcloudmqttsdkdemo.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/1 0001.
 */

public class ReceiveDataPointsMsg {

    private List<DataPoints> dataPoints;


    private String devName;

    public List<DataPoints> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPoints> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    @Override
    public String toString() {
        return "ReceiveDataPointsMsg{" +
                "dataPoints=" + dataPoints +
                ", devName='" + devName + '\'' +
                '}';
    }
}
