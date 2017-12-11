package cn.usr.usrcloudmqttsdkdemo.entity;

/**
 * Created by Administrator on 2017/12/1 0001.
 */

public class DevAlarm {
    private String devName;
    private String pointId;
    private String dataName;
    private String value;
    private String alarmValue;
    private String alarmCondition;
    private String alarmState;

    public String getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(String alarmState) {
        this.alarmState = alarmState;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAlarmValue() {
        return alarmValue;
    }

    public void setAlarmValue(String alarmValue) {
        this.alarmValue = alarmValue;
    }

    public String getAlarmCondition() {
        return alarmCondition;
    }

    public void setAlarmCondition(String alarmCondition) {
        this.alarmCondition = alarmCondition;
    }

    @Override
    public String toString() {
        return "DevAlarm{" +
                "devName='" + devName + '\'' +
                ", pointId='" + pointId + '\'' +
                ", dataName='" + dataName + '\'' +
                ", value='" + value + '\'' +
                ", alarmValue='" + alarmValue + '\'' +
                ", alarmCondition='" + alarmCondition + '\'' +
                ", alarmState='" + alarmState + '\'' +
                '}';
    }
}
